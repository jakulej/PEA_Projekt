package kulej.algorithms;

import kulej.mainpackage.Graph;

import java.util.*;
import java.util.stream.IntStream;

public class BandB {
    int nodeCount;
    int[][] graph;
    PriorityQueue<PathElement> queue = new PriorityQueue<PathElement>();
    public class PathElement extends kulej.mainpackage.Path implements Comparable<PathElement>{
        boolean isChecked = false;
        int[][] graphPath;
        int reduceCost;
        int currentNode;



        PathElement(int[][] graphPath, int reduceCost){
            currentNode = 0;
            this.graphPath = new int[nodeCount][nodeCount];
            for (int i = 0; i < nodeCount; i++) {
                this.graphPath[i] = Arrays.copyOf(graphPath[i],nodeCount);
            }
            this.reduceCost = reduceCost;
            this.currentPath = new LinkedHashSet<Integer>();
            this.nodeLeft = new LinkedList<Integer>();
            this.isChecked = true;
            for (int i = 0; i < nodeCount; i++) {
                this.nodeLeft.add(i);
            }
            // 0 wierzchołek startowy

            this.nodeLeft.remove((Object) 0);
            this.currentPath.add(0);

        }
        PathElement(int[][] graphPath, int reduceCost, int start, int destination, PathElement pathElement){
            currentNode = destination;
            this.graphPath = new int[nodeCount][nodeCount];
            for (int i = 0; i < nodeCount; i++) {
                this.graphPath[i] = Arrays.copyOf(graphPath[i],nodeCount);
            }
            this.reduceCost = reduceCost;
            this.currentPath = new LinkedHashSet<Integer>(pathElement.currentPath);
            this.nodeLeft = new LinkedList<Integer>(pathElement.nodeLeft);
            this.nodeLeft.remove((Object)destination);
            this.currentPath.add(destination);
            fillInf(start,destination, this.graphPath);
            this.reduceCost += minimalizeGraph(this);
            this.reduceCost += graph[start][destination];

        }

        @Override
        public int compareTo(PathElement o) {
            return Integer.compare(this.reduceCost,o.reduceCost);
        }
    }


    public BandB(Graph graph){
        this.graph = graph.getAdjacencyMatrix();
        this.nodeCount = graph.getNodeCount();
        for (int i = 0; i < nodeCount; i++) {
            this.graph[i][i] = Integer.MAX_VALUE;
        }
    }


    public PathElement resolve(){
        long time = System.nanoTime();
        PathElement pathElement = new PathElement(graph,0);
        minimalizeGraph(pathElement);
        for (int i = 0; i < nodeCount; i++) {
            this.graph[i] = Arrays.copyOf(pathElement.graphPath[i],nodeCount);
        }

       do{
           for (int node:pathElement.nodeLeft) {
               queue.add(new PathElement(pathElement.graphPath, pathElement.reduceCost, pathElement.currentNode,node, pathElement));
           }
           pathElement = queue.poll();
           if(time - System.nanoTime()> 120000000000L){
                return null;
           }
       }while(pathElement.nodeLeft.size()>0);


        return pathElement;
    }


    private void fillInf(int start, int destination, int[][]graphToFill){
        for (int i = 0; i < nodeCount; i++) {
            graphToFill[start][i] = Integer.MAX_VALUE;
            graphToFill[i][destination] = Integer.MAX_VALUE;
        }
        graphToFill[destination][start] = Integer.MAX_VALUE;
    }
    private int minimalizeGraph(PathElement pathElement){
        int minimalizedAmount;
        int minimalizedColumn;

        minimalizedAmount = minimalizeRow(pathElement);
        minimalizedColumn =minimalizeColumn(pathElement);
        return minimalizedAmount+minimalizedColumn;
    }
    private int minimalizeRow(PathElement pathElement){
        int[] minInRow = new int[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            minInRow[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < nodeCount; i++) {
            if(minInRow[i]> 500)
                minInRow[i] = 0;
        }

        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if(pathElement.graphPath[i][j]<minInRow[i]){
                    minInRow[i] = pathElement.graphPath[i][j];
                }
            }
        }
        pathElement.reduceCost += IntStream.of(minInRow).sum();
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                pathElement.graphPath[i][j] -= minInRow[i];
            }
        }
        return Arrays.stream(minInRow).sum();
    }
    private int minimalizeColumn(PathElement pathElement){
        int[] minInColumn = new int[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            minInColumn[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < nodeCount; i++) {
            if(minInColumn[i]> 500)
                minInColumn[i] = 0;
        }

        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if(pathElement.graphPath[j][i]<minInColumn[i]){
                    minInColumn[i] = pathElement.graphPath[j][i];
                }
            }
        }
        pathElement.reduceCost = IntStream.of(minInColumn).sum();
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                pathElement.graphPath[j][i] -= minInColumn[i];
            }
        }
        return Arrays.stream(minInColumn).sum();
    }
    public static int getCost(int[][] graph, int[] path){
        int cost = 0;
        for (int i = 1; i < graph.length; i++) {
            cost+= graph[path[i-1]][path[i]];
        }
        cost += graph[path[graph.length-1]][path[0]];
        return cost;
    }


}
