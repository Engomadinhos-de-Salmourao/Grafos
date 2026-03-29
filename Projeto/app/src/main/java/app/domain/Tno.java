package app.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tno {
    private Integer lugar;
    private Float dist;
    private Float tempoDeslocamento;
    private Tno proximo;
}
