package scrit.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scrit.project.domain.*;
import scrit.project.dto.RoteiroRequest;
import scrit.project.dto.RoteiroResponse;
import scrit.project.repository.*;
import scrit.project.service.DijkstraService.MatrizCaminhos;
import scrit.project.service.KruskalService.SubgrafoMST;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoteiroService {

    private final DestinoRepository destinoRepository;
    private final LugarRepository   lugarRepository;
    private final RoteiroRepository roteiroRepository;
    private final KnapsackService   knapsackService;
    private final KruskalService    kruskalService;
    private final DijkstraService   dijkstraService;
    private final GrafoService      grafoService;

    private static final DateTimeFormatter HM         = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalTime         INICIO_DIA = LocalTime.of(9, 0);
    private static final LocalTime         FIM_DIA    = LocalTime.of(20, 0);

    @Transactional
    public RoteiroResponse gerar(RoteiroRequest req) throws IOException {
        log.info("Gerando roteiro: destino={}, orçamento={}, dias={}, h/dia={}",
                req.getDestinoId(), req.getOrcamento(), req.getNumDias(), req.getTempoDiario());

        Destino destino = destinoRepository.findById(req.getDestinoId())
                .orElseThrow(() -> new IllegalArgumentException("Destino não encontrado: " + req.getDestinoId()));

        // Hotel buscado do grafo.txt
        Lugar hotel = null;
        if (req.getHotelId() != null) {
            hotel = lugarRepository.findById(req.getHotelId())
                    .orElseThrow(() -> new IllegalArgumentException("Hotel não encontrado: " + req.getHotelId()));
            if (hotel.getTipo() != TypeLugar.HOTEL) {
                throw new IllegalArgumentException("O lugar informado não é um hotel.");
            }
        }

        List<Lugar> todosLugares = lugarRepository.findByDestinoId(req.getDestinoId());
        if (todosLugares.isEmpty()) {
            throw new IllegalStateException(
                    "Nenhum lugar cadastrado para o destino. " +
                    "Aguarde o processamento concluir (GET /api/destinos/" + req.getDestinoId() + ").");
        }

        float tempoTotalH  = req.getNumDias() * req.getTempoDiario();
        List<Lugar> selecionados = knapsackService.selecionar(todosLugares, req.getOrcamento(), tempoTotalH);

        if (selecionados.isEmpty()) {
            throw new IllegalStateException(
                    "Nenhum lugar se encaixa nas restrições de orçamento e tempo. " +
                    "Tente aumentar o orçamento ou o tempo disponível.");
        }

        List<Lugar> subconjunto = new ArrayList<>(selecionados);
        if (hotel != null && !subconjunto.contains(hotel)) {
            subconjunto.add(0, hotel);
        }

        GrafoDestino   grafo  = grafoService.obterGrafo();
        SubgrafoMST    mst    = kruskalService.gerarMST(grafo, subconjunto);
        MatrizCaminhos matriz = dijkstraService.calcular(mst);

        List<Lugar> restaurantes = todosLugares.stream()
                .filter(l -> l.getTipo() == TypeLugar.RESTAURANTE)
                .toList();

        List<DiaRoteiro> dias = distribuirPorDias(
                selecionados, restaurantes, hotel, matriz, mst,
                req.getNumDias(), req.getTempoDiario());

        double custoTotal     = dias.stream().mapToDouble(d -> d.getCustoDia()     != null ? d.getCustoDia()     : 0).sum();
        double distanciaTotal = dias.stream().mapToDouble(d -> d.getDistanciaDia() != null ? d.getDistanciaDia() : 0).sum();
        float  scoreTotal     = (float) selecionados.stream()
                .mapToDouble(l -> l.getScore() != null ? l.getScore() : 0).average().orElse(0);

        Roteiro roteiro = Roteiro.builder()
                .destino(destino)
                .hotelId(hotel != null ? hotel.getId() : null)
                .hotelNome(hotel != null ? hotel.getNome() : null)
                .orcamento(req.getOrcamento())
                .numDias(req.getNumDias())
                .tempoDiario(req.getTempoDiario())
                .custoTotal(custoTotal)
                .distanciaTotal(distanciaTotal)
                .scoreTotal(scoreTotal)
                .build();

        roteiro = roteiroRepository.save(roteiro);

        final Roteiro roteiroSalvo = roteiro;
        for (DiaRoteiro dia : dias) {
            dia.setRoteiro(roteiroSalvo);
            for (ParadaRoteiro parada : dia.getParadas()) {
                parada.setDiaRoteiro(dia);
            }
        }
        roteiro.getDias().addAll(dias);
        roteiro = roteiroRepository.save(roteiro);

        log.info("Roteiro id={} gerado: {} dias, custo=R${}, distância={}km, score={}",
                roteiro.getId(), dias.size(),
                String.format("%.2f", custoTotal),
                String.format("%.1f", distanciaTotal),
                String.format("%.1f", (double) scoreTotal));

        return RoteiroResponse.from(roteiro);
    }

    // ─── Distribuição ────────────────────────────────────────────────────────

    private List<DiaRoteiro> distribuirPorDias(
            List<Lugar> pontos, List<Lugar> restaurantes, Lugar hotel,
            MatrizCaminhos matriz, SubgrafoMST mst,
            int numDias, float tempoDiarioH) {

        // Mapa: índice local (na MatrizCaminhos) → Lugar
        Map<Integer, Lugar> localParaLugar = construirMapaLocal(pontos, mst);

        int hotelLocal = -1;
        if (hotel != null) {
            for (Map.Entry<Integer, Lugar> e : localParaLugar.entrySet()) {
                if (hotel.getId() != null && hotel.getId().equals(e.getValue().getId())) {
                    hotelLocal = e.getKey();
                    break;
                }
            }
        }

        List<Lugar> fila = new ArrayList<>(pontos);
        fila.sort(Comparator.comparingDouble((Lugar l) -> l.getScore() != null ? l.getScore() : 0).reversed());

        List<DiaRoteiro> dias  = new ArrayList<>();
        int pontoPorDia = Math.max(1, (int) Math.ceil((double) fila.size() / numDias));

        for (int d = 1; d <= numDias; d++) {
            int ini = (d - 1) * pontoPorDia;
            if (ini >= fila.size()) {
                dias.add(criarDiaVazio(d, hotel));
                continue;
            }
            int fim = Math.min(ini + pontoPorDia, fila.size());
            List<Lugar> pontosHoje = new ArrayList<>(fila.subList(ini, fim));
            pontosHoje = ordenarPorRotaMinima(pontosHoje, hotel, matriz, localParaLugar, hotelLocal);

            Lugar restaurante = escolherRestaurante(restaurantes);
            dias.add(montarDia(d, hotel, pontosHoje, restaurante, tempoDiarioH,
                    matriz, localParaLugar, hotelLocal));
        }

        return dias;
    }

    private Map<Integer, Lugar> construirMapaLocal(List<Lugar> pontos, SubgrafoMST mst) {
        Map<Integer, Lugar> mapa = new HashMap<>();
        for (int i = 0; i < mst.vertices().size(); i++) {
            int idLugar = mst.vertices().get(i) + 1; // base-0 → id base-1
            for (Lugar l : pontos) {
                if (l.getId() != null && l.getId() == idLugar) {
                    mapa.put(i, l);
                    break;
                }
            }
        }
        return mapa;
    }

    private List<Lugar> ordenarPorRotaMinima(
            List<Lugar> pontos, Lugar hotel,
            MatrizCaminhos matriz,
            Map<Integer, Lugar> localParaLugar, int hotelLocal) {

        if (pontos.size() <= 1 || matriz.vertices().isEmpty()) return pontos;

        Map<Integer, Integer> idParaLocal = new HashMap<>();
        for (Map.Entry<Integer, Lugar> e : localParaLugar.entrySet()) {
            if (e.getValue().getId() != null) idParaLocal.put(e.getValue().getId(), e.getKey());
        }

        List<Lugar>  ordenados = new ArrayList<>();
        Set<Integer> visitados = new HashSet<>();
        int atual = (hotelLocal >= 0) ? hotelLocal : 0;

        while (ordenados.size() < pontos.size()) {
            Lugar proximo     = null;
            float menorDist   = Float.MAX_VALUE;
            int   proxLocal   = -1;

            for (Lugar l : pontos) {
                if (visitados.contains(l.getId())) continue;
                Integer local = idParaLocal.get(l.getId());
                if (local == null) continue;

                float d = (atual < matriz.distancias().length && local < matriz.distancias()[atual].length)
                        ? matriz.dist(atual, local) : Float.MAX_VALUE;

                if (d < menorDist) { menorDist = d; proximo = l; proxLocal = local; }
            }

            if (proximo == null) {
                // lugares sem posição na matriz → adiciona no final
                for (Lugar l : pontos) {
                    if (!visitados.contains(l.getId())) { ordenados.add(l); visitados.add(l.getId()); }
                }
                break;
            }
            ordenados.add(proximo);
            visitados.add(proximo.getId());
            atual = proxLocal;
        }
        return ordenados;
    }

    private DiaRoteiro montarDia(
            int numeroDia, Lugar hotel, List<Lugar> pontos, Lugar restaurante,
            float tempoDiarioH, MatrizCaminhos matriz,
            Map<Integer, Lugar> localParaLugar, int hotelLocal) {

        List<ParadaRoteiro> paradas = new ArrayList<>();
        LocalTime horario = INICIO_DIA;
        double custoDia = 0;
        double distDia  = 0;
        float  scoreDia = 0;
        int    ordem    = 1;

        if (hotel != null) {
            paradas.add(paradaBuilder(hotel.getId(), hotel.getNome(), ordem++,
                    horario.format(HM), horario.format(HM), 0.0, null, null));
        }

        int     meioIndex           = pontos.size() / 2;
        boolean restauranteInserido = false;

        for (int i = 0; i < pontos.size(); i++) {
            Lugar ponto     = pontos.get(i);
            Lugar anterior  = (i == 0) ? hotel : pontos.get(i - 1);

            float deslocMin = estimarDeslocamento(anterior, ponto, matriz, localParaLugar, hotelLocal);
            double deslocKm = deslocMin / 60.0 * 30.0; // 30 km/h

            horario = horario.plusMinutes((long) deslocMin);
            if (horario.isAfter(FIM_DIA)) break;

            if (!estaAberto(ponto, horario)) horario = LocalTime.of(9, 1);

            float     perm  = ponto.getTempoPermanencia() != null ? ponto.getTempoPermanencia() : 1.5f;
            LocalTime saida = horario.plusMinutes((long) (perm * 60));

            paradas.add(paradaBuilder(ponto.getId(), ponto.getNome(), ordem++,
                    horario.format(HM), saida.format(HM),
                    ponto.getCusto() != null ? ponto.getCusto() : 0.0,
                    deslocKm, deslocMin));

            custoDia += ponto.getCusto() != null ? ponto.getCusto() : 0.0;
            distDia  += deslocKm;
            scoreDia += ponto.getScore() != null ? ponto.getScore() : 0;
            horario   = saida;

            if (!restauranteInserido && i >= meioIndex && restaurante != null) {
                float deslocRest = estimarDeslocamento(ponto, restaurante, matriz, localParaLugar, hotelLocal);
                double kmRest    = deslocRest / 60.0 * 30.0;
                horario = horario.plusMinutes((long) deslocRest);
                double custoRest = restaurante.getCusto() != null ? restaurante.getCusto() : 30.0;

                paradas.add(paradaBuilder(restaurante.getId(), restaurante.getNome(), ordem++,
                        horario.format(HM), horario.plusMinutes(60).format(HM),
                        custoRest, kmRest, deslocRest));

                custoDia += custoRest;
                distDia  += kmRest;
                horario   = horario.plusMinutes(60);
                restauranteInserido = true;
            }
        }

        // Retorno ao hotel
        if (hotel != null && !paradas.isEmpty()) {
            Lugar ultimo =  buscarLugarPorIdNaLista(
                    paradas.get(paradas.size() - 1).getLugarId(),
                    pontos);
            float deslocVolta = estimarDeslocamento(ultimo, hotel, matriz, localParaLugar, hotelLocal);
            double kmVolta    = deslocVolta / 60.0 * 30.0;
            horario = horario.plusMinutes((long) deslocVolta);

            paradas.add(paradaBuilder(hotel.getId(), hotel.getNome(), ordem,
                    horario.format(HM), horario.format(HM), 0.0, 0.0, 0f));
            distDia += kmVolta;
        }

        float tempoTotal = (float) ((horario.toSecondOfDay() - INICIO_DIA.toSecondOfDay()) / 3600.0);

        return DiaRoteiro.builder()
                .numeroDia(numeroDia)
                .custoDia(custoDia)
                .distanciaDia(distDia)
                .tempoTotal(tempoTotal)
                .scoreDia(pontos.isEmpty() ? 0 : scoreDia / pontos.size())
                .paradas(paradas)
                .build();
    }

    // ─── Utilitários ─────────────────────────────────────────────────────────

    private ParadaRoteiro paradaBuilder(Integer lugarId, String lugarNome, int ordem,
            String chegada, String saida, Double custo,
            Double distProx, Float tempoProx) {
        return ParadaRoteiro.builder()
                .lugarId(lugarId)
                .lugarNome(lugarNome)
                .ordem(ordem)
                .horarioChegada(chegada)
                .horarioSaida(saida)
                .custoVisita(custo)
                .distanciaAteProximo(distProx)
                .tempoDeslocamentoAteProximo(tempoProx)
                .build();
    }

    private float estimarDeslocamento(Lugar de, Lugar para,
            MatrizCaminhos matriz, Map<Integer, Lugar> localParaLugar, int hotelLocal) {

        if (de == null || para == null) return 0f;
        if (de.getId() != null && de.getId().equals(para.getId())) return 0f;

        if (!matriz.vertices().isEmpty()) {
            Integer localDe   = idParaLocal(de.getId(),   localParaLugar);
            Integer localPara = idParaLocal(para.getId(), localParaLugar);
            if (localDe != null && localPara != null
                    && localDe < matriz.tempos().length
                    && localPara < matriz.tempos()[localDe].length) {
                float t = matriz.tempo(localDe, localPara);
                if (t < Float.MAX_VALUE / 2) return t;
            }
        }

        if (de.getLatitude() != null && para.getLatitude() != null) {
            double km = scrit.project.utils.HaversineUtil.distanciaKm(
                    de.getLatitude(), de.getLongitude(),
                    para.getLatitude(), para.getLongitude());
            return scrit.project.utils.HaversineUtil.tempoDeslocamentoMinutos(km);
        }

        return 10f;
    }

    private Integer idParaLocal(Integer id, Map<Integer, Lugar> localParaLugar) {
        for (Map.Entry<Integer, Lugar> e : localParaLugar.entrySet()) {
            if (e.getValue().getId() != null && e.getValue().getId().equals(id)) return e.getKey();
        }
        return null;
    }

    private boolean estaAberto(Lugar lugar, LocalTime horario) {
        if (lugar.getHorariosJson() == null || lugar.getHorariosJson().isBlank()) return true;
        return !horario.isBefore(LocalTime.of(9, 0)) && !horario.isAfter(LocalTime.of(19, 0));
    }

    private Lugar escolherRestaurante(List<Lugar> restaurantes) {
        if (restaurantes == null || restaurantes.isEmpty()) return null;
        return restaurantes.stream()
                .max(Comparator.comparingDouble(l -> l.getScore() != null ? l.getScore() : 0))
                .orElse(null);
    }

    private DiaRoteiro criarDiaVazio(int numeroDia, Lugar hotel) {
        List<ParadaRoteiro> paradas = new ArrayList<>();
        if (hotel != null) {
            paradas.add(paradaBuilder(hotel.getId(), hotel.getNome(), 1,
                    "09:00", "09:00", 0.0, null, null));
        }
        return DiaRoteiro.builder()
                .numeroDia(numeroDia)
                .custoDia(0.0).distanciaDia(0.0).tempoTotal(0f).scoreDia(0f)
                .paradas(paradas)
                .build();
    }

    @SafeVarargs
    private Lugar buscarLugarPorIdNaLista(Integer id, List<Lugar>... listas) {
        for (List<Lugar> lista : listas) {
            if (lista == null) continue;
            for (Lugar l : lista) {
                if (l != null && id != null && id.equals(l.getId())) return l;
            }
        }
        return null;
    }

    // ─── Consultas ───────────────────────────────────────────────────────────

    public List<RoteiroResponse> listarPorDestino(Integer destinoId) {
        return roteiroRepository.findByDestinoId(destinoId).stream()
                .map(RoteiroResponse::from)
                .toList();
    }

    public RoteiroResponse buscarPorId(Integer id) {
        return roteiroRepository.findById(id)
                .map(RoteiroResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Roteiro não encontrado: id=" + id));
    }
}
