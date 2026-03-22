package Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Builder;

import Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Grafo.Grafo;
import Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Grafo.TGrafoMatriz;
import Lista_Implementação.Exercicios_Matriz.Grafo_Direcionado.Grafo.TGrafoPonderadoMatriz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class TGrafoMatrizBuilder {
    public static Grafo archiveReader(String archive) throws IOException {
        Path caminho = Path.of(archive);
        List<String> linhas = Files.readAllLines(caminho);

        Grafo gm = null;
        GraphType type = null;

        for (String linha : linhas) {
            if(Objects.equals(linha, linhas.getFirst())){
                if(linha.equalsIgnoreCase("ponderado")){
                    type = GraphType.PONDERADO;
                }
                else if(linha.equalsIgnoreCase("não ponderado")){type = GraphType.NAO_PONDERADO;}
            }
            else if(Objects.equals(linha, linhas.get(1))){
                if (type != null) {
                    if(type == GraphType.NAO_PONDERADO){
                        gm  = new TGrafoMatriz(Integer.parseInt(linha));
                    }
                    else {
                        gm  = new TGrafoPonderadoMatriz(Integer.parseInt(linha));
                    }

                }
            }
            else{
                if(Objects.equals(linha, linhas.get(2))){
                    continue;
                }
                String[] result = linha.split(" ");
                int v = Integer.parseInt(result[0]);
                int w = Integer.parseInt(result[1]);
                if (type != null && type.equals(GraphType.NAO_PONDERADO)){
                    if (gm instanceof TGrafoMatriz aux){
                        aux.insertA(v,w);
                    }
                }
                else{
                    float weight = Float.parseFloat(result[2]);
                    if (gm instanceof TGrafoPonderadoMatriz aux){
                        aux.insertA(v,w, weight);
                    }
                }
            }
        }
        return gm;
    }
}
