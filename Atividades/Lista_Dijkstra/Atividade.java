package Lista_Dijkstra;



import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        GrafoNDPonderado g = Builder.archiveReader("Lista_Dijkstra/build.txt");
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.dijkstra_algoritimo(g, 3);
    }
}
