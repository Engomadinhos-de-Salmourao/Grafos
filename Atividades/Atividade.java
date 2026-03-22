import Exercicios_Matriz.Grafo_Direcionado.Grafo.Grafo;
import Exercicios_Matriz.Grafo_Direcionado.Grafo.TGrafoMatriz;
import Exercicios_Matriz.Grafo_Direcionado.Builder.TGrafoMatrizBuilder;

import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        String arquivo = "Atividades/Exercicios_Matriz/Grafo_Direcionado/text.txt";
        Grafo g = TGrafoMatrizBuilder.archiveReader(arquivo);


        if(g instanceof TGrafoMatriz aux){
            TGrafoMatriz g2 = aux.grafoReduzido();
            g2.show();
        }


    }
}
