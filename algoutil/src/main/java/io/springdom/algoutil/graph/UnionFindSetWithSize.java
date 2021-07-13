package io.springdom.algoutil.graph;

@SuppressWarnings("unused")
public class UnionFindSetWithSize {
    // disjoint-set to perform union find.
    private final int[] parent;
    // size of the given set.
    private final int[] size;

    public UnionFindSetWithSize(int size) {
        parent = new int[size];
        this.size = new int[size];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;  // self representation of disjoint-set.
            this.size[i] = 1;    // initial size to 1.
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
        if (xParent == yParent)
            return;
        // yParent having a greater size of the tree.
        if (size[yParent] > size[xParent]) {
            parent[xParent] = yParent;
            size[yParent] += size[xParent];
        }
        else {
            parent[yParent] = xParent;
            size[xParent] += size[yParent];
        }
    }

    public int getSetSize(int index) {
        index = find(index);
        return size[index];
    }
}
