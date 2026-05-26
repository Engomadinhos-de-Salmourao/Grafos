package scrit.project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA que representa um roteiro turístico gerado.
 *
 * O hotel já não é um @ManyToOne para Lugar (que saiu do JPA).
 * Armazenamos apenas hotelId e hotelNome como colunas simples.
 */
@Entity
@Table(name = "roteiro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Roteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    /** ID do hotel no grafo.txt. Não é uma FK no banco. */
    @Column(name = "hotel_id")
    private Integer hotelId;

    /** Nome do hotel — desnormalizado. */
    @Column(name = "hotel_nome")
    private String hotelNome;

    @Column(nullable = false)
    private Double orcamento;

    @Column(name = "num_dias", nullable = false)
    private Integer numDias;

    @Column(name = "tempo_diario", nullable = false)
    private Float tempoDiario;

    @Column(name = "custo_total")
    private Double custoTotal;

    @Column(name = "distancia_total")
    private Double distanciaTotal;

    @Column(name = "score_total")
    private Float scoreTotal;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "roteiro", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DiaRoteiro> dias = new ArrayList<>();

    @PrePersist
    void prePersist() { this.criadoEm = LocalDateTime.now(); }

    /** Conveniência: nome do hotel (pode ser null se não houver hotel). */
    public String getHotelNomeOuNull() {
        return hotelNome;
    }
}
