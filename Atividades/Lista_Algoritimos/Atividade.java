package Lista_Algoritimos;

import Lista_Algoritimos.Builder.GrafoBuilder;
import Lista_Algoritimos.Builder.GrafoNDBuilder;
import Lista_Algoritimos.Grafo.TGrafo;
import Lista_Algoritimos.Grafo.TGrafoND;

import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        TGrafo grafo = GrafoBuilder.archiveReader("./Builder/GrafoD.txt");
        TGrafoND grafoND = GrafoNDBuilder.archiveReader("./Builder/GrafoND.txt");

        System.out.println("Largura Grafo Direcionado: ");
        grafo.largura(0);
        System.out.println("\n\nLargura Grafo Não Direcionado: ");
        grafoND.largura(0);
        System.out.println("\n\nProfundidade Grafo Direcionado: ");
        grafo.profundidade(0);
        System.out.println("\n\nProfundidade Grafo Não Direcionado: ");
        grafoND.profundidade(0);
    }
}
