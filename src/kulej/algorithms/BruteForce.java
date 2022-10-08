package kulej.algorithms;

import kulej.mainpackage.Graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;



public class BruteForce {



    int[] path;
    int nodeCount;
    int[][] graph;
    LinkedList<Integer> nodeLeft;

    public BruteForce(int nodeCount, Graph graph){

        this.nodeCount = nodeCount;
        path = new int[nodeCount];
        fillTable(-1,path);
        this.graph = graph.getAdjacencyMatrix();
    };

    public int g(int node, LinkedList<Integer> nodeLeft){

        int[] cost = new int[nodeLeft.size()];
        for(int i =0;i<nodeLeft.size();i++){
            cost[i] =  graph[node][nodeLeft.get(i)] + g(nodeLeft.get(i),new LinkedList<Integer>(Collections.singleton(nodeLeft.remove(i))));
        }
        IntSummaryStatistics stat = Arrays.stream(cost).summaryStatistics();

        nodeLeft.indexOf(stat.getMin());//

        return stat.getMin();

    }

    private static void fillTable(int number, int[] table){
        for (int i=0;i<table.length;i++){
            table[i] = number;
        }
    }
}
