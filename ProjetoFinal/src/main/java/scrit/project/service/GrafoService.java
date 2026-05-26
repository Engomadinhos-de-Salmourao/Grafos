package scrit.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import scrit.project.domain.GrafoDestino;
import scrit.project.domain.Lugar;
import scrit.project.repository.GrafoFileRepository;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrafoService {

    private final GrafoFileRepository grafoFileRepository;
    private GrafoDestino grafo;

    /**
     * Carrega (ou recarrega) o grafo a partir do grafo.txt.
     * Se o arquivo estiver vazio ou não existir, retorna um grafo vazio com n=0
     * sem lançar exceção — o sistema deve funcionar mesmo sem dados iniciais.
     */
    public GrafoDestino carregarGrafo() throws IOException {
        log.info("Carregando grafo do arquivo...");
        this.grafo = grafoFileRepository.carregar(); // já trata arquivo vazio/inexistente
        log.info("Grafo carregado: {} vértices, {} arestas.", grafo.getN(), grafo.getM() / 2);
        return grafo;
    }

    /**
     * Retorna o grafo em memória; se ainda não foi carregado, carrega do arquivo.
     * Nunca retorna null — no pior caso retorna um GrafoDestino(0).
     */
    public GrafoDestino obterGrafo() throws IOException {
        if (grafo == null) {
            carregarGrafo();
        }
        return grafo;
    }

    public void adicionarLugar(Lugar lugar) throws IOException {
        obterGrafo().insereV(lugar);
        log.debug("Vértice adicionado: {}", lugar.getNome());
    }

    public void removerLugar(int id) throws IOException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id do lugar inválido: " + id);
        }
        GrafoDestino g = obterGrafo();
        if (id > g.getN()) {
            throw new IllegalArgumentException(
                    "Id " + id + " fora do intervalo válido (1–" + g.getN() + ").");
        }
        g.removeV(id);
        log.debug("Vértice removido: id={}", id);
    }

    public void adicionarAresta(Integer v, Integer w, Float dist, Float tempo) throws IOException {
        if (v == null || w == null || v <= 0 || w <= 0) {
            throw new IllegalArgumentException("Ids dos lugares devem ser maiores que zero.");
        }
        if (v.equals(w)) {
            throw new IllegalArgumentException("Uma aresta não pode ligar um lugar a ele mesmo.");
        }
        if (dist == null || dist < 0) {
            throw new IllegalArgumentException("Distância não pode ser negativa.");
        }
        if (tempo == null || tempo < 0) {
            throw new IllegalArgumentException("Tempo não pode ser negativo.");
        }
        GrafoDestino g = obterGrafo();
        if (v > g.getN() || w > g.getN()) {
            throw new IllegalArgumentException(
                    "Vértice fora do intervalo. O grafo tem " + g.getN() + " vértice(s).");
        }
        g.insereA(v - 1, w - 1, dist, tempo); // converte base-1 → base-0
    }

    public void removerAresta(Integer v, Integer w) throws IOException {
        if (v == null || w == null || v <= 0 || w <= 0 || v.equals(w)) {
            throw new IllegalArgumentException("Ids inválidos para remoção da aresta.");
        }
        obterGrafo().removeA(v - 1, w - 1);
    }

    public void gravarGrafo() throws IOException {
        if (grafo == null) {
            throw new IllegalStateException("Nenhum grafo carregado em memória para gravar.");
        }
        log.info("Gravando grafo no arquivo...");
        grafoFileRepository.gravar(grafo);
    }

    public void showGrafo() throws IOException {
        obterGrafo().show();
    }

    public String conexidade() throws IOException {
        GrafoDestino g = obterGrafo();
        if (g.getN() == 0) return "Grafo vazio";
        return (g.conexidade() == 1) ? "Conexo" : "Não Conexo";
    }

    public String mostrarConteudoArquivo() throws IOException {
        return grafoFileRepository.lerConteudo();
    }

    public void setGrafo(GrafoDestino grafo) {
        this.grafo = grafo;
    }
}
