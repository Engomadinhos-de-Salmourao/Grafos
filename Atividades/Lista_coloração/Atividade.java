package Lista_coloração;

import java.util.Arrays;
import Lista_coloração.Grafo.TGrafoND;

public class Atividade {
    public static void main(String[] args) {
        int[][] matriz = {
            {0, 0, 0, 0, 0, 1, 1, 0, 1},
            {0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 1, 0, 0, 1, 0, 0, 1, 0},
            {0, 0, 0, 0, 1, 1, 0, 1, 0},
            {0, 0, 1, 1, 0, 0, 1, 0, 0},
            {1, 0, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 1, 1},
            {0, 1, 1, 1, 0, 0, 1, 0, 0},
            {1, 0, 0, 0, 0, 1, 1, 0, 0}
        };

        int[][] matriz2 = {
            {0, 1, 0, 0, 1},
            {1, 0, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 0, 1},
            {1, 0, 0, 1, 0}
        };

        int[][] matriz3 = {
            {0, 1, 1, 0, 0, 0},
            {1, 0, 1, 1, 0, 0},
            {1, 1, 0, 1, 1, 0},
            {0, 1, 1, 0, 1, 1},
            {0, 0, 1, 1, 0, 1},
            {0, 0, 0, 1, 1, 0}
        };

        processMatrix("Matriz 1", matriz);
        processMatrix("Matriz 2", matriz2);
        processMatrix("Matriz 3", matriz3);
    }

    private static void processMatrix(String title, int[][] matriz) {
        System.out.println(title + ":");
        printMatriz(matriz);
        TGrafoND grafo = new TGrafoND(matriz.length);
        for (int i = 0; i < matriz.length; i++) {
            for (int j = i + 1; j < matriz.length; j++) {
                if (matriz[i][j] == 1) {
                    grafo.insertA(i, j);
                }
            }
        }
        System.out.println("Grafo criado a partir da matriz de adjacência.");
        grafo.coloracao();
        System.out.println();
    }

    private static void printMatriz(int[][] matriz) {
        for (int[] linha : matriz) {
            System.out.println(Arrays.toString(linha));
        }
    }
}
