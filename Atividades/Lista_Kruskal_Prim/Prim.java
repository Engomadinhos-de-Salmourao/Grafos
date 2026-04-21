package Lista_Kruskal_Prim;

import Lista_Kruskal_Prim.Grafo.GrafoNDPonderado;

public class Prim {
    public static GrafoNDPonderado prim(GrafoNDPonderado g) {
        int n = g.getN();

        GrafoNDPonderado arvore = new GrafoNDPonderado(n);
        boolean[] T = new boolean[n];
        float[] custo = {0};

        T[0] = true;

        prim(g, T, arvore, custo);

        return arvore;
    }

    private static void prim(GrafoNDPonderado g, boolean[] T, GrafoNDPonderado arvore, float[] custo) {
        int n = g.getN();
        Float[][] adj = g.getAdj();

        float valor = Float.POSITIVE_INFINITY;
        int vint = -1;
        int vext = -1;

        for (int k = 0; k < n; k++) {
            if (T[k]) {
                for (int i = 0; i < n; i++) {
                    if (!T[i] && !adj[k][i].equals(Float.POSITIVE_INFINITY)) {
                        if (adj[k][i] < valor) {
                            valor = adj[k][i];
                            vint = k;
                            vext = i;
                        }
                    }
                }
            }
        }

        if (vext == -1) {
            return;
        }

        custo[0] = custo[0] + valor;
        T[vext] = true;
        arvore.insertA(vext, vint, valor);

        boolean todosEmT = true;
        for (int i = 0; i < n; i++) {
            if (!T[i]) {
                todosEmT = false;
                break;
            }
        }

        if (!todosEmT) {
            prim(g, T, arvore, custo);
        }
    }
}
