package io.springdom.algoutil.graph;

@SuppressWarnings("unused")
public class DFSToFindCycleInDirectedGraph {

    private DFSToFindCycleInDirectedGraph() {
        // Empty private constructor
    }

    private static int[] visited;

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
    public static boolean cycleInGraph(int[][] adjacencyList) {
        visited = new int[adjacencyList.length];
        for (int i = 0; i < adjacencyList.length; i++) {
            if (visited[i] == 0 && dfs(adjacencyList, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean dfs(int[][] adjacencyList, int i) {
        if (visited[i] > 0) {
            return visited[i] == 2;
        }
        visited[i] = 2;
        for (int j : adjacencyList[i]) {
            if (dfs(adjacencyList, j)) {
                return true;
            }
        }
        visited[i] = 1;
        return false;
    }

}
