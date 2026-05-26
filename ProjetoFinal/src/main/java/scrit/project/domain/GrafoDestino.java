package scrit.project.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Grafo não-direcionado representado por lista de adjacência.
 * Todos os índices internos (adj[]) são base-0.
 * Os IDs de Lugar no mapa 'lugares' são base-1.
 */
@Getter
public class GrafoDestino {

    private int n;   // número de vértices
    private int m;   // número de half-edges (cada aresta não-dir. conta 2)
    private Tno[] adj;

    @Setter
    private Map<Integer, Lugar> lugares;

    public GrafoDestino(int n) {
        this.n      = n;
        this.m      = 0;
        this.adj    = new Tno[n];
        this.lugares = new HashMap<>();
    }

    // ─── Conexidade ──────────────────────────────────────────────────────────

    /**
     * Retorna 1 se o grafo é conexo, 0 se não.
     * Um grafo vazio (n=0) é considerado "trivialmente conexo" (retorna 1).
     */
    public int conexidade() {
        if (n == 0) return 1;
        List<Integer> visitados = largura(0);
        return (visitados.size() == n) ? 1 : 0;
    }

    public List<Integer> largura(int v) {
        List<Integer> visitados = new ArrayList<>();
        Queue<Integer> fila = new LinkedList<>();

        visitados.add(v);
        fila.add(v);

        while (!fila.isEmpty()) {
            int    atual = fila.remove();
            Tno    aux   = adj[atual];
            while (aux != null) {
                if (!visitados.contains(aux.getLugar())) {
                    fila.add(aux.getLugar());
                    visitados.add(aux.getLugar());
                }
                aux = aux.getProximo();
            }
        }
        return visitados;
    }

    // ─── Inserção e remoção de arestas ───────────────────────────────────────

    /**
     * Insere aresta não-direcionada entre v e w (ambos base-0).
     * Ignora silenciosamente se a aresta já existe.
     */
    public void insereA(Integer v, Integer w, Float dist, Float tempo) {
        if (v < 0 || w < 0 || v >= n || w >= n || v.equals(w)) return;

        Tno no  = adj[v], ant  = null;
        Tno no2 = adj[w], ant2 = null;

        // Verifica se já existe antes de inserir (v→w)
        while (no != null && w >= no.getLugar()) {
            if (w.equals(no.getLugar())) return; // duplicata
            ant = no;
            no  = no.getProximo();
        }
        // Verifica (w→v)
        while (no2 != null && v >= no2.getLugar()) {
            if (v.equals(no2.getLugar())) return; // duplicata
            ant2 = no2;
            no2  = no2.getProximo();
        }

        Tno novoNo  = new Tno();
        novoNo.setLugar(w);
        novoNo.setDist(dist);
        novoNo.setTempoDeslocamento(tempo);
        novoNo.setProximo(no);

        Tno novoNo2 = new Tno();
        novoNo2.setLugar(v);
        novoNo2.setDist(dist);
        novoNo2.setTempoDeslocamento(tempo);
        novoNo2.setProximo(no2);

        if (ant  == null) adj[v] = novoNo;  else ant.setProximo(novoNo);
        if (ant2 == null) adj[w] = novoNo2; else ant2.setProximo(novoNo2);

        m += 2;
    }

    public void removeA(int v, int w) {
        Tno no = adj[v], ant = null;
        while (no != null && no.getLugar() != w) { ant = no; no = no.getProximo(); }

        Tno no2 = adj[w], ant2 = null;
        while (no2 != null && no2.getLugar() != v) { ant2 = no2; no2 = no2.getProximo(); }

        if (no != null && no2 != null) {
            if (ant  == null) adj[v] = no.getProximo();  else ant.setProximo(no.getProximo());
            if (ant2 == null) adj[w] = no2.getProximo(); else ant2.setProximo(no2.getProximo());
            no.setProximo(null);
            no2.setProximo(null);
            m -= 2;
        } else {
            System.out.println("Aresta não encontrada entre " + v + " e " + w);
        }
    }

    // ─── Inserção e remoção de vértices ──────────────────────────────────────

    /**
     * Adiciona um novo vértice ao final do grafo.
     * O ID do lugar no mapa é (n_antigo + 1), base-1.
     * Se o lugar já tem um ID definido, usa-o; caso contrário, atribui automaticamente.
     */
    public void insereV(Lugar lugar) {
        Tno[] aux = new Tno[n + 1];
        System.arraycopy(adj, 0, aux, 0, n);
        int novoId = n + 1; // base-1
        n++;
        adj = aux;

        if (lugar.getId() == null) {
            lugar.setId(novoId);
        }
        lugares.put(novoId, lugar);
    }

    /**
     * Remove o vértice de ID v (base-1).
     * Reindexação: todos os IDs e referências de arestas são ajustados.
     */
    public void removeV(int v) {
        if (v < 1 || v > n) {
            System.out.println("ID " + v + " fora do intervalo válido (1–" + n + ").");
            return;
        }
        int pos = v - 1; // converte para índice base-0

        // Remove todas as arestas do vértice
        while (adj[pos] != null) {
            removeA(pos, adj[pos].getLugar());
        }

        // Reconstrói adj e lugares sem o vértice removido
        Tno[]            novoAdj    = new Tno[n - 1];
        Map<Integer, Lugar> novosMapa = new HashMap<>();

        int novoIdx = 0;
        for (int i = 0; i < n; i++) {
            if (i == pos) continue;
            novoAdj[novoIdx] = adj[i];
            Lugar l = lugares.get(i + 1); // i+1 = id base-1 antigo
            if (l != null) {
                l.setId(novoIdx + 1);     // novo id base-1
                novosMapa.put(novoIdx + 1, l);
            }
            novoIdx++;
        }

        // Corrige referências de arestas que apontavam para índices > pos
        for (Tno no : novoAdj) {
            Tno aux = no;
            while (aux != null) {
                if (aux.getLugar() > pos) aux.setLugar(aux.getLugar() - 1);
                aux = aux.getProximo();
            }
        }

        adj    = novoAdj;
        lugares = novosMapa;
        n--;
    }

    // ─── Consultas ───────────────────────────────────────────────────────────

    public Lugar getLugarById(int id) {
        return lugares.get(id);
    }

    public Tno getAresta(int v, int w) {
        if (v < 0 || v >= n) return null;
        Tno no = adj[v];
        while (no != null) {
            if (no.getLugar() == w) return no;
            no = no.getProximo();
        }
        return null;
    }

    public List<Integer> vizinhos(int v) {
        List<Integer> lista = new ArrayList<>();
        if (v < 0 || v >= n) return lista;
        Tno no = adj[v];
        while (no != null) { lista.add(no.getLugar()); no = no.getProximo(); }
        return lista;
    }

    // ─── Debug ───────────────────────────────────────────────────────────────

    public void show() {
        System.out.printf("n: %d%n", n);
        System.out.printf("m: %d%n", m / 2);
        for (int i = 0; i < n; i++) {
            Lugar l = lugares.get(i + 1);
            System.out.printf("%n[%d] %s: ", i + 1, l != null ? l.getNome() : "?");
            Tno no = adj[i];
            while (no != null) {
                System.out.printf("(→%d dist:%.1f tempo:%.1f) ",
                        no.getLugar() + 1, no.getDist(), no.getTempoDeslocamento());
                no = no.getProximo();
            }
        }
        System.out.println("\n\nfim da impressão do grafo.");
    }
}
