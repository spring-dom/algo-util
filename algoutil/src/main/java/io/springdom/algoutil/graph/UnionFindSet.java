package io.springdom.algoutil.graph;

@SuppressWarnings("unused")
public class UnionFindSet {
    // disjoint-set to perform union find.
    private final int[] parent;
    // rank to improve the time complexity by doing union based on rank. basically rank == height of our disjoint set.
    private final int[] rank;

    public UnionFindSet(int size) {
        parent = new int[size];
        rank = new int[size];
        for (int i=0; i<parent.length; i++) {
            parent[i] = i;  // self representation of disjoint-set.
            rank[i] = 0;    // initial rank is 0.
        }
    }

    public int find(int a) {
        if (a == parent[a]) // roots always have self representation in disjoint-set.
            return a;
        parent[a] = find(parent[a]); // path compression for alpha(n) time complexity.
        return parent[a];
    }

    public void union(int x, int y) {
        int xParent = find(x);
        int yParent = find(y);
        // yParent having a greater height of the tree. then xParent's parent will become yParent.
        if (rank[yParent] > rank[xParent]) {
            parent[xParent] = yParent;
        }
        // yParent having a greater height of the tree. then yParent's parent will become xParent.
        else if (rank[xParent] > rank[yParent]) {
            parent[yParent] = xParent;
        }
        // yParent's height == xParent's height. So add as per our choice and increase the height by 1.
        else {
            parent[yParent] = xParent;
            rank[xParent]++;
        }
    }
}
