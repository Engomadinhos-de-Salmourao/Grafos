package Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Grafo;

import java.util.Objects;

public class TGrafoPonderadoMatriz implements Grafo {
    private	int n;
    private	int arestas;
    private Float[][] adj;

    public TGrafoPonderadoMatriz(int n){
        this.n = n;
        this.arestas = 0;
        this.adj = new Float[n][n];
        for(int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                this.adj[i][j]= Float.POSITIVE_INFINITY;
            }
        }
    }
    @Override
    public int inDegree(int v) {
        int degree = 0;
        for(int i=0; i<n;i++){
            if (!adj[i][v].equals(Float.POSITIVE_INFINITY)){
                degree += 1;
            }
        }
        return degree;
    }

    @Override
    public int outDegree(int v) {
        int degree = 0;
        for(int i=0; i<n;i++){
            if(!adj[v][i].equals(Float.POSITIVE_INFINITY)){
                degree += 1;
            }
        }
        return degree;
    }

    @Override
    public int degree(int v) {return this.inDegree(v) + this.outDegree(v);
    }

    @Override
    public boolean nodeSource(int v) {return this.outDegree(v) >= 1 && this.inDegree(v) == 0;}

    @Override
    public boolean nodeReceiver(int v) {return this.inDegree(v) >= 1 && this.outDegree(v)==0;}

    @Override
    public boolean symmetric() {
        for(int i=0;i<n;i++){
            for(int j=0; j<n; j++){
                if(!Objects.equals(this.adj[i][j], this.adj[j][i])){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isComplete() {
        return this.arestas == this.n*this.n-this.n;
    }


    public void insertA(int v, int w, float weight) {
        if(adj[v][w].equals(Float.POSITIVE_INFINITY)){
            adj[v][w] = weight;
            arestas++;
        }
        else{
            System.out.println("Já existe uma aresta entre os nós " + v + " e " + w );
        }
    }

    @Override
    public void removeA(int v, int w) {
        if(!adj[v][w].equals(Float.POSITIVE_INFINITY)){
            adj[v][w] = Float.POSITIVE_INFINITY;
            arestas--;
        }
        else{
            System.out.println("Não existe uma aresta entre os nós " + v + " e " + w);
        }
    }

    @Override
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
}
