package Lista_Implementação;

import Lista_Implementação.Exercicios_ListaAdjacencia.Builder.TGrafoListaBuilder;
import Lista_Implementação.Exercicios_ListaAdjacencia.Builder.TGrafoListaNDBuilder;
import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TGrafoLista;
import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TGrafoListaND;
import Lista_Implementação.Exercicios_ListaAdjacencia.Transformer;
import Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Builder.TGrafoMatrizBuilder;
import Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Grafo.Grafo;
import Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Grafo.TGrafoMatriz;
import Lista_Implementação.Exercicios_Matriz.Grafo_ND.Builder.TGrafoNdMatrizBuilder;
import Lista_Implementação.Exercicios_Matriz.Grafo_ND.Grafo.TGrafoNdMatriz;

import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        String arquivo = "./Matriz.txt";
        String arquivo1 = "./ND.txt";
        String arquivo2 = "./MatrizPonderado.txt";
        String arquivo3 = "./ListaDirecionada.txt";

        TGrafoListaND g_lista_nd = TGrafoListaNDBuilder.archiveReader(arquivo1);
        TGrafoLista g_lista = TGrafoListaBuilder.archiveReader(arquivo3);
        Grafo g = TGrafoMatrizBuilder.archiveReader(arquivo);
        Grafo g_ponderado = TGrafoMatrizBuilder.archiveReader(arquivo2);
        TGrafoNdMatriz g_nd = TGrafoNdMatrizBuilder.archiveReader(arquivo1);

        //Exercício 1
        System.out.println("Exercício 1- inDegree");
        System.out.println("Grau de entrada do vértice 0: " + g.inDegree(0));

        //Exercício 2
        System.out.println("\n\n\nExercício 2- outDegree");
        System.out.println("Grau de saída do vértice 0: " + g.outDegree(0));

        //Exercício 3
        System.out.println("\n\n\nExercício 3- Degree");
        System.out.println("Grau do vértice 0: " + g.degree(0));

        //Exercício 4
        System.out.println("\n\n\nExercício 4- Fonte");
        System.out.println("Vértice 0 é fonte? " + g.nodeSource(0));

        //Exercício 5
        System.out.println("\n\n\nExercício 5- Sorvedouro");
        System.out.println("Vértice 0 é sorvedouro? " + g.nodeReceiver(0));

        //Exercício 6
        System.out.println("\n\n\nExercício 6- Simétrico");
        System.out.println("Grafo é simétrico? " + g.symmetric());


       //Exercício 7
        System.out.println("\n\n\nExercício 7- Ler Arquivo");
        g.show();

        //Exercício 8
        System.out.println("\n\n\nExercício 8- Grafo Não Direcionado");
        g_nd.show();
        System.out.println("Remover aresta entre 0 e 2");
        g_nd.removeA(0, 2);
        g_nd.show();

        //Exercício 9
        System.out.println("\n\n\nExercício 9- Degree Não Direcionado");
        System.out.println("Grau do vértice 0: " + g_nd.degree(0));

        //Exercício 10
        System.out.println("\n\n\nExercício 10- Grafo Ponderado Direcionado");
        g_ponderado.show();

        //Exercício 11
        System.out.println("\n\n\nExercício 11- Remover Vértice");
        System.out.println("Remover vértice 0 do grafo direcionado");
        if(g instanceof TGrafoMatriz aux){
            aux.removeV(0);
        }
        g.show();
        System.out.println("\n\nRemover vértice 0 do grafo não direcionado");
        g_nd.removeV(0);
        g_nd.show();

        //Recuperar Estado Original dos grafos
        g = TGrafoMatrizBuilder.archiveReader(arquivo);
        g_nd = TGrafoNdMatrizBuilder.archiveReader(arquivo1);

        //Exercício 12
        System.out.println("\n\n\nExercício 12- Grafo Não Dirigido Completo");
        System.out.println("O Grafo Não Dirigido é completo? " + g_nd.isComplete());

        //Exercício 13
        System.out.println("\n\n\nExercício 13- Grafo Dirigido Completo");
        System.out.println("O Grafo Dirigido é completo? " + g.isComplete());

        //Exercício 14
        System.out.println("\n\n\nExercício 14- Complemento do Grafo");
        System.out.println("Complemento do Grafo Dirigido");
        if(g instanceof TGrafoMatriz aux){
            aux.complement().show();
        }
        System.out.println("\n\nComplemento do Grafo Não Dirigido");
        g_nd.complement().show();

        //Exercício 15
        System.out.println("\n\n\nExercício 15- Conexidade Grafo Não Dirigido");
        System.out.println("Conexidade:  " + g_nd.conexidade());

        //Exercício 16
        System.out.println("\n\n\nExercício 16- Conexidade Grafo Dirigido");
        if(g instanceof  TGrafoMatriz aux){
            System.out.println("Conexidade:  C" + aux.conexidade());
        }

        //Exercício 17
        System.out.println("\n\n\nExercício 17- Grafo Reduzido");
        if(g instanceof  TGrafoMatriz aux){
            aux.grafoReduzido().show();
        }

        //Exercício 18
        System.out.println("\n\n\nExercício 18- inDegree Lista");
        System.out.println("Grau de entrada do vértice 0: " + g_lista.degree(0));

        //Exercício 19
        System.out.println("\n\n\nExercício 19- outDegree Lista");
        System.out.println("Grau de saída do vértice 0: " + g_lista.outDegree(0));

        //Exercício 20
        System.out.println("\n\n\nExercício 20- Degree Lista");
        System.out.println("Grau do vértice 0: " + g_lista.degree(0));

        //Exercício 21
        System.out.println("\n\n\nExercício 21- Grafo igual");
        System.out.println("Grafos são iguais: " + g_lista.equal(g_lista));

        //Exercício 22
        System.out.println("\n\n\nExercício 22- Grafo Transformado");
        Transformer t = new Transformer();
        System.out.println("\nGrafo em Lista");
        g_lista.show();
        System.out.println("\nTransformado em Matriz");
        t.transformGrafoListaToMatriz(g_lista).show();

        //Exercício 23
        System.out.println("\n\n\nExercício 23- Inverter Lista");
        System.out.println("\nGrafo em Lista não invertido");
        g_lista.show();
        System.out.println("\nGrafo em Lista invertido");
        g_lista.inverteListas();
        g_lista.show();

        //Recuperar Estado Original do Grafo
        g_lista = TGrafoListaBuilder.archiveReader(arquivo3);

        //Exercício 24
        System.out.println("\n\n\nExercício 24- Fonte Lista");
        System.out.println("Vértice 0 é fonte? " + g_lista.fonte(0));

        //Exercício 25
        System.out.println("\n\n\nExercício 25- Sorvedouro Lista");
        System.out.println("Vértice 0 é sorvedouro? " + g_lista.sorvedouro(0));

        //Exercício 26
        System.out.println("\n\n\nExercício 26- Simétrico Lista");
        System.out.println("Grafo é simétrico? " + g_lista.simetrico());


        //Exercício 27
        System.out.println("\n\n\nExercício 27- Ler Arquivo Lista");
        g_lista.show();
        System.out.println("\n\n");
        g_lista_nd.show();

        //Exercício 28
        System.out.println("\n\n\nExercício 28- Remover Vértice Lista Não Direcionada");
        System.out.println("\nGrafo em Lista Antes da Remoção");
        g_lista_nd.show();
        System.out.println("\nGrafo em Lista Depois da Remoção do Vértice 0");
        g_lista_nd.removeV(0);
        g_lista_nd.show();

        //Exercício 29
        System.out.println("\n\n\nExercício 29- Remover Vértice Lista Direcionada");
        System.out.println("\nGrafo em Lista Antes da Remoção");
        g_lista.show();
        System.out.println("\nGrafo em Lista Depois da Remoção do Vértice 0");
        g_lista.removeV(0);
        g_lista.show();

        //Recuperar Estado Original
        g_lista_nd = TGrafoListaNDBuilder.archiveReader(arquivo1);
        g_lista = TGrafoListaBuilder.archiveReader(arquivo3);

        //Exercício 30
        System.out.println("\n\n\nExercício 30- Grafo Completo Lista");
        System.out.println("O Grafo em Lista Não Dirigido é completo? " + g_lista_nd.isComplete());
        System.out.println("O Grafo em Lista Dirigido é completo? " + g_lista.isComplete());

    }
}
