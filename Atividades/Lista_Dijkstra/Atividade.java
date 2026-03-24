package Lista_Dijkstra;



import Lista_Dijkstra.Builder.Builder;
import Lista_Dijkstra.Builder.Builder_Direcionado;
import Lista_Dijkstra.Grafo.GrafoNDPonderado;
import Lista_Dijkstra.Grafo.GrafoPonderado;

import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        GrafoPonderado g = Builder_Direcionado.archiveReader("./grafo2.txt");
        GrafoNDPonderado g_nd = Builder.archiveReader("./grafo1.txt");
        Dijkstra dijkstra = new Dijkstra();

        System.out.println("Aplicando Dijkstra para Grafo Direcionado do Exemplo: ");
        dijkstra.dijkstra_algoritimo(g, 3);

        System.out.println("\n\nAplicando Dijkstra para Grafo Não Direcionado do Exemplo: ");
        dijkstra.dijkstra_algoritimo(g, 3);



    }
}
