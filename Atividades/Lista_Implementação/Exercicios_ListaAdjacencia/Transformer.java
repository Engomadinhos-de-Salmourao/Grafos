package Lista_Implementação.Exercicios_ListaAdjacencia;

import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TGrafoLista;
import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TGrafoListaND;
import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TNo;
import Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Grafo.TGrafoMatriz;
import Lista_Implementação.Exercicios_Matriz.Grafo_ND.Grafo.TGrafoNdMatriz;

public class Transformer {
    public TGrafoLista transformGrafoMatrizToLista(TGrafoMatriz gm) {
        TGrafoLista grafo = new TGrafoLista(gm.getN());

        for (int i = 0; i < gm.getN(); i++) {
            for (int j = 0; j < gm.getN(); j++) {
                if (gm.getAdj()[i][j] == 1) {
                    grafo.insereA(i, j);
                }
            }
        }

        return grafo;
    }

    public TGrafoMatriz transformGrafoListaToMatriz(TGrafoLista gm) {
        TGrafoMatriz grafo = new TGrafoMatriz(gm.getN());

        for (int i = 0; i < gm.getN(); i++) {
            TNo aux = gm.getAdj()[i];
            while(aux != null){
                int j = aux.w;
                grafo.insertA(i, j);
                aux = aux.prox;
            }
        }

        return grafo;
    }


    public TGrafoListaND transformGrafoMatrizToLista(TGrafoNdMatriz gm) {
        TGrafoListaND grafo = new TGrafoListaND(gm.getN());

        for (int i = 0; i < gm.getN(); i++) {
            for (int j = 0; j < gm.getN(); j++) {
                if (gm.getAdj()[i][j] == 1) {
                    grafo.insereA(i, j);
                }
            }
        }

        return grafo;
    }

    public TGrafoNdMatriz transformGrafoListaToMatriz(TGrafoListaND gm) {
        TGrafoNdMatriz grafo = new TGrafoNdMatriz(gm.getN());

        for (int i = 0; i < gm.getN(); i++) {
            TNo aux = gm.getAdj()[i];
            while(aux != null){
                int j = aux.w;
                grafo.insertA(i, j);
                aux = aux.prox;
            }
        }

        return grafo;
    }
}
