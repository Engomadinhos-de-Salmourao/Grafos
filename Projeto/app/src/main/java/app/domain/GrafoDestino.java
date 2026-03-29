package app.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
public class  GrafoDestino {
    private int n;
    private int m;
    private Tno[] adj;
    private Destino destino;
    private List<Integer> lugares;


    public GrafoDestino(int n) {
        this.n = n;
        this.m = 0;
        this.adj = new Tno[n];
    }

    public GrafoDestino(Destino destino, int n){
        this.destino = destino;
        this.lugares = new ArrayList<>();
        this.n = n;
        this.m = 0;
        this.adj = new Tno[n];
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
                if(destino != null){
                    if (!visitados.contains(this.getPosition(aux.getLugar()))) {
                        fila.add(this.getPosition(aux.getLugar()));
                        visitados.add(this.getPosition(aux.getLugar()));
                    }
                }
                else{
                    if (!visitados.contains(aux.getLugar())) {
                        fila.add(aux.getLugar());
                        visitados.add(aux.getLugar());
                    }
                }

                aux = aux.getProximo();
            }
        }
        return visitados;
    }

    public void insereA(Integer v, Integer w, Float dist, Float tempo) {
        int pos;
        int pos2;
        if(this.destino != null){
            pos  = getPosition(v);
            pos2 = getPosition(w);
        }
        else{
            pos = v;
            pos2 = w;
        }


        Tno novoNo;
        Tno no = adj[pos];
        Tno ant = null;

        Tno novoNo2;
        Tno no2 = this.adj[pos2];
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
            adj[pos] = novoNo;
        } else{
            ant.setProximo(novoNo);
        }

        if( ant2 == null){
            adj[pos2] = novoNo2;
        } else{
            ant2.setProximo(novoNo2);
        }

        m+=2;
    }

    public void removeA(int v, int w) {
        int pos;
        int pos2;
        if(this.destino != null){
            pos  = getPosition(v);
            pos2 = getPosition(w);
        }
        else{
            pos = v;
            pos2 = w;
        }

        Tno no = adj[pos];
        Tno ant = null;

        Tno no2 = adj[pos2];
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
                adj[pos] = no.getProximo();
            } else {
                ant.setProximo(no.getProximo());
            }

            if (ant2 == null) {
                adj[pos2] = no2.getProximo();
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

    public void insereV(int v){
        Tno[] aux = new Tno[n+1];
        for (int i = 0; i < n; i++) {
            aux[i] = adj[i];
        }

        n++;
        adj = aux;
        if(destino != null) lugares.add(v);
    }

    public void removeV(int v) {
        int pos;
        if(this.destino != null){
            pos  = getPosition(v);
        } else{pos = v;}

        while (adj[pos] != null) {
            removeA(v, adj[v].getLugar());
        }

        Tno[] novoAdj = new Tno[n - 1];

        int j = 0;
        for (int i = 0; i < n; i++) {
            if (i == pos) {
                continue;
            }
            novoAdj[j] = adj[i];
            j++;
        }


        if(destino != null) lugares.remove(v);
        adj = novoAdj;
        n--;
    }

    private int getPosition(Integer v){return this.lugares.indexOf(v);}

    public void show() {
        System.out.print("n: " + n);
        System.out.print("\nm: " + m + "\n");
        for( int i=0; i < n; i++){
            System.out.print("\n" + i + ": ");
            Tno no = adj[i];
            while( no != null ){
                System.out.print("(id: " +no.getLugar() + "; distância: " + no.getDist() + "; tempo: " + no.getTempoDeslocamento() + ") ");
                no = no.getProximo();
            }
        }
        System.out.print("\n\nfim da impressao do grafo.\n");
    }
    
}
