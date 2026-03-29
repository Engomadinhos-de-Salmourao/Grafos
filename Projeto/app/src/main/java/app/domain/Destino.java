package app.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Destino {
    private Long id;
    private String nome;
}
