package scrit.project.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tno {
    private int lugar;
    private Float dist;
    private Float tempoDeslocamento;
    private Tno proximo;
}
