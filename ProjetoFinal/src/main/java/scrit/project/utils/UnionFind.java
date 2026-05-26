package scrit.project.utils;


public class UnionFind {

    private final int[] pai;
    private final int[] rank;

    public UnionFind(int n) {
        pai  = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) pai[i] = i;
    }

    
    public int find(int x) {
        if (pai[x] != x) pai[x] = find(pai[x]);
        return pai[x];
    }

    
    public boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false; // já conectados → formaria ciclo
        if (rank[rx] < rank[ry]) { int tmp = rx; rx = ry; ry = tmp; }
        pai[ry] = rx;
        if (rank[rx] == rank[ry]) rank[rx]++;
        return true;
    }

    
    public boolean conectados(int x, int y) {
        return find(x) == find(y);
    }
}
