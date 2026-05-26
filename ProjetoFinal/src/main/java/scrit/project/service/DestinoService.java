package scrit.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrit.project.domain.Destino;
import scrit.project.domain.GrafoDestino;
import scrit.project.domain.Lugar;
import scrit.project.dto.DestinoRequest;
import scrit.project.dto.DestinoResponse;
import scrit.project.repository.DestinoRepository;
import scrit.project.repository.GrafoFileRepository;
import scrit.project.repository.LugarRepository;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DestinoService {

    private final DestinoRepository   destinoRepository;
    private final LugarRepository     lugarRepository;
    private final GrafoFileRepository grafoFileRepository;
    private final OsmService          osmService;
    private final GooglePlacesService googlePlacesService;
    private final GrafoConstrutor     grafoConstrutor;
    private final GrafoService        grafoService;

    @Transactional
    public DestinoResponse cadastrar(DestinoRequest request) {
        if (destinoRepository.existsByNomeIgnoreCase(request.getNome())) {
            throw new IllegalArgumentException("Destino já cadastrado: " + request.getNome());
        }
        Destino destino = Destino.builder()
                .nome(request.getNome())
                .status(Destino.StatusDestino.PENDENTE)
                .build();
        destino = destinoRepository.save(destino);
        log.info("Destino '{}' cadastrado id={}. Iniciando processamento...",
                destino.getNome(), destino.getId());
        processarDestino(destino.getId());
        return DestinoResponse.from(destino);
    }

    @Transactional
    public void deletar(Integer id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Destino não encontrado: id=" + id));
        destinoRepository.delete(destino);
        log.info("Destino id={} deletado do banco.", id);

        try {
            GrafoDestino grafo = grafoFileRepository.carregar();
            lugarRepository.deleteByDestinoId(id, grafo);
            grafoService.setGrafo(grafo); // sincroniza memória após remoção
        } catch (IOException e) {
            log.error("Erro ao remover lugares do grafo.txt para destino id={}: {}", id, e.getMessage());
        }
    }

    public List<DestinoResponse> listarTodos() {
        return destinoRepository.findAll().stream().map(DestinoResponse::from).toList();
    }

    public DestinoResponse buscarPorId(Integer id) {
        return destinoRepository.findById(id)
                .map(DestinoResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Destino não encontrado: id=" + id));
    }

    @Async
    public void processarDestino(Integer destinoId) {
        Destino destino = destinoRepository.findById(destinoId).orElse(null);
        if (destino == null) return;

        try {
            destino.setStatus(Destino.StatusDestino.PROCESSANDO);
            destinoRepository.save(destino);

            osmService.geocodificar(destino, destino.getNome());
            destinoRepository.save(destino);

            List<Lugar> lugares = osmService.buscarLugares(destino);
            if (lugares.isEmpty()) {
                log.warn("Nenhum lugar encontrado para '{}'.", destino.getNome());
                destino.setStatus(Destino.StatusDestino.CONCLUIDO);
                destinoRepository.save(destino);
                return;
            }

            googlePlacesService.enriquecerLugares(lugares);

            // 1. Carrega o grafo atual do arquivo (ou vazio se for o primeiro destino)
            GrafoDestino grafo = grafoFileRepository.carregar();

            // 2. Insere os vértices no grafo E persiste no arquivo
            List<Lugar> salvos = lugarRepository.saveAll(lugares, grafo);
            log.info("{} lugares salvos no grafo.txt para '{}'.", salvos.size(), destino.getNome());

            // 3. Cria as arestas usando o MESMO grafo já atualizado (com os vértices)
            grafoConstrutor.construirEAtualizar(destino, grafo, salvos);

            destino.setStatus(Destino.StatusDestino.CONCLUIDO);
            destinoRepository.save(destino);
            log.info("Processamento de '{}' concluído.", destino.getNome());

        } catch (Exception e) {
            log.error("Erro ao processar destino id={}: {}", destinoId, e.getMessage(), e);
            destino.setStatus(Destino.StatusDestino.ERRO);
            destino.setMensagemErro(e.getMessage());
            destinoRepository.save(destino);
        }
    }
}
