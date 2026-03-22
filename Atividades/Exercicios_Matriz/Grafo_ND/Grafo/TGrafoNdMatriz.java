package Exercicios_Matriz.Grafo_ND.Grafo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TGrafoNdMatriz {
    private	int n;
    private	int arestas;
    private Integer[][] adj;


    public TGrafoNdMatriz(int n) {
        this.n = n;
        this.arestas = 0;
        this.adj = new Integer [n][n];

        for(int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                this.adj[i][j]=0;
            }
        }

    }

    public int inDegree(int v){
        int degree = 0;
        for(int i=0; i<n;i++){
            if(adj[i][v] == 1){
                degree += 1;
            }
        }
        return degree;
    }


    public int degree(int v){return this.inDegree(v);}

    public boolean nodeSource(int v){return this.inDegree(v) >= 1;}

    public boolean nodeReceiver(int v){return this.inDegree(v) >= 1;}

    public boolean isComplete(){
        return this.arestas == fatorial(this.n)/(fatorial(this.n - 2) * fatorial(2));
    }

    public int fatorial(int num){
        if(num == 0) return 1;
        int result = 1;
        for(int i=1; i<=num; i++){
            result *= i;
        }
        return result;
    }

    public TGrafoNdMatriz complement(){
        TGrafoNdMatriz gaux = new TGrafoNdMatriz(this.n);
        for(int i=0; i<this.n; i++){
            for(int j=0; j<this.n; j++){
                if(this.adj[i][j] == 0){
                    gaux.insertA(i, j);
                }
            }
        }
        return gaux;
    }

    public int conexidade(){
        List<Integer> visitados = this.largura(0);
        return (visitados.size() == n)?1:0;
    }

    public List<Integer> largura(int v){
        List<Integer> visitados = new ArrayList<>();
        Queue<Integer> fila = new LinkedList<>();

        visitados.add(v);
        fila.add(v);
        while (!fila.isEmpty()) {
            int n = fila.remove();
            for (int m = 0; m<this.n; m++) {
                if (this.adj[n][m] == 0) {
                    continue;
                }
                if (!visitados.contains(m)) {
                    fila.add(m);
                    visitados.add(m);
                }
            }
        }
        return visitados;
    }

    public void insertA(int v, int w) {
        if(adj[v][w] == 0 ){
            adj[v][w] = 1;
            adj[w][v] = 1;
            arestas++;
        }
        else{
            System.out.println("Já existe uma aresta entre os nós " + v + " e " + w);
        }
    }

    public void removeV(int v){
        Integer[][] aux = new Integer [n-1][n-1];
        int iaux=0;
        int jaux=0;
        for(int i=0; i<this.n; i++){
            for (int j=0; j<this.n; j++){
                if(j!=v && i !=v){
                    aux[iaux][jaux] = this.adj[i][j];
                    jaux++;
                }
                else if (this.adj[i][j] != 0){
                    this.removeA(i,j);
                    this.removeA(j,i);
                }

            }
            if(i != v){
                iaux++;
            }
            jaux = 0;
        }
        this.n--;
        this.adj = aux;
    }

    public void removeA(int v, int w) {
        if(adj[v][w] == 1 ){
            adj[v][w] = 0;
            adj[w][v] = 0;
            arestas--;
        }
        else{
            System.out.println("Não existe uma aresta entre os nós " + v + " e " + w);
        }
    }

    public void show() {
        System.out.println("n: " + n );
        System.out.println("m: " + arestas);
        for( int i=0; i < n; i++){
            System.out.print("\n");
            for( int w=0; w < n; w++)
                if(adj[i][w] == 1)
                    System.out.print("Adj[" + i + "," + w + "]= 1" + " ");
                else System.out.print("Adj[" + i + "," + w + "]= 0" + " ");
        }
        System.out.println("\n\nfim da impressao do grafo." );
    }
}
