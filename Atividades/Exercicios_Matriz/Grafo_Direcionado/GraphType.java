package Exercicios_Matriz.Grafo_Direcionado;

public enum GraphType {
    PONDERADO("ponderado"),
    NAO_PONDERADO("não ponderado");

    String type;
    GraphType(String n) {
        this.type = n;
    }

    public String getType() {
        return type;
    }
}
