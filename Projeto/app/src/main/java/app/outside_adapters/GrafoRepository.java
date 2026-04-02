package app.outside_adapters;

import app.domain.*;
import app.ports.GrafoRepositoryPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrafoRepository implements GrafoRepositoryPort {

    private final String caminhoArquivo = "./src/main/java/app/files/grafo.txt";

    @Override
    public GrafoDestino carregar() throws IOException {
        Path caminho = Path.of(caminhoArquivo);
        List<String> linhas = Files.readAllLines(caminho);

        int quantidadeLugares = Integer.parseInt(linhas.get(1).trim());
        GrafoDestino grafo = new GrafoDestino(quantidadeLugares);

        Map<Integer, Lugar> lugaresPorId = new HashMap<>();

        int inicioLugares = 2;
        int fimLugares = inicioLugares + quantidadeLugares - 1;
        int linhaQtdConexoes = fimLugares + 2;

        if (linhaQtdConexoes >= linhas.size()) {
            throw new IllegalArgumentException("Arquivo inválido: seção de conexões não encontrada.");
        }

        for (int i = inicioLugares; i <= fimLugares; i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) {
                continue;
            }

            Lugar lugar = linhaParaLugar(linha);
            lugaresPorId.put(lugar.getId(), lugar);
        }

        grafo.setLugares(lugaresPorId);

        for (int i = linhaQtdConexoes + 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) {
                continue;
            }

            String[] partes = linha.split(";");
            Integer idOrigem = Integer.parseInt(partes[0]);
            Integer idDestino = Integer.parseInt(partes[1]);
            Float dist = Float.parseFloat(partes[2]);
            Float tempo = Float.parseFloat(partes[3]);

            if (!lugaresPorId.containsKey(idOrigem) || !lugaresPorId.containsKey(idDestino)) {
                throw new IllegalStateException(
                        "Conexão inválida no arquivo. Origem: " + idOrigem + ", destino: " + idDestino
                );
            }

            grafo.insereA(idOrigem-1, idDestino-1, dist, tempo);
        }

        return grafo;
    }

    @Override
    public void gravar(GrafoDestino grafo) throws IOException {
        Path caminho = Path.of(caminhoArquivo);
        List<String> linhas = new ArrayList<>();

        linhas.add("3");
        linhas.add(String.valueOf(grafo.getN()));

        Map<Integer, Lugar> lugares = grafo.getLugares();
        for (int i = 1; i <= grafo.getN(); i++) {
            Lugar lugar = lugares.get(i);
            if (lugar == null) {
                throw new IllegalStateException("Lugar não encontrado para o id: " + i);
            }
            linhas.add(lugarParaLinha(lugar));
        }

        linhas.add("");
        int quantidadeArestas = grafo.getM() / 2;
        linhas.add(String.valueOf(quantidadeArestas));
        for (int i = 0; i < grafo.getN(); i++) {
            Tno aux = grafo.getAdj()[i];

            while (aux != null) {
                // evita duplicar arestas em grafo não direcionado
                if (i < aux.getLugar()) {
                    linhas.add((i+1) + ";" + (aux.getLugar()+1) + ";" + aux.getDist() + ";" + aux.getTempoDeslocamento()
                    );
                }
                aux = aux.getProximo();
            }
        }

        Files.write(caminho, linhas);
    }

    @Override
    public void mostrarConteudo() throws IOException {
        Path caminho = Path.of(caminhoArquivo);
        List<String> linhas = Files.readAllLines(caminho);
        for(String linha: linhas){
            System.out.println(linha);
        }
    }


    private Lugar linhaParaLugar(String linha) {
        String[] partes = linha.split(";", 10);

        int id = Integer.parseInt(partes[0]);
        int idDestino = Integer.parseInt(partes[1]);
        String nome = partes[2];
        String descricao = partes[3];
        double custo = Double.parseDouble(partes[4]);
        float tempo = Float.parseFloat(partes[5]);
        float score = Float.parseFloat(partes[6]);
        int numReviews = Integer.parseInt(partes[7]);
        String horarios = partes[8];
        String tipo = partes[9];

        Lugar lugar = new Lugar();


        lugar.setId(id);
        lugar.setId_destino(idDestino);
        lugar.setNome(nome);
        lugar.setDescription(descricao);
        lugar.setCusto(custo);
        lugar.setTempo(tempo);
        lugar.setScore(score);
        lugar.setNum_reviews(numReviews);
        lugar.setHorarios(textoParaHorarios(horarios));


        switch (tipo.toUpperCase()){
            case "HOTEL":
                lugar.setTipo(TypeLugar.HOTEL);
                break;
            case "PONTO_TURISTICO":
                lugar.setTipo(TypeLugar.PONTO_TURISTICO);
                break;
            case "RESTAURANTE":
                lugar.setTipo(TypeLugar.RESTAURANTE);
                break;
            default:
                throw new IllegalArgumentException("Tipo de lugar inválido: " + tipo);
        }

        return lugar;
    }

    private List<Set<Time>> textoParaHorarios(String texto) {
        List<Set<Time>> horarios = new ArrayList<>();

        if (texto == null || texto.isBlank() || texto.equals("[]")) {
            return horarios;
        }

        String conteudo = texto.trim();

        if (conteudo.startsWith("[") && conteudo.endsWith("]")) {
            conteudo = conteudo.substring(1, conteudo.length() - 1).trim();
        }

        if (conteudo.isEmpty()) {
            return horarios;
        }

        Pattern pattern = Pattern.compile("\\[(.*?)]");
        Matcher matcher = pattern.matcher(conteudo);

        while (matcher.find()) {
            String grupo = matcher.group(1).trim();
            Set<Time> conjunto = new LinkedHashSet<>();

            if (!grupo.isEmpty()) {
                String[] horariosTexto = grupo.split(",");

                for (String horario : horariosTexto) {
                    String valor = horario.trim();
                    if (!valor.isEmpty()) {
                        conjunto.add(Time.valueOf(valor));
                    }
                }
            }

            horarios.add(conjunto);
        }

        return horarios;
    }

    private String lugarParaLinha(Lugar lugar) {
        return lugar.getId() + ";" +
                lugar.getId_destino() + ";" +
                lugar.getNome() + ";" +
                lugar.getDescription() + ";" +
                lugar.getCusto() + ";" +
                lugar.getTempo() + ";" +
                lugar.getScore() + ";" +
                lugar.getNum_reviews() + ";" +
                horariosParaTexto(lugar.getHorarios()) + ";" +
                lugar.getTipo().name();
    }

    private String horariosParaTexto(List<Set<Time>> horarios) {
        if (horarios == null) {
            return "[]";
        }
        return horarios.toString();
    }
}
