package Exercicios_Matriz.Grafo_ND;

import Exercicios_Matriz.Grafo_Direcionado.GraphType;
import Lista_Algoritimos.TGrafoND;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class TGrafoNdMatrizBuilder {
    public static TGrafoNdMatriz archiveReader(String archive) throws IOException {
        Path caminho = Path.of(archive);
        List<String> linhas = Files.readAllLines(caminho);

        TGrafoNdMatriz gm = null;

        for (String linha : linhas) {
            if(Objects.equals(linha, linhas.getFirst())){
                gm  = new TGrafoNdMatriz(Integer.parseInt(linha));
            }
            else{
                if(Objects.equals(linha, linhas.get(1))){
                    continue;
                }
                String[] result = linha.split(" ");
                int v = Integer.parseInt(result[0]);
                int w = Integer.parseInt(result[1]);
                if (gm != null) {
                    gm.insertA(v,w);
                }

            }
        }
        return gm;
    }
}
