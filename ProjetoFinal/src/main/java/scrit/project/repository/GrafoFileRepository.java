package scrit.project.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import scrit.project.domain.Destino;
import scrit.project.domain.GrafoDestino;
import scrit.project.domain.Lugar;
import scrit.project.domain.Tno;
import scrit.project.domain.TypeLugar;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Repositório responsável por ler e gravar o grafo em arquivo .txt.
 *
 * Formato do grafo.txt:
 * <pre>
 * &lt;numDestinos&gt;
 * &lt;numVertices&gt;
 * id;destinoId;nome;descricao;custo;tempoPermanencia;score;numReviews;horariosJson;tipo
 * ... (uma linha por vértice)
 *
 * &lt;numArestas&gt;
 * v;w;dist;tempo   (v e w em base-1)
 * ...
 * </pre>
 */
@Slf4j
@Repository
public class GrafoFileRepository {

    @Value("${grafo.arquivo.caminho:src/main/java/scrit/project/files/grafo.txt}")
    private String caminhoArquivo;

    // ─────────────────────────────── CARREGAR ───────────────────────────────

    /**
     * Carrega o grafo do arquivo. Retorna um GrafoDestino(0) vazio se o arquivo
     * não existir ou estiver vazio/corrompido — nunca lança exceção por ausência de dados.
     */
    public GrafoDestino carregar() throws IOException {
        Path path = Paths.get(caminhoArquivo);

        if (!Files.exists(path) || Files.size(path) == 0) {
            log.warn("grafo.txt não encontrado ou vazio em '{}'. Retornando grafo vazio.", caminhoArquivo);
            return new GrafoDestino(0);
        }

        log.info("Carregando grafo de '{}'", caminhoArquivo);

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8))) {

            String linhaDestinos = lerLinhaNaoVazia(br);
            if (linhaDestinos == null) {
                log.warn("grafo.txt sem conteúdo válido. Retornando grafo vazio.");
                return new GrafoDestino(0);
            }

            // numDestinos é informativo; lemos mas não usamos para limitar o parse
            int numDestinos = parseIntSafe(linhaDestinos, 0);
            log.debug("Número de destinos declarados no arquivo: {}", numDestinos);

            String linhaVertices = lerLinhaNaoVazia(br);
            if (linhaVertices == null) {
                log.warn("grafo.txt: linha de numVertices ausente. Retornando grafo vazio.");
                return new GrafoDestino(0);
            }

            int numVertices = parseIntSafe(linhaVertices, 0);
            log.debug("Número de vértices declarados: {}", numVertices);

            GrafoDestino grafo = new GrafoDestino(numVertices);
            Map<Integer, Lugar> lugares = new HashMap<>();

            for (int i = 0; i < numVertices; i++) {
                String linha = lerLinhaNaoVazia(br);
                if (linha == null) {
                    log.warn("grafo.txt: esperados {} vértices, encontrados {}. Interrompendo leitura.", numVertices, i);
                    break;
                }
                try {
                    Lugar lugar = parseLugar(linha);
                    lugares.put(lugar.getId(), lugar);
                } catch (Exception e) {
                    log.warn("Linha de vértice inválida (ignorada): '{}' — {}", linha, e.getMessage());
                }
            }
            grafo.setLugares(lugares);

            String linhaArestas = lerLinhaNaoVazia(br);
            if (linhaArestas == null) {
                log.info("grafo.txt sem seção de arestas. Grafo com {} vértices e 0 arestas.", grafo.getN());
                return grafo;
            }

            int numArestas = parseIntSafe(linhaArestas, 0);
            log.debug("Número de arestas declaradas: {}", numArestas);

            for (int i = 0; i < numArestas; i++) {
                String linha = lerLinhaNaoVazia(br);
                if (linha == null) break;
                try {
                    parseAresta(linha, grafo);
                } catch (Exception e) {
                    log.warn("Linha de aresta inválida (ignorada): '{}' — {}", linha, e.getMessage());
                }
            }

            log.info("Grafo carregado com sucesso: {} vértices, {} arestas (não-direcionadas).",
                    grafo.getN(), grafo.getM() / 2);
            return grafo;
        }
    }

    // ─────────────────────────────── GRAVAR ─────────────────────────────────

    /**
     * Grava o estado atual do grafo em grafo.txt, sobrescrevendo o arquivo anterior.
     */
    public void gravar(GrafoDestino grafo) throws IOException {
        Path path = Paths.get(caminhoArquivo);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        log.info("Gravando grafo em '{}': {} vértices", caminhoArquivo, grafo.getN());

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8))) {

            Map<Integer, Lugar> lugares = grafo.getLugares();

            // Conta destinos únicos
            long numDestinos = lugares.values().stream()
                    .map(l -> l.getDestino() != null ? l.getDestino().getId() : 0)
                    .distinct()
                    .count();

            bw.write(String.valueOf(numDestinos));
            bw.newLine();
            bw.write(String.valueOf(grafo.getN()));
            bw.newLine();

            // Grava vértices em ordem de ID
            List<Integer> ids = new ArrayList<>(lugares.keySet());
            Collections.sort(ids);
            for (int id : ids) {
                bw.write(formatarLugar(lugares.get(id)));
                bw.newLine();
            }

            bw.newLine(); // linha em branco separa vértices de arestas

            // Grava arestas únicas (v < w)
            List<int[]> arestas = coletarArestasUnicas(grafo);
            bw.write(String.valueOf(arestas.size()));
            bw.newLine();
            for (int[] aresta : arestas) {
                int v = aresta[0];
                int w = aresta[1];
                Tno tno = grafo.getAresta(v, w);
                if (tno == null) tno = grafo.getAresta(w, v);
                if (tno == null) continue;
                bw.write(String.format(Locale.US, "%d;%d;%.1f;%.1f",
                        v + 1, w + 1, tno.getDist(), tno.getTempoDeslocamento()));
                bw.newLine();
            }

            bw.newLine();
        }

        log.info("Grafo gravado com sucesso.");
    }

    // ─────────────────────────────── LER CONTEÚDO ───────────────────────────

    public String lerConteudo() throws IOException {
        Path path = Paths.get(caminhoArquivo);
        if (!Files.exists(path)) {
            return "Arquivo grafo.txt não encontrado em: " + path.toAbsolutePath();
        }
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    // ─────────────────────────────── PARSE ──────────────────────────────────

    /**
     * Formato esperado (10 campos separados por ';'):
     * id;destinoId;nome;descricao;custo;tempoPermanencia;score;numReviews;horariosJson;tipo
     *
     * O horariosJson pode conter ponto-e-vírgula internamente? Não — por isso
     * usamos split com limite 10 para evitar quebras em campos futuros.
     */
    private Lugar parseLugar(String linha) {
        // Separa apenas os 10 primeiros campos; o 9º (horariosJson) pode conter vírgulas mas não ponto-e-vírgula.
        String[] partes = linha.split(";", 10);

        if (partes.length < 10) {
            throw new IllegalArgumentException(
                    "Linha de vértice inválida (esperado 10 campos, encontrado " + partes.length + "): " + linha);
        }

        Lugar lugar = new Lugar();
        lugar.setId(Integer.parseInt(partes[0].trim()));

        int destinoId = parseInt(partes[1].trim());
        Destino destino = new Destino();
        destino.setId(destinoId);
        lugar.setDestino(destino);

        lugar.setNome(partes[2].trim());
        lugar.setDescricao(partes[3].trim());
        lugar.setCusto(parseDouble(partes[4].trim()));
        lugar.setTempoPermanencia(parseFloat(partes[5].trim()));
        lugar.setScore(parseFloat(partes[6].trim()));
        lugar.setNumReviews(parseInt(partes[7].trim()));
        lugar.setHorariosJson(partes[8].trim());

        String tipoStr = partes[9].trim();
        try {
            lugar.setTipo(TypeLugar.valueOf(tipoStr));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("TypeLugar desconhecido: '" + tipoStr + "'");
        }

        return lugar;
    }

    private void parseAresta(String linha, GrafoDestino grafo) {
        String[] partes = linha.split(";");
        if (partes.length < 4) {
            log.warn("Linha de aresta inválida (ignorada): '{}'", linha);
            return;
        }

        int v = Integer.parseInt(partes[0].trim()) - 1; // base-1 → base-0
        int w = Integer.parseInt(partes[1].trim()) - 1;
        float dist  = parseFloat(partes[2].trim());
        float tempo = parseFloat(partes[3].trim());

        if (v < 0 || w < 0 || v >= grafo.getN() || w >= grafo.getN()) {
            log.warn("Aresta com vértice fora do range [0,{}]: v={} w={} — ignorada.",
                    grafo.getN() - 1, v, w);
            return;
        }

        grafo.insereA(v, w, dist, tempo);
    }

    // ─────────────────────────────── FORMATAR ───────────────────────────────

    private String formatarLugar(Lugar l) {
        int    destinoId = (l.getDestino() != null && l.getDestino().getId() != null) ? l.getDestino().getId() : 0;
        String horarios  = (l.getHorariosJson() != null) ? l.getHorariosJson() : "[]";
        double custo     = (l.getCusto()            != null) ? l.getCusto()            : 0.0;
        float  tempo     = (l.getTempoPermanencia() != null) ? l.getTempoPermanencia() : 0.0f;
        float  score     = (l.getScore()            != null) ? l.getScore()            : 0.0f;
        int    reviews   = (l.getNumReviews()       != null) ? l.getNumReviews()       : 0;

        return String.format(Locale.US, "%d;%d;%s;%s;%.1f;%.1f;%.1f;%d;%s;%s",
                l.getId(),
                destinoId,
                nvl(l.getNome()),
                nvl(l.getDescricao()),
                custo,
                tempo,
                score,
                reviews,
                horarios,
                l.getTipo().name());
    }

    private List<int[]> coletarArestasUnicas(GrafoDestino grafo) {
        List<int[]> arestas = new ArrayList<>();
        Tno[] adj = grafo.getAdj();

        for (int v = 0; v < grafo.getN(); v++) {
            Tno no = adj[v];
            while (no != null) {
                int w = no.getLugar();
                if (v < w) { // conta cada aresta uma só vez
                    arestas.add(new int[]{v, w});
                }
                no = no.getProximo();
            }
        }

        arestas.sort(Comparator.comparingInt((int[] a) -> a[0]).thenComparingInt(a -> a[1]));
        return arestas;
    }

    // ─────────────────────────────── UTILITÁRIOS ────────────────────────────

    /** Lê a próxima linha não vazia (ignora linhas em branco e comentários #). */
    private String lerLinhaNaoVazia(BufferedReader br) throws IOException {
        String linha;
        while ((linha = br.readLine()) != null) {
            linha = linha.trim();
            if (!linha.isEmpty() && !linha.startsWith("#")) return linha;
        }
        return null;
    }

    private int parseIntSafe(String s, int fallback) {
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return fallback; }
    }

    private double parseDouble(String s) {
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 0.0; }
    }

    private float parseFloat(String s) {
        try { return Float.parseFloat(s); } catch (NumberFormatException e) { return 0.0f; }
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }

    private String nvl(String s) {
        return (s != null) ? s : "";
    }
}
