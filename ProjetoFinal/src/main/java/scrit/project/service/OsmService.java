package scrit.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import scrit.project.domain.Destino;
import scrit.project.domain.Lugar;
import scrit.project.domain.TypeLugar;

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
public class OsmService {

    private static final String NOMINATIM_URL =
            "https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1&addressdetails=1";

    private static final String OVERPASS_URL = "https://overpass-api.de/api/interpreter";
    private static final String OVERPASS_QUERY_TEMPLATE = """
            [out:json][timeout:60];
            (
              node["tourism"~"hotel|attraction|museum|viewpoint|zoo|aquarium|theme_park|artwork"](%s);
              node["amenity"~"restaurant|cafe|fast_food|bar|pub"](%s);
              node["leisure"~"park|nature_reserve|garden"](%s);
              node["historic"~"monument|memorial|castle|ruins|building"](%s);
            );
            out body 150;
            """;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    
    @Cacheable(value = "nominatim", key = "#nome")
    public void geocodificar(Destino destino, String nome) throws IOException, InterruptedException {
        String url = String.format(NOMINATIM_URL, encode(nome));
        log.info("Nominatim: buscando '{}'", nome);

        String json = get(url, "Mozilla/5.0 (compatible; TurismoBot/1.0)");
        JsonNode array = objectMapper.readTree(json);

        if (array.isEmpty()) {
            throw new IOException("Cidade não encontrada no OSM: " + nome);
        }

        JsonNode result = array.get(0);
        destino.setLatitude(result.get("lat").asDouble());
        destino.setLongitude(result.get("lon").asDouble());
        JsonNode bb = result.get("boundingbox");
        if (bb != null && bb.size() >= 4) {
            String bbox = bb.get(0).asText() + "," + bb.get(2).asText() + ","
                        + bb.get(1).asText() + "," + bb.get(3).asText();
            destino.setBoundingBox(bbox);
        }

        log.info("Geocodificado: lat={} lng={} bbox={}",
                destino.getLatitude(), destino.getLongitude(), destino.getBoundingBox());
    }

    
    @Cacheable(value = "overpass", key = "#destino.id")
    public List<Lugar> buscarLugares(Destino destino) throws IOException, InterruptedException {
        String bbox = destino.getBoundingBox();
        if (bbox == null || bbox.isBlank()) {
            throw new IllegalStateException("Destino sem bounding box: " + destino.getNome());
        }

        String query = String.format(OVERPASS_QUERY_TEMPLATE, bbox, bbox, bbox, bbox);
        log.info("Overpass: buscando lugares em bbox={}", bbox);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(OVERPASS_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("data=" + encode(query)))
                .timeout(Duration.ofSeconds(60))
                .build();

        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new IOException("Overpass retornou status " + resp.statusCode());
        }

        return parseLugares(objectMapper.readTree(resp.body()), destino);
    }

    private List<Lugar> parseLugares(JsonNode root, Destino destino) {
        List<Lugar> lista = new ArrayList<>();
        JsonNode elementos = root.get("elements");
        if (elementos == null) return lista;

        for (JsonNode el : elementos) {
            try {
                JsonNode tags = el.get("tags");
                if (tags == null) continue;

                String nome = tags.has("name") ? tags.get("name").asText() : null;
                if (nome == null || nome.isBlank()) continue;

                double lat = el.get("lat").asDouble();
                double lon = el.get("lon").asDouble();

                TypeLugar tipo = inferirTipo(tags);
                String endereco = construirEndereco(tags);

                Lugar lugar = Lugar.builder()
                        .destino(destino)
                        .nome(nome)
                        .descricao(tags.has("description") ? tags.get("description").asText() : "")
                        .latitude(lat)
                        .longitude(lon)
                        .endereco(endereco)
                        .tipo(tipo)
                        .custo(0.0)
                        .tempoPermanencia(tempoPadrao(tipo))
                        .score(0.0f)
                        .numReviews(0)
                        .horariosJson(horariosAberto())
                        .build();

                lista.add(lugar);
            } catch (Exception e) {
                log.debug("Elemento OSM ignorado: {}", e.getMessage());
            }
        }

        log.info("Overpass: {} lugares encontrados para '{}'", lista.size(), destino.getNome());
        return lista;
    }

    private TypeLugar inferirTipo(JsonNode tags) {
        if (tags.has("tourism")) {
            String t = tags.get("tourism").asText();
            if (t.equals("hotel") || t.equals("hostel") || t.equals("guest_house")) return TypeLugar.HOTEL;
        }
        if (tags.has("amenity")) {
            String a = tags.get("amenity").asText();
            if (a.equals("restaurant") || a.equals("cafe") || a.equals("fast_food")
                    || a.equals("bar") || a.equals("pub")) return TypeLugar.RESTAURANTE;
        }
        return TypeLugar.PONTO_TURISTICO;
    }

    private String construirEndereco(JsonNode tags) {
        StringBuilder sb = new StringBuilder();
        if (tags.has("addr:street"))  sb.append(tags.get("addr:street").asText());
        if (tags.has("addr:housenumber")) sb.append(", ").append(tags.get("addr:housenumber").asText());
        if (tags.has("addr:city")) sb.append(" - ").append(tags.get("addr:city").asText());
        return sb.toString().trim();
    }

    private float tempoPadrao(TypeLugar tipo) {
        return switch (tipo) {
            case HOTEL -> 0.0f;
            case RESTAURANTE -> 1.0f;
            case PONTO_TURISTICO -> 1.5f;
        };
    }

    
    private String horariosAberto() {
        return "[[09:00:00, 18:00:00], [09:00:00, 18:00:00], [09:00:00, 18:00:00], "
             + "[09:00:00, 18:00:00], [09:00:00, 18:00:00], [09:00:00, 18:00:00], "
             + "[09:00:00, 18:00:00]]";
    }

    private String get(String url, String userAgent) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", userAgent)
                .GET()
                .timeout(Duration.ofSeconds(15))
                .build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new IOException("HTTP " + resp.statusCode() + " em " + url);
        }
        return resp.body();
    }

    private String encode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }
}
