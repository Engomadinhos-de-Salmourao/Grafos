package Lista_Dijkstra.Grafo;

public interface Grafo {
   void insertA(int v, int w, float weight);
   void show();
   int getN();
   Float[][] getAdj();
}
