package Lista_Algoritimos.Grafo;


import java.util.*;

public class TGrafo {
    private	int n;
    private	int arestas;
    private Integer[][] adj;
    
    public TGrafo(int n){
        this.n = n;
        this.arestas = 0;
        this.adj = new Integer [n][n];
        for(int i = 0; i< n; i++){
            for(int j = 0; j< n; j++){
                this.adj[i][j]=0;
            }
        }
    }
    
    public void insertA(int v, int w) {
        if(adj[v][w].equals(0)){
            adj[v][w] = 1;
            arestas++;
        }
        else{
            System.out.println("Já existe uma aresta entre os nós ");
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

    // ... (métodos profundidade e largura omitidos para brevidade)

    /**
     * Implementação da Coloração Sequencial baseada no Algoritmo fornecido.
     * Complexidade: O(n^2) devido à matriz de adjacência.
     */
    public void coloracao() {
        // Map para representar as classes de cores Ci. Chave = Cor (k), Valor = Lista de Vértices
        Map<Integer, List<Integer>> classesCores = new HashMap<>();

        // Loop: para i <- 1 até n faça (usando 0 até n-1 para Java)
        for (int i = 0; i < this.n; i++) {
            int k = 1; // k <- 1
            boolean atribuido = false;

            // Simulação do LOOP / goto
            while (!atribuido) {
                // Inicializa a classe de cor se não existir (Ci <- Ø)
                classesCores.putIfAbsent(k, new ArrayList<>());

                // Checa a condição: se N(i) ∩ Ck = Ø
                if (intersecaoVazia(i, classesCores.get(k))) {
                    // Ck <- Ck ∪ { i }
                    classesCores.get(k).add(i);
                    atribuido = true;
                } else {
                    // k++; goto LOOP;
                    k++;
                }
            }
        }

        exibirColoracao(classesCores);
    }

    /**
     * Verifica se o vértice v não possui vizinhos dentro da classe de cor informada.
     */
    private boolean intersecaoVazia(int v, List<Integer> classeCor) {
        for (int vizinho : classeCor) {
            // Na matriz de adjacência, adj[v][vizinho] == 1 significa que são vizinhos
            if (adj[v][vizinho] == 1) {
                return false; // Interseção não é vazia (conflito de cor)
            }
        }
        return true;
    }

    private void exibirColoracao(Map<Integer, List<Integer>> classes) {
        System.out.println("\n--- Resultado da Coloração Sequencial ---");
        for (Map.Entry<Integer, List<Integer>> entry : classes.entrySet()) {
            System.out.println("Cor " + entry.getKey() + ": Vértices " + entry.getValue());
        }
        System.out.println("Número Cromático Sugerido (k): " + classes.size());
    }
}


}
