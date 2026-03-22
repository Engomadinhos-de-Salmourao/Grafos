package Lista_Dijkstra;



import java.util.ArrayList;
import java.util.List;

public class Dijkstra {

    public void dijkstra_algoritimo(GrafoNDPonderado grafo, int origem){
        if(origem >= grafo.getN()) return;
        origem -= 1;

        Float[] d = new Float[grafo.getN()];
        for (int i = 0; i < grafo.getN(); i++) {
            if (i == origem) {
                d[i] = 0f;
            } else {
                d[i] = Float.POSITIVE_INFINITY;
            }
        }

        List<Integer> aberto = new ArrayList<>();
        List<Integer> sucessores;
        Integer[] rot = new Integer[grafo.getN()];

        for (int i = 0; i < grafo.getN(); i++) {
            rot[i] = 0;
            aberto.add(i);
        }


        while (!aberto.isEmpty()) {
            int r = aberto.getFirst();

            for (int i = 0; i < grafo.getN(); i++) {
                if(!aberto.contains(i)) continue;

                if(aberto.size() == 1){
                    r = aberto.getFirst();
                }
                else if (d[i] < d[r]) {
                    r = i;
                }
            }

            aberto.remove(aberto.indexOf(r));
            sucessores = this.rotas_de_v(r,grafo);

            for (int i = 0; i < sucessores.size(); i++) {
                float p = Math.min(d[sucessores.get(i)], (d[r] + grafo.getAdj()[r][sucessores.get(i)]));
                if (p < d[sucessores.get(i)]) {
                    d[sucessores.get(i)] = p;
                    rot[sucessores.get(i)] = r;
                }
            }
        }

        System.out.println("Distância mínima da origem " + (origem+1) +" para: ");
        for(int i=0; i<grafo.getN(); i++){
            if(i == origem) continue;
            System.out.println(i+1 + "-> " + d[i]);
        }


        this.print_rot(rot, grafo, origem);
    }

    public List<Integer> rotas_de_v(int v,GrafoNDPonderado grafo) {
        List<Integer> rotas = new ArrayList<>();
        for (int i = 0; i < grafo.getN(); i++) {
            if (grafo.getAdj()[v][i] != Float.POSITIVE_INFINITY) {
                rotas.add(i);
            }
        }
        return rotas;
    }

    public void print_rot(Integer[] rot,GrafoNDPonderado grafo,int origem){
        for (int i = 0; i < grafo.getN(); i++) {
            if(origem == i) continue;
            int k = i;
            System.out.print("\n\n Rota da " + (origem+1) + " para " + (i+1) + ": " + (origem+1));
            while (rot[rot[k]] != 0) {
                System.out.print(" -> " + (rot[k]+1));
                k = rot[k];
            }
            System.out.print(" -> " + (i+1));
        }
    }
}
