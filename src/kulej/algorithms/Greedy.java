package kulej.algorithms;

import kulej.mainpackage.Path;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Greedy {


    public static Path resolve(int[][] graph, int nodeCount){
        Path path = new Path();
        path.nodeLeft = new LinkedList<>();
        path.currentPath = new LinkedHashSet<>();
        for (int i = 0; i < nodeCount; i++) {
            path.nodeLeft.add(i);
        }
        int actualNode = 0;
        int lowestCostNode = 1;
        while(!path.nodeLeft.isEmpty()){
            for (int node:path.nodeLeft) {
                if(graph[actualNode][node]<graph[actualNode][lowestCostNode])
                    lowestCostNode = node;
            }
            path.currentPath.add(lowestCostNode);
            path.nodeLeft.remove((Object)lowestCostNode);
            actualNode = lowestCostNode;
            if(!path.nodeLeft.isEmpty())
                lowestCostNode = path.nodeLeft.get(0);
        }


        return path;
    }
}
