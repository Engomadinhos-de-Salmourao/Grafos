package Lista_Implementação.Exercicios_ListaAdjacencia.Builder;

import Lista_Implementação.Exercicios_ListaAdjacencia.Grafo.TGrafoLista;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class TGrafoListaBuilder {
    public static TGrafoLista archiveReader(String archive) throws IOException {
        Path caminho = Path.of(archive);
        List<String> linhas = Files.readAllLines(caminho);

        TGrafoLista gm = null;

        for (String linha : linhas) {
            if(Objects.equals(linha, linhas.getFirst())){
                gm  = new TGrafoLista(Integer.parseInt(linha));
            }
            else{
                if(Objects.equals(linha, linhas.get(1))){
                    continue;
                }
                String[] result = linha.split(" ");
                int v = Integer.parseInt(result[0]);
                int w = Integer.parseInt(result[1]);
                if (gm != null) {
                    gm.insereA(v,w);
                }
            }
        }
        return gm;
    }
}
