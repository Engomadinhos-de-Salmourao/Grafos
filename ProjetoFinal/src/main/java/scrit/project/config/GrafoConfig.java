package scrit.project.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import scrit.project.service.GrafoService;


@Slf4j
@Component
@RequiredArgsConstructor
public class GrafoConfig implements ApplicationRunner {

    private final GrafoService grafoService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            var grafo = grafoService.carregarGrafo();
            log.info("=== Grafo carregado na inicialização: {} vértices, {} arestas ===",
                    grafo.getN(), grafo.getM() / 2);
        } catch (Exception e) {
            log.warn("Não foi possível carregar o grafo.txt na inicialização: {}. " +
                    "Use POST /api/grafo/carregar para tentar novamente.", e.getMessage());
        }
    }
}
