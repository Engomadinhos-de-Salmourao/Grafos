package Exercicios_Matriz.Grafo_Direcionado;

public interface Grafo {
    int inDegree(int v);
    int outDegree(int v);
    int degree(int v);
    boolean nodeSource(int v);
    boolean nodeReceiver(int v);
    boolean symmetric();
    boolean isComplete();
    void removeA(int v, int w);
    void show();
}
