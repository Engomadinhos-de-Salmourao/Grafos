package scrit.project.dto;

import lombok.Getter;
import lombok.Setter;
import scrit.project.domain.Destino;
import scrit.project.domain.Lugar;
import scrit.project.domain.TypeLugar;

@Getter
@Setter
public class LugarRequest {
    private Integer destinoId;
    private String nome;
    private String descricao;
    private Double latitude;
    private Double longitude;
    private String endereco;
    private Double custo;
    private Float tempoPermanencia;
    private Float score;
    private Integer numReviews;
    private String horariosJson;
    private String tipo; // "HOTEL", "PONTO_TURISTICO", "RESTAURANTE"

    public Lugar toLugar() {
        Destino destino = new Destino();
        destino.setId(destinoId);

        return Lugar.builder()
                .destino(destino)
                .nome(nome)
                .descricao(descricao)
                .latitude(latitude)
                .longitude(longitude)
                .endereco(endereco)
                .custo(custo)
                .tempoPermanencia(tempoPermanencia)
                .score(score)
                .numReviews(numReviews)
                .horariosJson(horariosJson)
                .tipo(TypeLugar.valueOf(tipo))
                .build();
    }
}
