package io.springdom.algoutil.graph;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("unused")
public class BFSToFindCycleInDirectedGraph {

    private BFSToFindCycleInDirectedGraph() {
        // Empty private constructor
    }

    /**
     * <b>Given a adjacency list of a Directed Unweighted Graph. Return {@code true} if there exists a cycle in the graph.</b>
     * <p>
     * <b></b>Example test case:</b>
     * <p>
     * <b>Input:</b> {{1, 3}, {2, 3, 4}, {0}, {}, {2, 5}, {}}
     * <p>
     * <b>Output:</b> true
     *
     * @param adjacencyList Adjacency list as 2D array.
     * @return {@code boolean}
     */
    public boolean cycleInGraph(int[][] adjacencyList) {
        int[] inDegree = new int[adjacencyList.length];
        for (int[] adjacent : adjacencyList) {
            for (int j : adjacent) {
                inDegree[j]++;
            }
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) queue.add(i);
        }
        int count = 0;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            count++;
            for (int j : adjacencyList[cur]) {
                inDegree[j]--;
                if (inDegree[j] == 0) queue.add(j);
            }
        }
        return count != adjacencyList.length;
    }

}
