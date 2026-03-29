package app.ports;

import app.domain.GrafoDestino;

public interface GrafoRepositoryPort {
    GrafoDestino carregarPorDestino(Integer id);
    GrafoDestino carregar();
    void adicionarAresta(int v, int w, int tempo, int dist);
    void adicionarLugar(Integer id);
    void removerAresta(int v, int w);
    void removerLugar(Integer id);
    void removerDestino(Integer id);

}
