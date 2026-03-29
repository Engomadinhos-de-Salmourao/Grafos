package app.outside_adapters;

import app.domain.GrafoDestino;
import app.ports.GrafoRepositoryPort;

public class GrafoRepository implements GrafoRepositoryPort {
    @Override
    public GrafoDestino carregarPorDestino(Integer id) {
        return null;
    }

    @Override
    public GrafoDestino carregar() {
        return null;
    }

    @Override
    public void adicionarAresta(int v, int w, int tempo, int dist) {

    }

    @Override
    public void adicionarLugar(Integer id) {

    }

    @Override
    public void removerAresta(int v, int w) {

    }

    @Override
    public void removerLugar(Integer id) {

    }

    @Override
    public void removerDestino(Integer id) {

    }
}
