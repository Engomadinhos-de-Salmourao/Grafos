package scrit.project.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;
import scrit.project.domain.Destino;
import scrit.project.domain.GrafoDestino;
import scrit.project.domain.Lugar;
import scrit.project.domain.TypeLugar;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GrafoFileRepositoryTest {

    private GrafoFileRepository repository;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        repository = new GrafoFileRepository();
        String caminhoTemp = tempDir.resolve("grafo_test.txt").toString();
        ReflectionTestUtils.setField(repository, "caminhoArquivo", caminhoTemp);
    }

    @Test
    void grafoVazioQuandoArquivoNaoExiste() throws IOException {
        GrafoDestino grafo = repository.carregar();
        assertEquals(0, grafo.getN());
    }

    @Test
    void gravaERelêGrafoSimples() throws IOException {
        GrafoDestino grafo = new GrafoDestino(2);

        Destino destino = new Destino();
        destino.setId(1);

        Lugar l1 = criarLugar(1, destino, "Hotel Teste", TypeLugar.HOTEL);
        Lugar l2 = criarLugar(2, destino, "Parque Teste", TypeLugar.PONTO_TURISTICO);

        grafo.getLugares().put(1, l1);
        grafo.getLugares().put(2, l2);
        grafo.insereA(0, 1, 3.5f, 12.0f);
        repository.gravar(grafo);
        GrafoDestino lido = repository.carregar();

        assertEquals(2, lido.getN(), "Deve ter 2 vértices");
        assertEquals(2, lido.getM(), "Deve ter 2 entradas na lista de adj (aresta não-dir.)");
        assertNotNull(lido.getLugares().get(1));
        assertNotNull(lido.getLugares().get(2));
        assertEquals("Hotel Teste", lido.getLugares().get(1).getNome());
        assertEquals("Parque Teste", lido.getLugares().get(2).getNome());
        assertNotNull(lido.getAresta(0, 1), "Aresta 0→1 deve existir");
        assertEquals(3.5f, lido.getAresta(0, 1).getDist(), 0.01f);
        assertEquals(12.0f, lido.getAresta(0, 1).getTempoDeslocamento(), 0.01f);
    }

    @Test
    void grafoComTresVerticesSemAresta() throws IOException {
        GrafoDestino grafo = new GrafoDestino(3);

        Destino d = new Destino();
        d.setId(1);

        grafo.getLugares().put(1, criarLugar(1, d, "A", TypeLugar.HOTEL));
        grafo.getLugares().put(2, criarLugar(2, d, "B", TypeLugar.RESTAURANTE));
        grafo.getLugares().put(3, criarLugar(3, d, "C", TypeLugar.PONTO_TURISTICO));

        repository.gravar(grafo);
        GrafoDestino lido = repository.carregar();

        assertEquals(3, lido.getN());
        assertEquals(0, lido.getM());
        assertEquals(0, lido.conexidade()); // 3 vértices isolados = não conexo
    }

    @Test
    void conexidadeGrafoConexo() throws IOException {
        GrafoDestino grafo = new GrafoDestino(3);
        Destino d = new Destino();
        d.setId(1);

        grafo.getLugares().put(1, criarLugar(1, d, "A", TypeLugar.HOTEL));
        grafo.getLugares().put(2, criarLugar(2, d, "B", TypeLugar.RESTAURANTE));
        grafo.getLugares().put(3, criarLugar(3, d, "C", TypeLugar.PONTO_TURISTICO));

        grafo.insereA(0, 1, 1.0f, 5.0f);
        grafo.insereA(1, 2, 2.0f, 8.0f);

        repository.gravar(grafo);
        GrafoDestino lido = repository.carregar();

        assertEquals(1, lido.conexidade()); // A-B-C conectados em cadeia
    }
    private Lugar criarLugar(int id, Destino destino, String nome, TypeLugar tipo) {
        return Lugar.builder()
                .id(id)
                .destino(destino)
                .nome(nome)
                .descricao("Descrição de " + nome)
                .latitude(-23.5)
                .longitude(-46.6)
                .custo(10.0)
                .tempoPermanencia(1.5f)
                .score(4.5f)
                .numReviews(100)
                .horariosJson("[[\"09:00:00\",\"18:00:00\"]]")
                .tipo(tipo)
                .build();
    }
}
