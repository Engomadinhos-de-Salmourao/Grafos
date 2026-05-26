package scrit.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scrit.project.domain.Destino;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DestinoResponse {
    private Integer id;
    private String nome;
    private Double latitude;
    private Double longitude;
    private String status;

    public static DestinoResponse from(Destino d) {
        return DestinoResponse.builder()
                .id(d.getId())
                .nome(d.getNome())
                .latitude(d.getLatitude())
                .longitude(d.getLongitude())
                .status(d.getStatus().name())
                .build();
    }
}
