import Lista_Implementação.Exercicios_ListaAdjacencia.Builder.TGrafoListaBuilder;
import Lista_Implementação.Exercicios_ListaAdjacencia.Builder.TGrafoListaNDBuilder;
import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TGrafoLista;
import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TGrafoListaND;

import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        String arquivo = "Atividades/Lista_Implementação.Exercicios_ListaAdjacencia/Builder/text.txt";
        TGrafoListaND g = TGrafoListaNDBuilder.archiveReader(arquivo);
        TGrafoLista g2 = TGrafoListaBuilder.archiveReader(arquivo);


    }
}
