package Lista_coloração.Grafo;

import java.util.*;

public class TGrafoND {
    private int n;
    private int arestas;
    private int[][] adj;

    public TGrafoND(int n) {
        this.n = n;
        this.arestas = 0;
        this.adj = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.adj[i][j] = 0;
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
            for (int m = 0; m < this.n; m++) {
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

    public void coloracao() {
        Map<Integer, List<Integer>> classesCores = new HashMap<>();
        Set<Integer> W = new LinkedHashSet<>();
        for (int i = 0; i < this.n; i++) {
            W.add(i);
        }

        int k = 1;
        while (!W.isEmpty()) {
            List<Integer> classeAtual = new ArrayList<>();
            classesCores.put(k, classeAtual);

            Iterator<Integer> iterator = W.iterator();
            while (iterator.hasNext()) {
                int i = iterator.next();
                if (intersecaoVazia(i, classeAtual)) {
                    classeAtual.add(i);
                    iterator.remove();
                }
            }

            k++;
        }

        exibirColoracao(classesCores);
    }

    private boolean intersecaoVazia(int v, List<Integer> classeCor) {
        for (int vizinho : classeCor) {
            if (adj[v][vizinho] == 1) {
                return false;
            }
        }
        return true;
    }

    public void show() {
        System.out.println("n: " + n);
        System.out.println("m: " + arestas);
        for (int i = 0; i < n; i++) {
            System.out.print("\n");
            for (int w = 0; w < n; w++) {
                System.out.print("Adj[" + (i + 1) + "," + (w + 1) + "]= " + adj[i][w] + " ");
            }
        }
        System.out.println("\n\nfim da impressão do grafo.");
    }

    public int getN() {
        return n;
    }

    public int[][] getAdj() {
        return adj;
    }

    private void exibirColoracao(Map<Integer, List<Integer>> classes) {
        System.out.println("\n--- Resultado da Coloração Sequencial ---");
        for (Map.Entry<Integer, List<Integer>> entry : classes.entrySet()) {
            System.out.println("Cor " + entry.getKey() + ": Vértices " + entry.getValue());
        }
        System.out.println("Número Cromático Sugerido (k): " + classes.size());
    }
}
