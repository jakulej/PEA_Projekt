package kulej.algorithms;

import kulej.mainpackage.Graph;
import kulej.mainpackage.Path;

import java.util.Arrays;
import java.util.Random;

public class Annealing {
    double temperature;
    double cooler;
    long time;
    int period;
    int nodeCount;
    int[][] graph;

    public Annealing(int temperature, double cooler, long time,int period, int[][] graph, int nodeCount) {
        this.temperature = temperature;
        this.cooler = cooler;
        this.time = time;
        this.period = period;
        this.graph = graph;
        this.nodeCount = nodeCount;
    }

    public int[] resolve(){
        long actualTime;
        int[] currentPath = Greedy.resolve(graph,nodeCount).currentPath.stream().mapToInt(i->i).toArray();
        int[] nextPath;

        actualTime = System.nanoTime();
        while (time>System.nanoTime()-actualTime) {
            for (int i = 0; i < period; i++) {
                nextPath = randomNextPath(currentPath);
                if (doSwap(currentPath, nextPath))
                    currentPath = nextPath;
            }
            actualizeTemp();
        }
        return currentPath;
    }
    private int[] randomNextPath(int[] path){
        Random random = new Random();
        int[] newPath = Arrays.copyOf(path,nodeCount);
        int first, second;

        first = random.nextInt(nodeCount);

        do {
            second = random.nextInt(nodeCount);
        }while (first==second);

        newPath[first] = path[second];
        newPath[second] = path[first];

        return newPath;
    }
    private int getPathCost(int[] path){
        int cost = 0;
        for (int i = 1; i < nodeCount; i++) {
            cost += graph[path[i-1]][path[i]];
        }
        return cost;
    }
    private double calculateProbability(int currentPathCost, int worstPathCost){
        return Math.exp((currentPathCost-worstPathCost)/temperature);
    }
    private void actualizeTemp(){
        temperature = temperature * cooler;
    }
    private boolean doSwap(int[] currentPath, int[] nextPath){
        Random random = new Random();
        int currentPathCost, nextPathCost;

        currentPathCost = getPathCost(currentPath);
        nextPathCost = getPathCost(nextPath);

        if(nextPathCost<currentPathCost)
            return true;
        double prob = calculateProbability(currentPathCost,nextPathCost);
        double rand =random.nextDouble();
        if(rand>prob)
            return true;
        else
            return false;

    }
}
