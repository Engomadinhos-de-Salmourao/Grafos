package Lista_Implementação.Exercicios_ListaAdjacencia.Grafo;

import java.util.Objects;

public class TGrafoLista {
    private	int n;
    private	int m;
    private	TNo adj[];

    public TGrafoLista( int n ) {
        this.n = n;
        this.m = 0;
        this.adj = new TNo[n];
    }

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

    public boolean isComplete(){return this.m == this.n*this.n-this.n;}

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

    public void removeV(int v) {
        while (adj[v] != null) {
            removeA(v, adj[v].w);
        }

        for (int i = 0; i < n; i++) {
            if (i != v) {
                removeA(i, v);
            }
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

    public boolean equal(TGrafoLista grafo){
        if(this.n != grafo.getN() || grafo.getM()!= this.m) return false;
        TNo aux;
        TNo aux_grafo;
        for(int i=0; i<this.n; i++){
            aux = this.adj[i];
            aux_grafo = grafo.getAdj()[i];
            while(aux != null && aux_grafo != null){
                if(aux.w != aux_grafo.w){
                    return false;
                }
                aux = aux.prox;
                aux_grafo = aux_grafo.prox;
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

    public void insereA( int v, int w) {
        TNo novoNo;
        TNo no = adj[v];
        TNo ant = null;

        while( no != null && w >= no.w ){
            if( w == no.w)
                return;
            ant = no;
            no = no.prox;
        };

        novoNo = new TNo();
        novoNo.w = w;
        novoNo.prox = no;

        if( ant == null){
            adj[v] = novoNo;
        } else
            ant.prox = novoNo;
        m++;
    }

    public void removeA(int v, int w) {
        TNo no = adj[v];
        TNo ant = null;

        while (no != null && no.w != w) {
            ant = no;
            no = no.prox;
        }

        if (no != null) {
            if (ant == null) {
                // removendo o primeiro nó
                adj[v] = no.prox;
            } else {
                ant.prox = no.prox;
            }
            no.prox = null;
            m--;
        }
    }

    public void show() {
        System.out.print("n: " + n);
        System.out.print("\nm: " + m + "\n");
        for( int i=0; i < n; i++){
            System.out.print("\n" + i + ": ");
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

    public int getM() {
        return m;
    }
}
