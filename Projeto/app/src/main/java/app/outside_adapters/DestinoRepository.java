package app.outside_adapters;

import app.domain.Destino;
import app.ports.DestinoRepositotyPort;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DestinoRepository implements DestinoRepositotyPort {
    private final String path = "./files/destinos.txt";

    @Override
    public void salvarDestino(String nome) throws IOException {
        int quantidade = this.getQuantidade(this.path);
        alterarLinha(this.path, 0,  quantidade+ 1);
        adicionarNoFinal(this.path, nome, quantidade+1);
    }

    @Override
    public void removerdestino(int id) throws IOException {
        try {
            Path path = Path.of(this.path);
            List<String> linhas = Files.readAllLines(path);
            for(String linha : linhas){
                if(!Objects.equals(linha, linhas.getFirst())){
                    String[] result = linha.split(";");
                    int id_linha = Integer.parseInt(result[0]);
                    if(id_linha == id){
                        linhas.remove(linha);
                        break;
                    }
                }
            }
            Files.write(path, linhas);

        } catch (IOException e) {
            System.out.println("Erro ao alterar: " + e.getMessage());
        }

        alterarLinha(this.path, 0, getQuantidade(this.path)-1);
    }

    @Override
    public List<Destino> getAll() {
        List<Destino> lista = new ArrayList<>();
        try {
            Path path = Path.of(this.path);
            List<String> linhas = Files.readAllLines(path);

            for(String linha : linhas){
                if(!Objects.equals(linha, linhas.getFirst())){
                    String[] result = linha.split(";");
                    int id_linha = Integer.parseInt(result[0]);
                    String nome = result[1];
                    lista.add(new Destino(id_linha, nome));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao alterar: " + e.getMessage());
        }
        return lista;
    }

    public static void adicionarNoFinal(String caminho, String nome, int quantidade) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminho, true))) {
            writer.newLine();
            writer.write(nome);
        } catch (IOException e) {
            System.out.println("Erro ao adicionar: " + e.getMessage());
        }
    }

    public static void alterarLinha(String caminho, int indx, int value) throws IOException {
        try {
            Path path = Path.of(caminho);
            List<String> linhas = Files.readAllLines(path);
            linhas.set(indx, String.valueOf(value));
            Files.write(path, linhas);
        } catch (IOException e) {
            System.out.println("Erro ao alterar: " + e.getMessage());
        }
    }

    public int getQuantidade(String caminho){
        try {
            Path path = Path.of(caminho);
            List<String> linhas = Files.readAllLines(path);
            return(Integer.parseInt(linhas.getFirst()));
        } catch (IOException e) {
            System.out.println("Erro ao alterar: " + e.getMessage());
        }
        return 0;
    }
}
