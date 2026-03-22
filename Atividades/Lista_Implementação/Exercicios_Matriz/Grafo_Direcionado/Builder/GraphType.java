package Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Builder;

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
