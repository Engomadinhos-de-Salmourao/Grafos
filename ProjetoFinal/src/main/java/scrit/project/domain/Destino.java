package scrit.project.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "destino")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;
    @Column
    private Double latitude;

    @Column
    private Double longitude;
    @Column(name = "bounding_box")
    private String boundingBox;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusDestino status = StatusDestino.PENDENTE;

    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro;

    public enum StatusDestino {
        PENDENTE,
        PROCESSANDO,
        CONCLUIDO,
        ERRO
    }
}
