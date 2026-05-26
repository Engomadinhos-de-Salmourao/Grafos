package scrit.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import scrit.project.domain.Lugar;
import scrit.project.domain.TypeLugar;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class KnapsackService {
    private static final float PASSO_TEMPO = 0.5f;

    
    public List<Lugar> selecionar(List<Lugar> candidatos, double orcamento, float tempoTotalH) {
        List<Lugar> itens = candidatos.stream()
                .filter(l -> l.getTipo() == TypeLugar.PONTO_TURISTICO)
                .filter(l -> l.getTempoPermanencia() != null && l.getTempoPermanencia() > 0)
                .filter(l -> l.getCusto() != null)
                .toList();

        if (itens.isEmpty()) {
            log.warn("Knapsack: nenhum ponto turístico disponível.");
            return List.of();
        }

        int n = itens.size();
        int capCusto  = (int) Math.ceil(orcamento);                         // em R$
        int capTempo  = (int) Math.ceil(tempoTotalH / PASSO_TEMPO);         // em unidades de 0.5h
        int[]    pesoCusto  = new int[n];
        int[]    pesoTempo  = new int[n];
        double[] beneficio  = new double[n];
        double maxBenef = 0;
        for (int i = 0; i < n; i++) {
            Lugar l = itens.get(i);
            double b = l.getScore() * Math.log1p(l.getNumReviews() != null ? l.getNumReviews() : 1);
            beneficio[i] = b;
            if (b > maxBenef) maxBenef = b;

            pesoCusto[i] = (int) Math.ceil(l.getCusto());
            pesoTempo[i] = (int) Math.ceil(l.getTempoPermanencia() / PASSO_TEMPO);
        }
        int[] benef = new int[n];
        for (int i = 0; i < n; i++) {
            benef[i] = (maxBenef > 0) ? (int) Math.round((beneficio[i] / maxBenef) * 1000) : 0;
        }

        log.info("Knapsack: n={} itens, capCusto=R${}, capTempo={}x0.5h", n, capCusto, capTempo);
        int[][] dpAtual = new int[capCusto + 1][capTempo + 1];
        int[][] dpAnter = new int[capCusto + 1][capTempo + 1];
        boolean[][][] keep = new boolean[n][capCusto + 1][capTempo + 1];

        for (int i = 0; i < n; i++) {
            for (int c = 0; c <= capCusto; c++) {
                System.arraycopy(dpAtual[c], 0, dpAnter[c], 0, capTempo + 1);
            }

            int pc = pesoCusto[i];
            int pt = pesoTempo[i];
            int b  = benef[i];

            for (int c = 0; c <= capCusto; c++) {
                for (int t = 0; t <= capTempo; t++) {
                    dpAtual[c][t] = dpAnter[c][t]; // não pega o item
                    if (c >= pc && t >= pt) {
                        int comItem = dpAnter[c - pc][t - pt] + b;
                        if (comItem > dpAtual[c][t]) {
                            dpAtual[c][t] = comItem;
                            keep[i][c][t] = true;
                        }
                    }
                }
            }
        }
        List<Lugar> selecionados = new ArrayList<>();
        int c = capCusto;
        int t = capTempo;

        for (int i = n - 1; i >= 0; i--) {
            if (keep[i][c][t]) {
                selecionados.add(0, itens.get(i)); // insere na frente para manter ordem
                c -= pesoCusto[i];
                t -= pesoTempo[i];
            }
        }

        double custoTotal  = selecionados.stream().mapToDouble(l -> l.getCusto() != null ? l.getCusto() : 0).sum();
        double tempoTotal  = selecionados.stream().mapToDouble(l -> l.getTempoPermanencia() != null ? l.getTempoPermanencia() : 0).sum();

        log.info("Knapsack: {} lugares selecionados | custo=R${} / R${} | tempo={}h / {}h",
                selecionados.size(),
                String.format("%.1f", custoTotal),
                String.format("%.1f", orcamento),
                String.format("%.1f", tempoTotal),
                String.format("%.1f", tempoTotalH));

        return selecionados;
    }
}
