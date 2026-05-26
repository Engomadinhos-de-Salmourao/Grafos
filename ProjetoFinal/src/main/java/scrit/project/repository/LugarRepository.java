package scrit.project.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import scrit.project.domain.GrafoDestino;
import scrit.project.domain.Lugar;
import scrit.project.domain.TypeLugar;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Repositório de Lugar — delega toda a persistência ao GrafoFileRepository.
 * Lugar não é uma entidade JPA; seus dados vivem exclusivamente no grafo.txt.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LugarRepository {

    private final GrafoFileRepository grafoFileRepository;

    /** Retorna todos os lugares do grafo carregado do arquivo. */
    public List<Lugar> findAll() throws IOException {
        GrafoDestino grafo = grafoFileRepository.carregar();
        return List.copyOf(grafo.getLugares().values());
    }

    /** Filtra lugares pelo id do Destino associado. */
    public List<Lugar> findByDestinoId(Integer destinoId) throws IOException {
        return findAll().stream()
                .filter(l -> l.getDestino() != null
                          && destinoId.equals(l.getDestino().getId()))
                .toList();
    }

    /** Filtra lugares pelo id do Destino e pelo tipo. */
    public List<Lugar> findByDestinoIdAndTipo(Integer destinoId, TypeLugar tipo) throws IOException {
        return findByDestinoId(destinoId).stream()
                .filter(l -> tipo == l.getTipo())
                .toList();
    }

    /** Busca um lugar pelo id (base-1). */
    public Optional<Lugar> findById(Integer id) throws IOException {
        GrafoDestino grafo = grafoFileRepository.carregar();
        return Optional.ofNullable(grafo.getLugares().get(id));
    }

    /**
     * Salva (persiste) uma lista de novos lugares no grafo em memória e grava o arquivo.
     * Os lugares recebem IDs sequenciais a partir do tamanho atual do grafo.
     */
    public List<Lugar> saveAll(List<Lugar> lugares, GrafoDestino grafo) throws IOException {
        int baseId = grafo.getN() + 1; // IDs base-1
        for (int i = 0; i < lugares.size(); i++) {
            Lugar l = lugares.get(i);
            l.setId(baseId + i);
            grafo.insereV(l);
        }
        grafoFileRepository.gravar(grafo);
        return lugares;
    }

    /** Remove todos os lugares de um destino do grafo e regrava o arquivo. */
    public void deleteByDestinoId(Integer destinoId, GrafoDestino grafo) throws IOException {
        List<Integer> idsParaRemover = grafo.getLugares().entrySet().stream()
                .filter(e -> e.getValue().getDestino() != null
                          && destinoId.equals(e.getValue().getDestino().getId()))
                .map(e -> e.getValue().getId())
                .sorted((a, b) -> b - a) // remove do maior ID para o menor (evita reindexação incorreta)
                .toList();

        for (Integer id : idsParaRemover) {
            grafo.removeV(id);
        }
        grafoFileRepository.gravar(grafo);
        log.info("Removidos {} lugares do destino id={} do grafo.", idsParaRemover.size(), destinoId);
    }
}
