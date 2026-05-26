package scrit.project.dto;

import lombok.*;
import scrit.project.domain.*;

import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class RoteiroResponse {
    private Integer id;
    private String  destino;
    private String  hotel;
    private Double  orcamento;
    private Integer numDias;
    private Float   tempoDiario;
    private Double  custoTotal;
    private Double  distanciaTotal;
    private Float   scoreTotal;
    private List<DiaRoteiroDTO> dias;

    public static RoteiroResponse from(Roteiro r) {
        return RoteiroResponse.builder()
                .id(r.getId())
                .destino(r.getDestino().getNome())
                .hotel(r.getHotelNome())          // agora é coluna simples
                .orcamento(r.getOrcamento())
                .numDias(r.getNumDias())
                .tempoDiario(r.getTempoDiario())
                .custoTotal(r.getCustoTotal())
                .distanciaTotal(r.getDistanciaTotal())
                .scoreTotal(r.getScoreTotal())
                .dias(r.getDias() != null
                        ? r.getDias().stream().map(DiaRoteiroDTO::from).toList()
                        : List.of())
                .build();
    }

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DiaRoteiroDTO {
        private Integer numeroDia;
        private Double  custoDia;
        private Double  distanciaDia;
        private Float   tempoTotal;
        private Float   scoreDia;
        private List<ParadaDTO> paradas;

        public static DiaRoteiroDTO from(DiaRoteiro d) {
            return DiaRoteiroDTO.builder()
                    .numeroDia(d.getNumeroDia())
                    .custoDia(d.getCustoDia())
                    .distanciaDia(d.getDistanciaDia())
                    .tempoTotal(d.getTempoTotal())
                    .scoreDia(d.getScoreDia())
                    .paradas(d.getParadas() != null
                            ? d.getParadas().stream().map(ParadaDTO::from).toList()
                            : List.of())
                    .build();
        }
    }

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ParadaDTO {
        private Integer ordem;
        private Integer lugarId;
        private String  nome;
        private String  horarioChegada;
        private String  horarioSaida;
        private Double  custoVisita;
        private Double  distanciaAteProximo;
        private Float   tempoDeslocamentoAteProximo;

        public static ParadaDTO from(ParadaRoteiro p) {
            return ParadaDTO.builder()
                    .ordem(p.getOrdem())
                    .lugarId(p.getLugarId())
                    .nome(p.getLugarNome())
                    .horarioChegada(p.getHorarioChegada())
                    .horarioSaida(p.getHorarioSaida())
                    .custoVisita(p.getCustoVisita())
                    .distanciaAteProximo(p.getDistanciaAteProximo())
                    .tempoDeslocamentoAteProximo(p.getTempoDeslocamentoAteProximo())
                    .build();
        }
    }
}
