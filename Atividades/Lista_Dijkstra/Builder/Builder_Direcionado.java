package Lista_Dijkstra.Builder;

import Lista_Dijkstra.Grafo.GrafoNDPonderado;
import Lista_Dijkstra.Grafo.GrafoPonderado;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Builder_Direcionado {
    public static GrafoPonderado archiveReader(String archive) throws IOException {
        Path caminho = Path.of(archive);
        List<String> linhas = Files.readAllLines(caminho);

        GrafoPonderado gm = null;

        for (String linha : linhas) {
            if(Objects.equals(linha, linhas.getFirst())){
                gm  = new GrafoPonderado(Integer.parseInt(linha));
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
