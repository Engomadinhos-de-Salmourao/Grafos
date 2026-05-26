package scrit.project.service;

import org.junit.jupiter.api.Test;
import scrit.project.domain.*;
import scrit.project.service.KruskalService.*;
import scrit.project.service.DijkstraService.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
class KnapsackServiceTest {

    private final KnapsackService knapsack = new KnapsackService();

    @Test
    void selecionaMelhorSubconjunto() {
        List<Lugar> candidatos = List.of(
                ponto(1, "MASP",     34.0, 2.0f, 4.6f, 500),
                ponto(2, "Ibirapuera", 0.0, 3.0f, 4.9f, 800),
                ponto(3, "Pinacoteca",55.0, 2.5f, 4.5f, 400),
                ponto(4, "Futebol",   90.0, 2.0f, 4.0f, 300),
                ponto(5, "Theatro",   83.0, 1.5f, 4.7f, 600)
        );
        List<Lugar> sel = knapsack.selecionar(candidatos, 200.0, 6.0f);

        assertFalse(sel.isEmpty(), "Deve selecionar ao menos 1 lugar");

        double custoSel = sel.stream().mapToDouble(l -> l.getCusto() != null ? l.getCusto() : 0).sum();
        float  tempoSel = (float) sel.stream().mapToDouble(l -> l.getTempoPermanencia() != null ? l.getTempoPermanencia() : 0).sum();

        assertTrue(custoSel <= 200.0 + 1e-3, "Custo deve respeitar orçamento. Foi: " + custoSel);
        assertTrue(tempoSel <= 6.0f  + 0.01f, "Tempo deve respeitar limite. Foi: " + tempoSel);
    }

    @Test
    void listaVaziaSeOrcamentoZero() {
        List<Lugar> candidatos = List.of(ponto(1, "X", 50.0, 1.0f, 4.0f, 100));
        List<Lugar> sel = knapsack.selecionar(candidatos, 0.0, 5.0f);
        assertTrue(sel.isEmpty() || sel.stream().allMatch(l -> l.getCusto() == 0),
                "Com orçamento zero só pode selecionar lugares gratuitos");
    }

    @Test
    void ignoraHoteis() {
        Lugar hotel = Lugar.builder().id(10).nome("Hotel").tipo(TypeLugar.HOTEL)
                .custo(0.0).tempoPermanencia(0f).score(4.0f).numReviews(100).build();
        Lugar ponto = ponto(1, "Ponto", 10.0, 1.0f, 4.5f, 50);

        List<Lugar> sel = knapsack.selecionar(List.of(hotel, ponto), 500.0, 10.0f);
        assertTrue(sel.stream().noneMatch(l -> l.getTipo() == TypeLugar.HOTEL),
                "Hotéis não devem ser selecionados pela mochila");
    }

    private Lugar ponto(int id, String nome, double custo, float tempo, float score, int reviews) {
        return Lugar.builder().id(id).nome(nome).tipo(TypeLugar.PONTO_TURISTICO)
                .custo(custo).tempoPermanencia(tempo).score(score).numReviews(reviews).build();
    }
}
class KruskalServiceTest {

    private final KruskalService kruskal = new KruskalService();

    @Test
    void mstComGrafoSimples() {
        GrafoDestino grafo = new GrafoDestino(4);
        Destino d = new Destino(); d.setId(1);

        grafo.getLugares().put(1, lugar(1, d, "A", TypeLugar.PONTO_TURISTICO));
        grafo.getLugares().put(2, lugar(2, d, "B", TypeLugar.PONTO_TURISTICO));
        grafo.getLugares().put(3, lugar(3, d, "C", TypeLugar.PONTO_TURISTICO));
        grafo.getLugares().put(4, lugar(4, d, "D", TypeLugar.PONTO_TURISTICO));
        grafo.insereA(0, 1, 1.0f, 3.0f);
        grafo.insereA(0, 2, 4.0f, 12.0f);
        grafo.insereA(1, 2, 2.0f, 6.0f);
        grafo.insereA(1, 3, 5.0f, 15.0f);
        grafo.insereA(2, 3, 3.0f, 9.0f);

        List<Lugar> selecionados = new ArrayList<>(grafo.getLugares().values());
        SubgrafoMST mst = kruskal.gerarMST(grafo, selecionados);
        assertEquals(3, mst.arestas().size(), "MST de 4 vértices deve ter 3 arestas");
        assertEquals(6.0f, mst.distanciaTotal(), 0.01f);
    }

    @Test
    void mstComUmVertice() {
        GrafoDestino grafo = new GrafoDestino(1);
        Destino d = new Destino(); d.setId(1);
        grafo.getLugares().put(1, lugar(1, d, "Só", TypeLugar.PONTO_TURISTICO));

        SubgrafoMST mst = kruskal.gerarMST(grafo, List.of(grafo.getLugares().get(1)));
        assertEquals(0, mst.arestas().size());
    }

    private Lugar lugar(int id, Destino d, String nome, TypeLugar tipo) {
        return Lugar.builder().id(id).destino(d).nome(nome).tipo(tipo)
                .latitude(-23.5).longitude(-46.6).build();
    }
}
class DijkstraServiceTest {

    private final KruskalService  kruskal  = new KruskalService();
    private final DijkstraService dijkstra = new DijkstraService();

    @Test
    void distanciasCorretasEmCadeiaLinear() {
        GrafoDestino grafo = new GrafoDestino(3);
        Destino d = new Destino(); d.setId(1);

        grafo.getLugares().put(1, lugar(1, d, "A", TypeLugar.PONTO_TURISTICO));
        grafo.getLugares().put(2, lugar(2, d, "B", TypeLugar.PONTO_TURISTICO));
        grafo.getLugares().put(3, lugar(3, d, "C", TypeLugar.PONTO_TURISTICO));

        grafo.insereA(0, 1, 1.0f, 2.0f);
        grafo.insereA(1, 2, 2.0f, 4.0f);

        List<Lugar> todos = new ArrayList<>(grafo.getLugares().values());
        SubgrafoMST mst = kruskal.gerarMST(grafo, todos);
        MatrizCaminhos mat = dijkstra.calcular(mst);
        assertEquals(3, mat.vertices().size());
        int localA = mat.vertices().indexOf(0); // idx base-0 do vértice A
        int localC = mat.vertices().indexOf(2);

        if (localA >= 0 && localC >= 0) {
            assertEquals(3.0f, mat.dist(localA, localC), 0.01f,
                    "Distância A→C deve ser 1+2=3");
            assertEquals(6.0f, mat.tempo(localA, localC), 0.01f,
                    "Tempo A→C deve ser 2+4=6");
        }
    }

    @Test
    void distanciaZeroParaOMesmoVertice() {
        GrafoDestino grafo = new GrafoDestino(2);
        Destino d = new Destino(); d.setId(1);
        grafo.getLugares().put(1, lugar(1, d, "X", TypeLugar.PONTO_TURISTICO));
        grafo.getLugares().put(2, lugar(2, d, "Y", TypeLugar.PONTO_TURISTICO));
        grafo.insereA(0, 1, 5.0f, 10.0f);

        SubgrafoMST mst = kruskal.gerarMST(grafo, new ArrayList<>(grafo.getLugares().values()));
        MatrizCaminhos mat = dijkstra.calcular(mst);

        for (int i = 0; i < mat.vertices().size(); i++) {
            assertEquals(0f, mat.dist(i, i), 0.001f, "Distância de um vértice a si mesmo deve ser 0");
        }
    }

    private Lugar lugar(int id, Destino d, String nome, TypeLugar tipo) {
        return Lugar.builder().id(id).destino(d).nome(nome).tipo(tipo)
                .latitude(-23.5 + id * 0.01).longitude(-46.6 + id * 0.01).build();
    }
}
