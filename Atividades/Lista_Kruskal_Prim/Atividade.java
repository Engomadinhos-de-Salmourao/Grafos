package Lista_Kruskal_Prim;

import Lista_Kruskal_Prim.Builder.Builder;
import Lista_Kruskal_Prim.Grafo.GrafoNDPonderado;

import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        GrafoNDPonderado grafo1 = Builder.archiveReader("./grafo1.txt");
        GrafoNDPonderado grafo2 = Builder.archiveReader("./grafo2.txt");

        Prim.prim(grafo1).show();
        System.out.println("/n/n/n/");
        Prim.prim(grafo2).show();


    }
}
