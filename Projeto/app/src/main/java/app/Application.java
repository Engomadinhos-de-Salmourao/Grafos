package app;

import app.domain.Lugar;
import app.domain.TypeLugar;
import app.service.GrafoService;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

public class Application {
    public static void main(String[] args) throws IOException {
        boolean running = true;
        Scanner s = new Scanner(System.in);
        GrafoService gs = new GrafoService();

        while(running){
            System.out.println("\n========== MENU ==========");
            System.out.println("1) Ler dados do arquivo grafo.txt");
            System.out.println("2) Gravar dados no arquivo grafo.txt");
            System.out.println("3) Inserir vértice");
            System.out.println("4) Inserir aresta");
            System.out.println("5) Remove vértice");
            System.out.println("6) Remove aresta");
            System.out.println("7) Mostrar conteúdo do arquivo");
            System.out.println("8) Mostrar grafo");
            System.out.println("9) Apresentar a conexidade do grafo e o reduzido");
            System.out.println("10) Encerrar a aplicação");
            System.out.print("Escolha uma opção: ");
            int opcao = s.nextInt();
            System.out.println("\n\n\n");

            switch (opcao) {
                case 1:
                   gs.obterGrafo();
                    break;
                case 2:
                    if (gs.grafo == null) {
                        System.out.println("Carregue o grafo antes de inserir um vértice.");
                    } else {
                        gs.gravar();
                    }
                    break;
                case 3:
                    if (gs.grafo == null) {
                        System.out.println("Carregue o grafo antes de inserir um vértice.");
                    } else {
                        Lugar novoLugar = lerLugar(s, gs.grafo.getN()+1);
                        gs.adicionarLugar(novoLugar);
                        System.out.println("Vértice inserido com sucesso.");
                    }
                    break;
                case 4:
                    if (gs.grafo == null) {
                        System.out.println("Carregue o grafo antes de inserir uma aresta.");
                    } else {
                        System.out.println("Quantidade de lugares: " + gs.grafo.getN());

                        System.out.print("Id do lugar de origem: ");
                        int origem = s.nextInt();

                        System.out.print("Id do lugar de destino: ");
                        int destino = s.nextInt();

                        System.out.print("Distância: ");
                        float dist = s.nextFloat();

                        System.out.print("Tempo de deslocamento: ");
                        float tempo = s.nextFloat();
                        s.nextLine();

                        gs.adicionarAresta(origem, destino, dist, tempo);
                        System.out.println("Aresta inserida com sucesso.");
                    }
                    break;
                case 5:
                    if (gs.grafo == null) {
                        System.out.println("Carregue o grafo antes de remover um vértice.");
                    } else {
                        System.out.println("Quantidade de lugares: " + gs.grafo.getN());
                        System.out.print("Id do vértice a remover: ");
                        int v = s.nextInt();
                        s.nextLine();

                        gs.removerLugar(v);
                        System.out.println("Vértice removido com sucesso.");
                    }
                    break;
                case 6:
                    if (gs.grafo == null) {
                        System.out.println("Carregue o grafo antes de remover uma aresta.");
                    } else {
                        System.out.println("Quantidade de lugares: " + gs.grafo.getN());

                        System.out.print("Id do lugar de origem: ");
                        int origem = s.nextInt();

                        System.out.print("Id do lugar de destino: ");
                        int destino = s.nextInt();
                        s.nextLine();

                        gs.removerAresta(origem, destino);
                        System.out.println("Aresta removida com sucesso.");
                    }
                    break;
                case 7:
                    gs.mostrarConteudo();
                    break;
                case 8:
                    if (gs.grafo == null) {
                        System.out.println("Nenhum grafo carregado.");
                    } else {
                        gs.showGrafo();
                    }
                    break;
                case 9:
                    if (gs.grafo == null) {
                        System.out.println("Nenhum grafo carregado.");
                    } else {
                        System.out.println("Conexidade do grafo: " + gs.conexidade());
                    }
                    break;
                case 10:
                    gs.gravar();
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public static Lugar lerLugar(Scanner sc, int proximoId) {
        Lugar lugar = new Lugar();

        lugar.setId(proximoId);

        System.out.println("\n=== Inserção de novo lugar ===");
        System.out.println("Id atribuído automaticamente: " + proximoId);

        System.out.print("Id do destino: ");
        lugar.setId_destino(sc.nextInt());
        sc.nextLine();

        System.out.print("Nome: ");
        lugar.setNome(sc.nextLine());

        System.out.print("Descrição: ");
        lugar.setDescription(sc.nextLine());

        System.out.print("Custo: ");
        lugar.setCusto(sc.nextDouble());

        System.out.print("Tempo de permanência: ");
        lugar.setTempo(sc.nextFloat());

        System.out.print("Score: ");
        lugar.setScore(sc.nextFloat());

        System.out.print("Número de reviews: ");
        lugar.setNum_reviews(sc.nextInt());
        sc.nextLine();

        System.out.println("Tipo do lugar:");
        System.out.println("1 - HOTEL");
        System.out.println("2 - RESTAURANTE");
        System.out.println("3 - PONTO_TURISTICO");
        System.out.print("Escolha: ");
        int tipo = sc.nextInt();
        sc.nextLine();

        switch (tipo) {
            case 1:
                lugar.setTipo(TypeLugar.HOTEL);
                lugar.setTempo(0f);
                break;
            case 2:
                lugar.setTipo(TypeLugar.RESTAURANTE);
                break;
            case 3:
                lugar.setTipo(TypeLugar.PONTO_TURISTICO);
                break;
            default:
                throw new IllegalArgumentException("Tipo inválido.");
        }

        lugar.setHorarios(lerHorarios(sc));

        return lugar;
    }

    public static List<Set<Time>> lerHorarios(Scanner sc) {
        List<Set<Time>> horarios = new ArrayList<>();


        for (int i = 0; i <7; i++) {
            Set<Time> conjunto = new LinkedHashSet<>();

            System.out.println("Conjunto " + (i + 1) + ":");

            System.out.print("Horário de abertura (HH:MM:SS): ");
            Time abertura = Time.valueOf(sc.nextLine());

            System.out.print("Horário de fechamento (HH:MM:SS): ");
            Time fechamento = Time.valueOf(sc.nextLine());

            conjunto.add(abertura);
            conjunto.add(fechamento);

            horarios.add(conjunto);
        }

        return horarios;
    }

}
