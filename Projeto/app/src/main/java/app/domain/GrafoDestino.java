package app.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter

public class  GrafoDestino {
    private int n;
    private int m;
    private Tno[] adj;
    @Setter
    private Map<Integer, Lugar> lugares;



    public GrafoDestino(int n) {
        this.n = n;
        this.m = 0;
        this.adj = new Tno[n];
        this.lugares = new HashMap<>();
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
            Tno aux = this.adj[n];
            while(aux != null){

                if (!visitados.contains(aux.getLugar())) {
                    fila.add(aux.getLugar());
                    visitados.add(aux.getLugar());
                }
                aux = aux.getProximo();
            }
        }
        return visitados;
    }

    public void insereA(Integer v, Integer w, Float dist, Float tempo) {
        Tno novoNo;
        Tno no = adj[v];
        Tno ant = null;

        Tno novoNo2;
        Tno no2 = this.adj[w];
        Tno ant2 = null;

        while(no != null && w >= no.getLugar()){
            if(w.equals(no.getLugar()))
                return;
            ant = no;
            no = no.getProximo();
        }

        while( no2 != null && v >= no2.getLugar()){
            if(v.equals(no2.getLugar()))
                return;
            ant2 = no2;
            no2 = no2.getProximo();
        }

        novoNo = new Tno();
        novoNo.setLugar(w);
        novoNo.setProximo(no);
        novoNo.setDist(dist);
        novoNo.setTempoDeslocamento(tempo);

        novoNo2 = new Tno();
        novoNo2.setLugar(v);
        novoNo2.setProximo(no2);
        novoNo2.setDist(dist);
        novoNo2.setTempoDeslocamento(tempo);

        if( ant == null){
            adj[v] = novoNo;
        } else{
            ant.setProximo(novoNo);
        }

        if( ant2 == null){
            adj[w] = novoNo2;
        } else{
            ant2.setProximo(novoNo2);
        }

        m+=2;
    }

    public void removeA(int v, int w) {
        Tno no = adj[v];
        Tno ant = null;

        Tno no2 = adj[w];
        Tno ant2 = null;

        while (no != null && no.getLugar() != w) {
            ant = no;
            no = no.getProximo();
        }

        while (no2 != null && no2.getLugar() != v) {
            ant2 = no2;
            no2 = no2.getProximo();
        }

        if (no != null && no2 != null) {
            if (ant == null) {
                adj[v] = no.getProximo();
            } else {
                ant.setProximo(no.getProximo());
            }

            if (ant2 == null) {
                adj[w] = no2.getProximo();
            } else {
                ant2.setProximo(no2.getProximo());
            }

            no.setProximo(null);
            no2.setProximo(null);

            m -= 2;

        } else {
            System.out.println("Não existe aresta entre " + v + " e " + w);
        }
    }

    public void insereV(Lugar lugar){
        Tno[] aux = new Tno[n+1];
        for (int i = 0; i < n; i++) {
            aux[i] = adj[i];
        }

        n++;
        adj = aux;
        lugares.put(this.n, lugar);
    }

    public void removeV(int v) {
        int pos = v-1;
        while (adj[pos] != null) {
            removeA(pos, adj[pos].getLugar());
        }

        Tno[] novoAdj = new Tno[n - 1];
        Map<Integer, Lugar> novosLugares = new HashMap<>();

        int novoIndice = 0;
        for (int i = 0; i < n; i++) {
            if (i == pos) {
                continue;
            }

            novoAdj[novoIndice] = adj[i];

            Lugar lugar = lugares.get(i+1);
            lugar.setId(novoIndice+1);
            novosLugares.put(novoIndice+1, lugar);

            novoIndice++;
        }

        for (int i = 0; i < novoAdj.length; i++) {
            Tno aux = novoAdj[i];

            while (aux != null) {
                if (aux.getLugar() > v) {
                    aux.setLugar(aux.getLugar() - 1);
                }
                aux = aux.getProximo();
            }
        }

        adj = novoAdj;
        lugares = novosLugares;
        n--;
    }


    public void show() {
        System.out.print("n: " + n);
        System.out.print("\nm: " + m + "\n");
        for( int i=0; i < n; i++){
            System.out.print("\n" + (i+1) + ": ");
            Tno no = adj[i];
            while( no != null ){
                System.out.print("(id: " +(no.getLugar()+1) + "; distância: " + no.getDist() + "; tempo: " + no.getTempoDeslocamento() + ") ");
                no = no.getProximo();
            }
        }
        System.out.print("\n\nfim da impressao do grafo.\n");
    }
    
}
