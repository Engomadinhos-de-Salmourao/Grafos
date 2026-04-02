package app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lugar {
    private Integer id;
    private Integer id_destino;
    private String nome;
    private String description;
    private Double custo;
    private Float tempo;
    private Float score;
    private Integer num_reviews;
    private List<Set<Time>> horarios;
    private TypeLugar tipo;


    public void atualizarDados(Double custo, Float tempo, List<Set<Time>> horarios, Integer num_reviews,  Float score){
        this.custo = custo;
        this.horarios = horarios;
        this.tempo = tempo;
        this.num_reviews = num_reviews;
        this.score = score;
    }
}
