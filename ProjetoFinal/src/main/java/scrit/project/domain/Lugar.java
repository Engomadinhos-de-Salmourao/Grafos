package scrit.project.domain;

import lombok.*;

/**
 * Lugar representa um vértice do grafo (ponto turístico, hotel ou restaurante).
 * NÃO é uma entidade JPA — todos os dados são persistidos exclusivamente
 * no arquivo grafo.txt via GrafoFileRepository.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lugar {

    private Integer id;

    /** Referência leve ao destino (apenas o ID é salvo no .txt). */
    private Destino destino;

    private String nome;

    private String descricao;

    private Double latitude;

    private Double longitude;

    private String endereco;

    private Double custo;

    private Float tempoPermanencia;

    private Float score;

    private Integer numReviews;

    /** JSON com horários de funcionamento, ex: [[09:00:00, 18:00:00], ...] */
    private String horariosJson;

    private TypeLugar tipo;

    public void atualizarDadosEnriquecidos(Double custo, Float tempoPermanencia,
                                            String horariosJson, Integer numReviews, Float score) {
        this.custo = custo;
        this.tempoPermanencia = tempoPermanencia;
        this.horariosJson = horariosJson;
        this.numReviews = numReviews;
        this.score = score;
    }
}
