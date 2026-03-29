package app.ports;

import app.domain.Destino;

public interface DestinoRepositotyPort{
    void salvarDestino(int id);
    void removerdestino(int  id);
    Destino getDestino(int id);
}
