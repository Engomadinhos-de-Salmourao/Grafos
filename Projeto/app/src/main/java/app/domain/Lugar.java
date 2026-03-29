package app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;

@Getter
@AllArgsConstructor
public abstract class Lugar {
    private Integer id;
    private String nome;
    private Double custo;
    private Float tempo;
    private Float score;
    private Time horarioAbertura;
    private Time horarioFechamento;
    private TypeLugar tipo;

    public void atualizarDados(Double custo, Float tempo, Time horarioAbertura, Time horarioFechamento){
        this.custo = custo;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.tempo = tempo;
    }
}
