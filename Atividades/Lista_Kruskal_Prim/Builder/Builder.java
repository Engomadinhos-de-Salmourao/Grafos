package Lista_Kruskal_Prim.Builder;


import Lista_Kruskal_Prim.Grafo.GrafoNDPonderado;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Builder {
    public static GrafoNDPonderado archiveReader(String archive) throws IOException {
        Path caminho = Path.of(archive);
        List<String> linhas = Files.readAllLines(caminho);

        GrafoNDPonderado gm = null;

        for (String linha : linhas) {
            if(Objects.equals(linha, linhas.getFirst())){
                gm  = new GrafoNDPonderado(Integer.parseInt(linha));
            }
            else{
                if(Objects.equals(linha, linhas.get(1))){
                   continue;
                }
                String[] result = linha.split(" ");
                int v = Integer.parseInt(result[0]);
                int w = Integer.parseInt(result[1]);
                float weight = Float.parseFloat(result[2]);
                if (gm != null) {
                    gm.insertA(v-1,w-1, weight);
                }

            }
        }
        return gm;
    }
}
