package Lista_Algoritimos;

import java.util.*;

public class TGrafoND {
    private	int n;
    private	int arestas;
    private int[][] adj;

    public TGrafoND(int n){
        this.n = n;
        this.arestas = 0;
        this.adj = new int [n][n];

        for(int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                this.adj[i][j]=0;
            }
        }
    }

    public void insertA(int v, int w){
        if(adj[v][w] == 0 ){
            adj[v][w] = 1;
            adj[w][v] = 1;
            arestas++;
        }
        else{
            System.out.println("Já existe uma aresta entre os nós " + v + " e " + w);
        }
    }

    public void profundidade(int inicio) {
        List<Integer> visitados = new ArrayList<>();
        Stack<Integer> pilha = new Stack<>();

        int n;
        visitados.add(inicio);
        pilha.add(inicio);
        System.out.print(inicio + " ");

        while (!pilha.isEmpty()) {
            n = pilha.pop();
            for (int m = 0; m<this.n; m++) {
                if (adj[n][m] == 0) {
                    continue;
                }
                if (!visitados.contains(m)) {
                    pilha.add(n);
                    visitados.add(m);
                    System.out.print(m + " ");
                    n = m;
                }
            }
        }
    }



    public void largura(int inicio) {
        List<Integer> visitados = new ArrayList<>();
        Queue<Integer> fila = new LinkedList<>();

        visitados.add(inicio);
        fila.add(inicio);
        while (!fila.isEmpty()) {
            int n = fila.remove();
            System.out.print(n + " ");
            for (int m = 0; m<this.n; m++) {
                if (adj[n][m] == 0) {
                    continue;
                }
                if (!visitados.contains(m)) {
                    fila.add(m);
                    visitados.add(m);
                }
            }
        }
    }


}
