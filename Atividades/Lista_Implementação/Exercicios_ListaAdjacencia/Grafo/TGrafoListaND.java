package Lista_Implementação.Exercicios_ListaAdjacencia.Grafo;

import java.util.Objects;

public class TGrafoListaND {
    private	int n;
    private	int m;
    private	TNo adj[];

    public TGrafoListaND( int n ) {
        this.n = n;
        this.m = 0;
        this.adj = new TNo[n];
    };

    public int inDegree(int v) {
        int qtde = 0;
        for (int i = 0; i < this.n; i++) {
            if (i != v){
                TNo aux = this.adj[i];
                while (aux != null) {
                    if (aux.w == v) {
                        qtde++;
                        break;
                    }
                    aux = aux.prox;
                }
            }
        }

        return qtde;
    }

    public int outDegree(int v) {
        int qtde = 0;
        TNo aux = this.adj[v];
        while (aux != null) {
            qtde++;
            aux = aux.prox;
        }

        return qtde;
    }

    public int degree(int v) {return inDegree(v) + outDegree(v);}

    public boolean fonte(int v) {return (this.inDegree(v)==0 && this.outDegree(v) > 0);}

    public boolean sorvedouro(int v) {return (this.inDegree(v)>0 && this.outDegree(v)==0);}

    public boolean isComplete(){
        return this.m == fatorial(this.n)/(fatorial(this.n - 2) * fatorial(2));
    }

    public boolean simetrico() {
        int[][] adj = new int[this.n][this.n];

        for (int i = 0; i<this.n; i++) {
            TNo aux = this.adj[i];
            while (aux != null) {
                int j = aux.w;
                adj[i][j] = 1;
                aux = aux.prox;
            }
        }

        for(int i=0;i<this.n;i++){
            for(int j=0; j<this.n; j++){
                if(!Objects.equals(adj[i][j], adj[j][i])){
                    return false;
                }
            }
        }
        return true;
    }

    public void inverteListas() {
        for (int i = 0; i < this.n; i++) {
            TNo atual = this.adj[i];
            TNo ant = null;
            TNo prox;

            while (atual != null) {
                prox = atual.prox;
                atual.prox = ant;
                ant = atual;
                atual = prox;
            }

            this.adj[i] = ant;
        }
    }

    public void removeV(int v) {
        while (adj[v] != null) {
            removeA(v, adj[v].w); // já remove v↔w
        }

        for (int i = v; i < n - 1; i++) {
            adj[i] = adj[i + 1];
        }
        adj[n - 1] = null;

        for (int i = 0; i < n - 1; i++) {
            TNo aux = adj[i];
            while (aux != null) {
                if (aux.w > v) {
                    aux.w--;
                }
                aux = aux.prox;
            }
        }

        n--;
    }

    public int fatorial(int num){
        if(num == 0) return 1;
        int result = 1;
        for(int i=1; i<=num; i++){
            result *= i;
        }
        return result;
    }

    public void insereA( int v, int w) {
        TNo novoNo;
        TNo no = adj[v];
        TNo ant = null;

        TNo novoNo2;
        TNo no2 = this.adj[w];
        TNo ant2 = null;

        while( no != null && w >= no.w ){
            if( w == no.w)
                return;
            ant = no;
            no = no.prox;
        }

        while( no2 != null && v >= no2.w){
            if(v == no2.w)
                return;
            ant2 = no2;
            no2 = no2.prox;
        }

        novoNo = new TNo();
        novoNo.w = w;
        novoNo.prox = no;

        novoNo2 = new TNo();
        novoNo2.w = v;
        novoNo2.prox = no2;

        if( ant == null){
            adj[v] = novoNo;
        } else{
            ant.prox = novoNo;
        }

        if( ant2 == null){
            adj[w] = novoNo2;
        } else{
            ant2.prox = novoNo2;
        }

        m+=2;
    }

    public void removeA(int v, int w) {

        TNo no = adj[v];
        TNo ant = null;

        TNo no2 = adj[w];
        TNo ant2 = null;

        // procura w na lista de v
        while (no != null && no.w != w) {
            ant = no;
            no = no.prox;
        }

        // procura v na lista de w
        while (no2 != null && no2.w != v) {
            ant2 = no2;
            no2 = no2.prox;
        }

        if (no != null && no2 != null) {

            // remove de v -> w
            if (ant == null) {
                adj[v] = no.prox;
            } else {
                ant.prox = no.prox;
            }

            // remove de w -> v
            if (ant2 == null) {
                adj[w] = no2.prox;
            } else {
                ant2.prox = no2.prox;
            }

            // limpa referências
            no.prox = null;
            no2.prox = null;

            m -= 2;

        } else {
            System.out.println("Não existe aresta entre " + v + " e " + w);
        }
    }

    public void show() {
        System.out.print("n: " + n);
        System.out.print("\nm: " + m + "\n");
        for( int i=0; i < n; i++){
            System.out.print("\n" + i + ": ");
            // Percorre a lista na posição i do vetor
            TNo no = adj[i];
            while( no != null ){
                System.out.print(no.w + " ");
                no = no.prox;
            }
        }
        System.out.print("\n\nfim da impressao do grafo.\n");
    }

    public int getN() {
        return n;
    }

    public TNo[] getAdj() {
        return adj;
    }
}
