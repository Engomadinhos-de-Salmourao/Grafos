package app.ports;

import app.domain.Destino;

import java.io.IOException;
import java.util.List;

public interface DestinoRepositotyPort{
    void salvarDestino(String nome) throws IOException;
    void removerdestino(int  id) throws IOException;
    List<Destino> getAll();
}
