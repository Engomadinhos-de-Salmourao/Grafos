package scrit.project.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa uma parada (ponto de visita) dentro de um DiaRoteiro.
 *
 * Como Lugar não é mais uma entidade JPA, armazenamos apenas o ID e o nome
 * do lugar como colunas simples. O restante dos dados do lugar pode ser
 * recuperado do grafo.txt via GrafoFileRepository quando necessário.
 */
@Entity
@Table(name = "parada_roteiro")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParadaRoteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dia_roteiro_id", nullable = false)
    private DiaRoteiro diaRoteiro;

    /** ID do Lugar no grafo.txt (base-1). Não é uma FK no banco. */
    @Column(name = "lugar_id", nullable = false)
    private Integer lugarId;

    /** Nome do lugar — desnormalizado para consultas rápidas sem recarregar o grafo. */
    @Column(name = "lugar_nome")
    private String lugarNome;

    @Column(nullable = false)
    private Integer ordem;

    @Column(name = "horario_chegada")
    private String horarioChegada;

    @Column(name = "horario_saida")
    private String horarioSaida;

    @Column(name = "custo_visita")
    private Double custoVisita;

    @Column(name = "distancia_ate_proximo")
    private Double distanciaAteProximo;

    @Column(name = "tempo_deslocamento_ate_proximo")
    private Float tempoDeslocamentoAteProximo;
}
