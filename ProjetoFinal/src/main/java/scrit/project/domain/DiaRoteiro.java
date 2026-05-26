package scrit.project.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "dia_roteiro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DiaRoteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roteiro_id", nullable = false)
    private Roteiro roteiro;

    @Column(name = "numero_dia", nullable = false)
    private Integer numeroDia;

    @Column(name = "custo_dia")
    private Double custoDia;

    @Column(name = "distancia_dia")
    private Double distanciaDia;         // km percorridos no dia

    @Column(name = "tempo_total")
    private Float tempoTotal;            // horas de passeio no dia

    @Column(name = "score_dia")
    private Float scoreDia;

    @OneToMany(mappedBy = "diaRoteiro", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParadaRoteiro> paradas = new ArrayList<>();
}
