package Exercicios_Matriz.Grafo_Direcionado.Grafo;

import java.util.*;

public class TGrafoMatriz implements Grafo {
    private	int n;
    private	int arestas;
    private Integer[][] adj;


    public TGrafoMatriz(int n){
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
            if (adj[i][v].equals(1)){
                degree += 1;
            }
        }
        return degree;
    }

    public int outDegree(int v){
        int degree = 0;
        for(int i=0; i<n;i++){
            if(adj[v][i].equals(1)){
                degree += 1;
            }
        }
        return degree;
    }

    public int degree(int v){return this.inDegree(v) + this.outDegree(v);}

    public boolean nodeSource(int v){return this.outDegree(v) >= 1 && this.inDegree(v) == 0;}

    public boolean nodeReceiver(int v){return this.inDegree(v) >= 1 && this.outDegree(v)==0;}

    public boolean symmetric(){
        for(int i=0;i<n;i++){
            for(int j=0; j<n; j++){
                if(!Objects.equals(this.adj[i][j], this.adj[j][i])){
                    return false;
                }
            }
        }
        return true;
    }

    public void removeV(int v){
        Integer[][] aux = new Integer [n-1][n-1];
        int iaux=0;
        int jaux=0;
        for(int i=0; i<this.n; i++){
            for (int j=0; j<this.n; j++){
                if(j!=v && i!=v){
                    aux[iaux][jaux] = this.adj[i][j];
                    jaux++;
                }
                else if(this.adj[i][j] != 0){
                    this.removeA(i,j);
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

    public boolean isComplete(){return this.arestas == this.n*this.n-this.n;}

    public int conexidade(){
        Integer[][] fd = new Integer[this.n][this.n];
        Integer[][] fi = new Integer[this.n][this.n];
        for(int i=0; i<this.n; i++){
            List<Integer> aux = this.fechoTD(i);
            for (int j=0;j<this.n;j++) {
                if(aux.contains(j)){
                    fd[i][j] = 1;
                }
                else{fd[i][j] = 0;}
            }
        }

        for(int i=0; i<this.n; i++){
            List<Integer> aux = this.fechoTI(i);
            for (int j=0;j<this.n;j++) {
                if(aux.contains(j)){
                    fi[i][j] = 1;
                }
                else{fi[i][j] = 0;}
            }
        }

        //C3

        boolean c3 = true;
        for(int i=0;i<this.n;i++){
            for(int j=0;j<this.n;j++){
                if(fd[i][j]==1 && fi[i][j]==1){;
                    continue;
                }
                else{
                    c3 = false;
                }
            }
        }

        if(c3){return 3;}

        //C2
        boolean c2 = true;
        Integer[][] union = new Integer[this.n][this.n];
        for(int i=0;i<this.n;i++){
            for(int j=0;j<this.n;j++){
                if(fd[i][j] ==0 && fi[i][j]==0){
                    c2 = false;
                    union[i][j] = 0;
                }
                else{
                    union[i][j] = 1;
                }
            }
        }

        if(c2){return 2;}

        //C1
        List<Integer> visitados = this.largura(0,union);
        return (visitados.size() == n)?1:0;

    }

    public TGrafoMatriz grafoReduzido(){
        List<List<Integer>> ciclos = new ArrayList<>();
        List<Integer> acessados = new ArrayList<>();
        Map<Integer, Integer> vertice = new HashMap<>();

        for(int v=0; v<this.n; v++){
            if(acessados.contains(v)){continue;}
            List<Integer> fd = this.fechoTD(v);
            List<Integer> fi = this.fechoTI(v);
            fd.retainAll(fi);


            if(!fd.isEmpty()){
                ciclos.add(fd);
                int cicloIdx = ciclos.size() - 1;
                for (Integer ver : fd) {
                    acessados.add(ver);
                    vertice.put(ver, cicloIdx);
                }
            }
        }

        TGrafoMatriz grafoReduzido = new TGrafoMatriz(ciclos.size());
        for(List<Integer> aux : ciclos){
            for(Integer i : aux){
                for(int j=0; j<this.n; j++){
                    if(this.adj[i][j] == 1 && !aux.contains(j)){
                        grafoReduzido.insertA(ciclos.indexOf(aux), vertice.get(j));
                    }
                }
            }
        }

        return grafoReduzido;
    }

    public List<Integer> largura(int v, Integer[][]union){
        List<Integer> visitados = new ArrayList<>();
        Queue<Integer> fila = new LinkedList<>();

        visitados.add(v);
        fila.add(v);
        while (!fila.isEmpty()) {
            int n = fila.remove();
            for (int m = 0; m<this.n; m++) {
                if (union[n][m] == 0) {
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

    public List<Integer> fechoTD(int n){
        List<Integer> aux = new ArrayList<>();
        aux.add(n);
        return this.sucessor(n, aux);
    }

    public List<Integer> fechoTI(int n){
        List<Integer> aux = new ArrayList<>();
        aux.add(n);
        return this.antecessor(n, aux);
    }

    private List<Integer> sucessor(int v, List<Integer> aux){
        for (int i=0; i<this.n; i++){
            if(i==v) continue;
            if(this.adj[v][i] == 1 && !aux.contains(i)){
                aux.add(i);
                aux = sucessor(i, aux);
            }
        }
        return aux;
    }

    private List<Integer> antecessor(int v, List<Integer> aux){
        for (int i=0; i<this.n; i++){
            if(i==v) continue;
            if(this.adj[i][v] == 1 && !aux.contains(i)){
                aux.add(i);
                aux = antecessor(i, aux);
            }
        }
        return aux;
    }

    public TGrafoMatriz complement(){
        TGrafoMatriz gaux = new TGrafoMatriz(this.n);
        for(int i=0; i<this.n; i++){
            for(int j=0; j<this.n; j++){
                if(this.adj[i][j].equals(0)){
                    gaux.insertA(i, j);
                }
            }
        }
        return gaux;
    }

    public void insertA(int v, int w) {
        if(adj[v][w].equals(0)){
            adj[v][w] = 1;
            arestas++;
        }
        else{
            System.out.println("Já existe uma aresta entre os nós " + v + " e " + w);
        }
    }

    public void removeA(int v, int w) {
        if(adj[v][w].equals(1)){
            adj[v][w] = 0;
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
                if(adj[i][w].equals(1)){
                    System.out.print("Adj[" + i + "," + w + "]= 1" + " ");
                }
                else {System.out.print("Adj[" + i + "," + w + "]= 0" + " ");}
        }
        System.out.println("\n\nfim da impressão do grafo." );
    }
}
