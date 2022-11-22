package kulej.algorithms;

import kulej.mainpackage.Graph;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class BandB {
    int nodeCount;
    int[][] graph;
    int minimaliziedAmount;
    LinkedList<PathElement> checkedPaths = new LinkedList<PathElement>();
    public class PathElement extends kulej.mainpackage.Path{
        boolean isChecked = false;
        int[][] graphPath;
        int reduceCost;
        int currentNode;

        PathElement(int[][] graphPath, int reduceCost){
            currentNode = 0;
            this.graphPath = new int[nodeCount][nodeCount];
            System.arraycopy(this.graphPath, 0, graphPath, 0, nodeCount);
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
        PathElement(int[][] graphPath, int reduceCost, int start, int destination){
            currentNode = destination;
            this.graphPath = Arrays.copyOf(graphPath,graphPath.length);
            this.reduceCost = reduceCost;
            this.currentPath = new LinkedHashSet<Integer>();
            this.nodeLeft = new LinkedList<Integer>();
            this.nodeLeft.remove((Object)destination);
            this.currentPath.add(destination);
            fillInf(start,destination);
            minimalizeGraph(this);
            this.reduceCost += graphPath[start][destination];

        }
        private void fillInf( int start, int destination){
            for (int i = 0; i < nodeCount; i++) {
                this.graphPath[start][i] = Integer.MAX_VALUE;
                this.graphPath[i][destination] = Integer.MAX_VALUE;
            }
           this.graphPath[destination][start] = Integer.MAX_VALUE;
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
        PathElement bestPath;
        int lowerCost;
        checkedPaths.add(new PathElement(graph,0));
        minimalizeGraph(checkedPaths.get(0));
        this.graph = checkedPaths.get(0).graphPath;
        findMin(checkedPaths.getFirst());

        bestPath = checkedPaths.getLast();
        lowerCost = bestPath.reduceCost;

        for (PathElement path: checkedPaths) {
            if(!path.isChecked)
                checkPath(path,lowerCost);
        }

        for (PathElement path: checkedPaths) {
            if(path.nodeLeft.size()==0 && path.reduceCost<bestPath.reduceCost)
                bestPath = path;
        }
        return bestPath;
    }

    private void checkPath(PathElement pathElement, int lowerCost){
        int size = pathElement.nodeLeft.size();
        pathElement.isChecked = true;
        for (int i = 0; i < size; i++) {
            checkedPaths.add(new PathElement(graph, pathElement.reduceCost, 0, pathElement.nodeLeft.get(i)));
            if(checkedPaths.getLast().reduceCost<lowerCost && size>0){
                checkPath(checkedPaths.getLast(), lowerCost);
            }
        }
    }
    private void findMin(PathElement pathElement){
        int size = pathElement.nodeLeft.size();
        if(size>0) {
            int[] cost = new int[size];
            int smallestCostIndex = 0;
            for (int i = 0; i < size; i++) {
                checkedPaths.add(new PathElement(graph, pathElement.reduceCost, 0, pathElement.nodeLeft.get(i)));
                cost[i] = checkedPaths.getLast().reduceCost;
            }

            for (int i = 0; i < size; i++) {
                if (cost[i] < cost[smallestCostIndex])
                    smallestCostIndex = i;
            }
            int index = checkedPaths.size() - size + smallestCostIndex;
            checkedPaths.get(index).isChecked = true;
            findMin(checkedPaths.get(index));
        }
    }


    private void minimalizeGraph(PathElement pathElement){
        int minimalizedAmount;
        int minimalizedColumn;

        minimalizeRow(pathElement);
        minimalizeColumn(pathElement);

        /*for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                System.out.print(pathElement.graph[i][j]+" ");
            }
            System.out.print("\n");
        }*/
    }
    private void minimalizeRow(PathElement pathElement){
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
    }
    private void minimalizeColumn(PathElement pathElement){
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
    }


}
