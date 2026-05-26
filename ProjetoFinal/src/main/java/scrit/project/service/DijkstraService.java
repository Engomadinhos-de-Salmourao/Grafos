package scrit.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import scrit.project.service.KruskalService.Aresta;
import scrit.project.service.KruskalService.SubgrafoMST;

import java.util.*;


@Slf4j
@Service
public class DijkstraService {

    public record MatrizCaminhos(
            List<Integer>  vertices,      // índices base-0 (ordem dos vértices)
            float[][]      distancias,    // distancias[i][j] = km entre vertices[i] e vertices[j]
            float[][]      tempos,        // tempos[i][j] = minutos entre vertices[i] e vertices[j]
            int[][]        proximo        // próximo vértice no caminho mínimo de i→j (base-0 local)
    ) {
        
        public float dist(int localI, int localJ) {
            return distancias[localI][localJ];
        }

        
        public float tempo(int localI, int localJ) {
            return tempos[localI][localJ];
        }
    }

    private static final float INF = Float.MAX_VALUE / 2;

    
    public MatrizCaminhos calcular(SubgrafoMST mst) {
        List<Integer> vertices = mst.vertices();
        int k = vertices.size();

        if (k == 0) {
            return new MatrizCaminhos(List.of(), new float[0][0], new float[0][0], new int[0][0]);
        }
        Map<Integer, Integer> globalParaLocal = new HashMap<>();
        for (int i = 0; i < k; i++) {
            globalParaLocal.put(vertices.get(i), i);
        }

        float[][] distancias = new float[k][k];
        float[][] tempos     = new float[k][k];
        int[][]   proximo    = new int[k][k];
        for (int i = 0; i < k; i++) {
            Arrays.fill(distancias[i], INF);
            Arrays.fill(tempos[i], INF);
            Arrays.fill(proximo[i], -1);
            distancias[i][i] = 0;
            tempos[i][i]     = 0;
        }
        for (int src = 0; src < k; src++) {
            dijkstra(src, vertices, mst.adj(), globalParaLocal, distancias[src], tempos[src], proximo[src]);
        }

        log.info("Dijkstra: matriz {}×{} calculada para {} vértices.", k, k, k);
        return new MatrizCaminhos(vertices, distancias, tempos, proximo);
    }

    private void dijkstra(int srcLocal,
                          List<Integer> vertices,
                          Map<Integer, List<Aresta>> adj,
                          Map<Integer, Integer> globalParaLocal,
                          float[] dist, float[] tempo, int[] prox) {
        PriorityQueue<float[]> heap = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        heap.add(new float[]{0f, srcLocal});
        dist[srcLocal]  = 0f;
        tempo[srcLocal] = 0f;

        while (!heap.isEmpty()) {
            float[] curr = heap.poll();
            float dAtual = curr[0];
            int    local = (int) curr[1];

            if (dAtual > dist[local]) continue; // entrada obsoleta
            int globalV = vertices.get(local);
            List<Aresta> vizinhos = adj.getOrDefault(globalV, List.of());

            for (Aresta aresta : vizinhos) {
                Integer localW = globalParaLocal.get(aresta.w());
                if (localW == null) continue;

                float novaDist  = dist[local] + aresta.dist();
                float novoTempo = tempo[local] + aresta.tempo();

                if (novaDist < dist[localW]) {
                    dist[localW]  = novaDist;
                    tempo[localW] = novoTempo;
                    prox[localW]  = local;
                    heap.add(new float[]{novaDist, localW});
                }
            }
        }
    }

    
    public List<Integer> reconstruirCaminho(MatrizCaminhos matriz, int srcLocal, int dstLocal) {
        List<Integer> caminho = new ArrayList<>();
        int atual = dstLocal;
        Set<Integer> visitados = new HashSet<>();

        while (atual != srcLocal && atual != -1) {
            if (!visitados.add(atual)) break; // ciclo (não deve ocorrer em MST)
            caminho.add(0, atual);
            atual = matriz.proximo()[srcLocal][atual];
        }
        caminho.add(0, srcLocal);
        return caminho;
    }
}
