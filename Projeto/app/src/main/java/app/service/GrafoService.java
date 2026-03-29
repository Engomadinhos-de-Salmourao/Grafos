package app.service;

import app.domain.GrafoDestino;

public class GrafoService {


    //Se o TripAdvisor estiver muitoooooo difícil, opção de popular com dados mock para teste e implementar o resto
    public void cadastrarDestino(String nome){
        //scrapper responsavel por obter dados necessários
        //necessário passar no txt antes para saber o index do novo destino
    }

    public void removerDestino(String nome){
        //aceitar Grafo como parametro, se não houver grafo obtido será null e não será necessário remover do grafo
        //necessário passar no txt para remover o destino
        //pegar o grafo e remover todos os nós que tinham o index do destino deletado e suas arestas
    }

    public void adicionarLugar(GrafoDestino grafo){
        //Adicionar 1 ao numero de nos do grafo no txt
        //adicionar uma linha no txt com a descrição do novo lugar, o id dele deve ser a quantidade de nós atual -1
        //retornar um grafo novo com o lugar nele (passar pela verificação do destino e se eh nulo ou não o grafo)
    }

    public void removerLugar(GrafoDestino grafo){
        //remove 1 ao numero de nos do grafo no txt
        //remove uma linha no txt com a descrição do lugar
        //retornar um grafo novo sem o lugar nele (passar pela verificação do destino e se eh nulo ou não o grafo)
    }

    public void adicionarAresta(GrafoDestino destino, Integer v, Integer w){
        //Adicionar 1 ao numero de arestas do grafo no txt
        //adicionar uma linha no txt com a descrição da nova aresta
        //retornar um grafo novo com a aresta nele (passar pela verificação do destino e se eh nulo ou não o grafo)
    }

    public void removerAresta(GrafoDestino destino, Integer v, Integer w){
        //remover 1 ao numero de arestas do grafo no txt
        //remover uma linha no txt com a descrição da nova aresta
        //retornar um grafo novo sem a aresta nele (passar pela verificação do destino e se eh nulo ou não o grafo)
    }

    public GrafoDestino obterGrafo(){
        return new GrafoDestino(0);
    }

    public void showGrafo(GrafoDestino grafo){grafo.show();}

    public String conexidade(GrafoDestino destino){return (obterGrafo().conexidade() == 1)?"Conexo":"Não Conexo";}

    public void grafoReduzido(GrafoDestino grafo){

    }
}
