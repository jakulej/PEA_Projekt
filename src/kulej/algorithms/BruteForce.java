package kulej.algorithms;

import kulej.mainpackage.Graph;
//import org.jetbrains.annotations.NotNull;

import java.util.*;


public class BruteForce {
    int nodeCount;
    int[][] graph;

    public class Path {
        LinkedList<Integer> nodeLeft;
        LinkedHashSet<Integer> currentPath;

        public int getCurrentCost() {
            return currentCost;
        }

        int currentCost;

        public Path(Path path, int visitingNode, int currentNode) {

            this.nodeLeft = new LinkedList<>(path.nodeLeft);
            nodeLeft.remove((Object) visitingNode);
            this.currentPath = new LinkedHashSet<>(path.currentPath);
            currentPath.add(visitingNode);
            this.currentCost = path.currentCost;
            currentCost = currentCost + graph[currentNode][visitingNode];
        }
        public Path(int startNode){
            nodeLeft = new LinkedList<>();
            for (int i = 0; i < nodeCount; i++) {
                nodeLeft.add(i);
            }
            nodeLeft.remove((Object)startNode);
            currentPath = new LinkedHashSet<>();
            currentPath.add(startNode);
            currentCost = 0;
        }
        public void addCurrentCost(int value){
            this.currentCost+=value;
        }
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        public int getStartingNode(){
            return currentPath.stream().findFirst().get();
        }public LinkedHashSet<Integer> getCurrentPath() {
            return currentPath;
        }
        public int getCCost() {
            return currentCost;
        }
    }


    public BruteForce(Graph graph) {

        this.nodeCount = graph.getNodeCount();
        this.graph = graph.getAdjacencyMatrix();
    }

    public Path g(int node,Path path) {


        if(path.nodeLeft.isEmpty()) {
            path.addCurrentCost(graph[node][path.getStartingNode()]);
            return path;
        }

        Path[] cost = new Path[path.nodeLeft.size()];
        int j = 0;
        for (int nodeToGo:path.nodeLeft) {
            cost[j] = g(nodeToGo, new Path(path,nodeToGo,node));
            j++;
        }

        int lowestCostIndex = 0;
        for (int i = 0; i < path.nodeLeft.size(); i++) {
            if (cost[i].getCurrentCost() < cost[lowestCostIndex].getCurrentCost())
                lowestCostIndex = i;
        }
        return cost[lowestCostIndex];

    }
    public Path getSolution(){
        //return g(0,new Path(0)).currentPath.stream().mapToInt(i->i).toArray();
        return g(0,new Path(0));
    }

}
