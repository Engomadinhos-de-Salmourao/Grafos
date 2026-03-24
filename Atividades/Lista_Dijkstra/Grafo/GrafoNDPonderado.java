package Lista_Dijkstra.Grafo;

public class GrafoNDPonderado implements Grafo {

    private	int n;
    private	int arestas;
    private Float[][] adj;

    public GrafoNDPonderado(int n){
        this.n = n;
        this.arestas = 0;
        this.adj = new Float[n][n];
        for(int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                this.adj[i][j]= Float.POSITIVE_INFINITY;
            }
        }
    }

    public void insertA(int v, int w, float weight) {
        if(adj[v][w].equals(Float.POSITIVE_INFINITY)){
            adj[v][w] = weight;
            adj[w][v] = weight;
            arestas+=2;
        }
        else{
            System.out.println("Já existe uma aresta entre os nós " + v + " e " + w );
        }
    }

    public void show() {
        System.out.println("n: " + n );
        System.out.println("m: " + arestas);
        for( int i=0; i < n; i++){
            System.out.print("\n");
            for( int w=0; w < n; w++)
                if(!adj[i][w].equals(Float.POSITIVE_INFINITY)){
                    System.out.print("Adj[" + i + "," + w + "]= " + adj[i][w] + " ");
                }
                else {System.out.print("Adj[" + i + "," + w + "]= " + Float.POSITIVE_INFINITY + " ");}
        }
        System.out.println("\n\nfim da impressão do grafo." );
    }

    public int getN() {
        return n;
    }

    public Float[][] getAdj() {
        return adj;
    }
}
