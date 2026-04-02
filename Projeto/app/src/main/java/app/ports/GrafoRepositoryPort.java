package app.ports;

import app.domain.GrafoDestino;
import app.domain.Lugar;

import java.io.IOException;

public interface GrafoRepositoryPort {
    GrafoDestino carregar() throws IOException;
    void gravar(GrafoDestino grafo) throws IOException;
    void mostrarConteudo() throws IOException;
}
