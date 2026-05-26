package scrit.project.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RoteiroRequest {
    private Integer destinoId;
    private Double  orcamento;
    private Integer numDias;
    private Float   tempoDiario;
    private Integer hotelId;
}
