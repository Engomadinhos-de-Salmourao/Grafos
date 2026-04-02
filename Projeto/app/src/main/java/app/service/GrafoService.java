package app.service;

import app.domain.Destino;
import app.domain.GrafoDestino;
import app.domain.Lugar;
import app.outside_adapters.DestinoRepository;
import app.outside_adapters.GrafoRepository;
import app.ports.DestinoRepositotyPort;
import app.ports.GrafoRepositoryPort;

import java.io.IOException;
import java.util.List;

public class GrafoService {

    public GrafoDestino grafo;
    private final DestinoRepositotyPort destinoRepository = new DestinoRepository();
    private final GrafoRepositoryPort grafoRepository = new GrafoRepository();

    public List<Destino> getAllDestinos(){
        return destinoRepository.getAll();
    }

    public void adicionarLugar(Lugar lugar) throws IOException {
        grafo.insereV(lugar);
    }

    public void removerLugar(int id) throws IOException {
        if (id <= 0) {
            throw new IllegalArgumentException("Id do lugar inválido.");
        }

        grafo.removeV(id);
    }

    public void adicionarAresta(Integer v, Integer w, Float dist, Float tempo) throws IOException {
        if (v <= 0 || w <= 0) {
            throw new IllegalArgumentException("Ids dos lugares devem ser maiores que zero.");
        }

        if (v.equals(w)) {
            throw new IllegalArgumentException("Uma aresta não pode ligar um lugar a ele mesmo.");
        }

        if (tempo < 0) {
            throw new IllegalArgumentException("Tempo não pode ser negativo.");
        }

        if (dist < 0) {
            throw new IllegalArgumentException("Distância não pode ser negativa.");
        }

        grafo.insereA(v-1, w-1, dist, tempo);
    }

    public void removerAresta(Integer v, Integer w) throws IOException {
        if (v <= 0 || w <= 0 || v.equals(w)) {
            throw new IllegalArgumentException("Ids inválidos para remoção da aresta.");
        }

        grafo.removeA(v-1, w-1);
    }

    public GrafoDestino obterGrafo() throws IOException {
        this.grafo = grafoRepository.carregar();
        return grafo;
    }

    public void showGrafo(){grafo.show();}

    public String conexidade(){return (grafo.conexidade() == 1)?"Conexo":"Não Conexo";}

    public void gravar() throws IOException {
        grafoRepository.gravar(grafo);
    }

    public void mostrarConteudo() throws IOException {
        grafoRepository.mostrarConteudo();
    }
}
