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
    int[] bestPath;
    int bestPathCost;

    public Annealing(double cooler, long time,int[][] graph, int nodeCount) {
        this.cooler = cooler;
        this.time = time;
        this.graph = graph;
        this.nodeCount = nodeCount;
        this.period = this.nodeCount * 20;
    }

    public int[] resolve(){
        int timeIterator = 0;
        long startTime;
        int[] currentPath = Greedy.resolve(graph,nodeCount).currentPath.stream().mapToInt(i->i).toArray();
        this.bestPath = currentPath;
        this.bestPathCost = getPathCost(this.bestPath);
        this.temperature = this.nodeCount * 100000000;
        int[] nextPath;

        startTime = System.nanoTime();
        while (time>System.nanoTime()-startTime) {

            for (int i = 0; i < period; i++) {
                nextPath = randomNextPath(currentPath);
                if(getPathCost(nextPath)<bestPathCost)
                    bestPath = nextPath;
                    bestPathCost = getPathCost(bestPath);
                if (doSwap(currentPath, nextPath))
                    currentPath = nextPath;
                if(System.nanoTime()-startTime>(10000000000L * timeIterator)){
                    System.out.println("wynik dla " +timeIterator+"0 sek = " + bestPathCost);
                    timeIterator++;
                }
            }
            actualizeTemp();
        }
        System.out.println("Temperatura koncowa: "+this.temperature);
        return bestPath;
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
    public int getPathCost(int[] path){
        int cost = 0;
        for (int i = 1; i < nodeCount; i++) {
            cost += graph[path[i-1]][path[i]];
        }
        return cost;
    }
    private double calculateProbability(int currentPathCost, int worstPathCost){
        return Math.exp((currentPathCost-worstPathCost)/this.temperature);
    }
    private void actualizeTemp(){
        temperature = temperature * cooler;
    }
    private boolean doSwap(int[] currentPath, int[] nextPath){
        Random random = new Random();
        int currentPathCost, nextPathCost;

        currentPathCost = getPathCost(currentPath);
        nextPathCost = getPathCost(nextPath);
        if(nextPathCost<currentPathCost){
            return true;
        }
        double prob = calculateProbability(currentPathCost,nextPathCost);
        double rand =random.nextDouble();
        if(rand>prob)
            return true;
        else
            return false;

    }
}
