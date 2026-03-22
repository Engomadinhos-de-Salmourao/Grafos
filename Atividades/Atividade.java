import Exercicios_Matriz.Grafo_Direcionado.Grafo;
import Exercicios_Matriz.Grafo_Direcionado.TGrafoMatriz;
import Exercicios_Matriz.Grafo_Direcionado.TGrafoMatrizBuilder;

import java.io.IOException;

public class Atividade {
    public static void main(String[] args) throws IOException {
        String arquivo = "Atividades/Exercicios_Matriz/Grafo_Direcionado/text.txt";
        String arquivoPonderado = "Atividades/Exercicios_Matriz/Grafo_Direcionado/textPonderado.txt";
        Grafo g = TGrafoMatrizBuilder.archiveReader(arquivo);

        g.show();
        if(g instanceof TGrafoMatriz aux){
            System.out.println("C" + aux.conexidade());
        }

    }
}
