package scrit.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import scrit.project.domain.Lugar;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlacesService {

    private static final String FIND_PLACE_URL =
            "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"
          + "?input=%s&inputtype=textquery&locationbias=point:%s,%s"
          + "&fields=place_id,rating,user_ratings_total,price_level,opening_hours&key=%s";

    private static final String DETAILS_URL =
            "https://maps.googleapis.com/maps/api/place/details/json"
          + "?place_id=%s&fields=rating,user_ratings_total,price_level,opening_hours&key=%s";
    private static final double[] CUSTO_POR_NIVEL = {0.0, 20.0, 50.0, 100.0, 200.0};

    @Value("${google.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    
    public void enriquecerLugares(List<Lugar> lugares) {
        log.info("Google Places: enriquecendo {} lugares...", lugares.size());
        int enriquecidos = 0;

        for (Lugar lugar : lugares) {
            try {
                boolean ok = enriquecer(lugar);
                if (ok) enriquecidos++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.debug("Google Places: falha ao enriquecer '{}': {}", lugar.getNome(), e.getMessage());
            }
        }

        log.info("Google Places: {}/{} lugares enriquecidos.", enriquecidos, lugares.size());
    }

    @Cacheable(value = "googlePlaces", key = "#lugar.nome + '_' + #lugar.latitude + '_' + #lugar.longitude")
    public boolean enriquecer(Lugar lugar) throws IOException, InterruptedException {
        String url = String.format(FIND_PLACE_URL,
                encode(lugar.getNome()),
                lugar.getLatitude(), lugar.getLongitude(),
                apiKey);

        String json = get(url);
        JsonNode root = objectMapper.readTree(json);

        if (!"OK".equals(root.path("status").asText())) {
            return false;
        }

        JsonNode candidates = root.get("candidates");
        if (candidates == null || candidates.isEmpty()) return false;

        JsonNode candidate = candidates.get(0);
        String placeId = candidate.path("place_id").asText(null);
        JsonNode dados = candidate;
        if (placeId != null && !placeId.isBlank()) {
            dados = buscarDetalhes(placeId);
        }
        aplicarDados(lugar, dados);
        return true;
    }

    private JsonNode buscarDetalhes(String placeId) throws IOException, InterruptedException {
        String url = String.format(DETAILS_URL, placeId, apiKey);
        String json = get(url);
        JsonNode root = objectMapper.readTree(json);
        return root.path("result");
    }

    private void aplicarDados(Lugar lugar, JsonNode dados) {
        if (dados.has("rating")) {
            lugar.setScore((float) dados.get("rating").asDouble());
        }
        if (dados.has("user_ratings_total")) {
            lugar.setNumReviews(dados.get("user_ratings_total").asInt());
        }
        if (dados.has("price_level")) {
            int nivel = dados.get("price_level").asInt();
            if (nivel >= 0 && nivel < CUSTO_POR_NIVEL.length) {
                lugar.setCusto(CUSTO_POR_NIVEL[nivel]);
            }
        }
        JsonNode openingHours = dados.path("opening_hours");
        if (!openingHours.isMissingNode()) {
            String horariosJson = parsearHorarios(openingHours);
            if (horariosJson != null) {
                lugar.setHorariosJson(horariosJson);
            }
        }
    }

    
    private String parsearHorarios(JsonNode openingHours) {
        try {
            String[][] horarios = new String[7][2];
            for (int i = 0; i < 7; i++) {
                horarios[i][0] = "09:00:00";
                horarios[i][1] = "18:00:00";
            }

            JsonNode periodos = openingHours.get("periods");
            if (periodos != null) {
                for (JsonNode periodo : periodos) {
                    JsonNode open  = periodo.get("open");
                    JsonNode close = periodo.get("close");
                    if (open == null) continue;

                    int dia = open.get("day").asInt(); // 0=Dom..6=Sab
                    String abertura  = formatarHora(open.path("time").asText("0900"));
                    String fechamento = (close != null)
                            ? formatarHora(close.path("time").asText("2200"))
                            : "23:59:00";

                    horarios[dia][0] = abertura;
                    horarios[dia][1] = fechamento;
                }
            }
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < 7; i++) {
                sb.append("[").append(horarios[i][0]).append(", ").append(horarios[i][1]).append("]");
                if (i < 6) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();

        } catch (Exception e) {
            log.debug("Falha ao parsear horários: {}", e.getMessage());
            return null;
        }
    }

    
    private String formatarHora(String hhmm) {
        if (hhmm == null || hhmm.length() < 4) return "09:00:00";
        return hhmm.substring(0, 2) + ":" + hhmm.substring(2, 4) + ":00";
    }

    private String get(String url) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new IOException("Google Places HTTP " + resp.statusCode());
        }
        return resp.body();
    }

    private String encode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }
}
