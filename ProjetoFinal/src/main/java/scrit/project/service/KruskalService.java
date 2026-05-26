package scrit.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import scrit.project.domain.GrafoDestino;
import scrit.project.domain.Lugar;
import scrit.project.domain.Tno;
import scrit.project.utils.UnionFind;

import java.util.*;


@Slf4j
@Service
public class KruskalService {

    public record Aresta(int v, int w, float dist, float tempo)
            implements Comparable<Aresta> {
        @Override
        public int compareTo(Aresta o) {
            return Float.compare(this.dist, o.dist);
        }
    }

    public record SubgrafoMST(
            List<Integer> vertices,          // índices base-0 dos vértices incluídos
            List<Aresta>  arestas,           // arestas da MST (dist mínima)
            float         distanciaTotal,    // soma das distâncias da MST (km)
            Map<Integer, List<Aresta>> adj   // lista de adjacência da MST
    ) {}

    
    public SubgrafoMST gerarMST(GrafoDestino grafo, List<Lugar> lugares) {
        if (lugares.size() < 2) {
            log.warn("Kruskal: menos de 2 vértices, MST trivial.");
            return new SubgrafoMST(List.of(), List.of(), 0f, Map.of());
        }
        Map<Integer, Integer> idParaIdx = new HashMap<>();
        for (Map.Entry<Integer, Lugar> entry : grafo.getLugares().entrySet()) {
            idParaIdx.put(entry.getKey(), entry.getKey() - 1);
        }
        Set<Integer> idxSet = new HashSet<>();
        List<Integer> idxList = new ArrayList<>();
        for (Lugar l : lugares) {
            Integer idx = idParaIdx.get(l.getId());
            if (idx != null && idxSet.add(idx)) {
                idxList.add(idx);
            }
        }
        List<Aresta> candidatas = new ArrayList<>();
        for (int v : idxList) {
            if (v >= grafo.getN()) continue;
            Tno no = grafo.getAdj()[v];
            while (no != null) {
                int w = no.getLugar();
                if (idxSet.contains(w) && v < w) { // evita duplicata
                    candidatas.add(new Aresta(v, w, no.getDist(), no.getTempoDeslocamento()));
                }
                no = no.getProximo();
            }
        }
        if (candidatas.isEmpty()) {
            log.warn("Kruskal: nenhuma aresta entre os vértices selecionados. " +
                     "Criando arestas artificiais para garantir conectividade.");
            for (int i = 0; i < idxList.size(); i++) {
                for (int j = i + 1; j < idxList.size(); j++) {
                    candidatas.add(new Aresta(idxList.get(i), idxList.get(j), 1.0f, 3.0f));
                }
            }
        }
        Collections.sort(candidatas);
        Map<Integer, Integer> idxParaLocal = new HashMap<>();
        for (int i = 0; i < idxList.size(); i++) {
            idxParaLocal.put(idxList.get(i), i);
        }
        UnionFind uf = new UnionFind(idxList.size());
        List<Aresta> mstArestas = new ArrayList<>();
        float distTotal = 0f;

        for (Aresta a : candidatas) {
            int localV = idxParaLocal.get(a.v());
            int localW = idxParaLocal.get(a.w());
            if (uf.union(localV, localW)) {
                mstArestas.add(a);
                distTotal += a.dist();
                if (mstArestas.size() == idxList.size() - 1) break; // MST completa
            }
        }
        Map<Integer, List<Aresta>> adj = new HashMap<>();
        for (int v : idxList) adj.put(v, new ArrayList<>());
        for (Aresta a : mstArestas) {
            adj.get(a.v()).add(a);
            adj.get(a.w()).add(new Aresta(a.w(), a.v(), a.dist(), a.tempo())); // bidirecional
        }

        log.info("Kruskal: MST com {} vértices, {} arestas, distância total = {} km",
                idxList.size(), mstArestas.size(), String.format("%.2f", distTotal));

        return new SubgrafoMST(idxList, mstArestas, distTotal, adj);
    }
}
