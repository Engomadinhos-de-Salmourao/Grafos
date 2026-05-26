package scrit.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import scrit.project.domain.Destino;
import scrit.project.domain.GrafoDestino;
import scrit.project.domain.Lugar;
import scrit.project.repository.GrafoFileRepository;
import scrit.project.utils.HaversineUtil;

import java.io.IOException;
import java.util.List;

/**
 * Recebe o grafo JÁ ATUALIZADO (com os vértices inseridos) e apenas cria
 * as arestas Haversine entre os novos lugares, depois persiste no arquivo.
 *
 * NÃO chama grafoService.obterGrafo() — o grafo atualizado é passado
 * diretamente pelo chamador para evitar race condition.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GrafoConstrutor {

    private final GrafoFileRepository grafoFileRepository;
    private final GrafoService        grafoService;

    /**
     * @param grafo  Instância já atualizada com os novos vértices inseridos.
     * @param novos  Lista de lugares recém-adicionados (com IDs já atribuídos).
     */
    public void construirEAtualizar(Destino destino, GrafoDestino grafo, List<Lugar> novos)
            throws IOException {

        log.info("GrafoConstrutor: criando arestas para {} lugares do destino '{}'",
                novos.size(), destino.getNome());

        // Os novos vértices estão nos últimos novos.size() slots do adj[].
        int baseIdx = grafo.getN() - novos.size();

        int adicionados = 0;
        for (int i = 0; i < novos.size(); i++) {
            for (int j = i + 1; j < novos.size(); j++) {
                Lugar a = novos.get(i);
                Lugar b = novos.get(j);

                if (a.getLatitude() == null || b.getLatitude() == null) continue;

                double distKm = HaversineUtil.distanciaKm(
                        a.getLatitude(), a.getLongitude(),
                        b.getLatitude(), b.getLongitude());
                float tempo = HaversineUtil.tempoDeslocamentoMinutos(distKm);

                int idxA = baseIdx + i;
                int idxB = baseIdx + j;

                if (idxA >= 0 && idxB >= 0 && idxA < grafo.getN() && idxB < grafo.getN()) {
                    grafo.insereA(idxA, idxB, (float) distKm, tempo);
                    adicionados++;
                }
            }
        }

        log.info("GrafoConstrutor: {} arestas criadas para '{}'", adicionados, destino.getNome());

        // Atualiza o grafo em memória no GrafoService e persiste no arquivo
        grafoService.setGrafo(grafo);
        grafoFileRepository.gravar(grafo);

        log.info("GrafoConstrutor: grafo.txt atualizado. Total: {} vértices, {} arestas.",
                grafo.getN(), grafo.getM() / 2);
    }

    public List<Integer> indicesDoDestino(GrafoDestino grafo, Integer destinoId) {
        return grafo.getLugares().entrySet().stream()
                .filter(e -> e.getValue().getDestino() != null
                          && destinoId.equals(e.getValue().getDestino().getId()))
                .map(e -> e.getKey() - 1)
                .sorted()
                .toList();
    }
}
