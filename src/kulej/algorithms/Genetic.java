package kulej.algorithms;

import java.util.*;

public class Genetic {
    int[][] graph;
    int nodeCount;
    int populationSize;
    double crossoverRatio = 0.8;
    double mutationRatio = 0.01;
    Mutation mutation;
    Path bestSolution;
    int selectionSize = 20;
    ArrayList<Path> population;


    long timeLimit;
    public int[] resolve(){

        population = new ArrayList<>();
        population = generatePopulation();
        long start = System.nanoTime();
        bestSolution = population.get(0);
        do {
            geneticLoop(start);
        }while(System.nanoTime()-start<timeLimit);

       return bestSolution.path;
    }
    private void geneticLoop(long start){
        Random random = new Random();
        ArrayList<Path> selected;
        PriorityQueue<Path> populationToSelection = new PriorityQueue<Path>(Comparator.reverseOrder());

        populationToSelection = getPopulationToSelection();
        selected = selection(populationToSelection);
        checkIsBest(selected.get(0),selected.get(1),start);

        //Krzyzowanie
        if(random.nextDouble()<crossoverRatio) {
            Crossover.PMX.crossover(selected.get(0), selected.get(1));
            checkIsBest(selected.get(0),selected.get(1),start);
        }

        //Mutacja
        if(random.nextDouble()<mutationRatio) {
            mutation.mutate(selected.get(0));
            checkIsBest(selected.get(0),selected.get(1),start);
        }
        if(random.nextDouble()<mutationRatio){
            mutation.mutate(selected.get(1));
            checkIsBest(selected.get(0), selected.get(1), start);
        }
    }
    double getFitnessScore(int[] path){
        double summary = 0;
        for (int i = 1; i < nodeCount; i++) {
            summary+= graph[path[i-1]][path[i]];
        }
        summary+=graph[path[nodeCount-1]][path[0]];
        return 1/summary;
    }
    ArrayList<Path> generatePopulation(){
        while (population.size()<populationSize){
            population.add(new Path());
        }
        return population;
    }
    private void checkIsBest(Path path1, Path path2, long start){
        float time = (float)(System.nanoTime()-start)/(float)100000000L;
        if(path1.fitnessScore>bestSolution.fitnessScore){
            bestSolution = new Path(path1);
            System.out.println("Wynik dla "+time + "0 sek = " + calculateCost(bestSolution.path, this.graph));
        } else if (path2.fitnessScore>bestSolution.fitnessScore) {
            bestSolution = new Path(path2);
            System.out.println("Wynik dla "+time + "0 sek = " + calculateCost(bestSolution.path, this.graph));

        }
    }
    private void printNewBestPath(long start){

    }
    int[] randomPath(){
        ArrayList nodeLeft = new ArrayList<>();
        Random random = new Random();
        int[] path = new int[nodeCount];

        for (int i = 0; i < nodeCount; i++) {
            nodeLeft.add(i);
        }
        for (int i = 0; i < nodeCount; i++) {
            path[i] = (int) nodeLeft.remove(random.nextInt(nodeCount-i));
        }
        return path;
    }
    ArrayList<Path> selection(PriorityQueue<Path> populationToSelection){
        ArrayList<Path> selected = new ArrayList<>();

        selected.add(populationToSelection.poll());
        selected.add(populationToSelection.poll());

        return selected;
    }
    PriorityQueue<Path> getPopulationToSelection(){
        PriorityQueue<Path> populationToSelection = new PriorityQueue<Path>(Comparator.reverseOrder());
        Random random = new Random();

        while(populationToSelection.size()<selectionSize)
            populationToSelection.add(population.get(random.nextInt(populationSize)));
        return populationToSelection;
    }
    Path getWorsePath(Path path1, Path path2){
        if (path1.fitnessScore< path2.fitnessScore){
            return path1;
        }
        else
            return path2;
    }
    public static int calculateCost(int[] path, int[][] graph){
        int cost = 0 ;
        for (int i = 0; i < graph.length; i++) {
            cost += graph[path[i-1]][path[i]];
        }
        cost += graph[path.length-1][path[0]];
        return cost;
    }
    private class Path implements Comparable<Path>{
        int[] path;
        double fitnessScore;

        public Path(){
            this.path = randomPath();
            fitnessScore = getFitnessScore(this.path);
        }
        public Path(Path path){
            this.path = new int[nodeCount];
            System.arraycopy(path.path,0,this.path,0,nodeCount);
            this.fitnessScore = path.fitnessScore;
        }
        public void printPath(){
            for (int i = 0; i < path.length; i++) {
                System.out.print(path[i] + " -> ");
            }
                System.out.print(path[0]+"\n");
        }
        public void actualizeFitnessScore(){
            this.fitnessScore = getFitnessScore(this.path);
        }

        @Override
        public int compareTo(Path o) {
            if(this.fitnessScore>o.fitnessScore)
                return 1;
            else if (this.fitnessScore<o.fitnessScore) {
                return -1;
            }
            else
                return 0;
        }
    }
    private enum Crossover {

        PMX(){
            @Override
            public void crossover(Genetic.Path path1, Genetic.Path path2){
                Random random = new Random();
                int begin, end;
                HashMap<Integer, Integer> map1 = new HashMap<>();
                HashMap<Integer, Integer> map2 = new HashMap<>();

                //Losowanie miejsca cięcia
                begin = random.nextInt(path1.path.length);
                do {
                    end = random.nextInt(path1.path.length);
                }while(begin==end);
                if(begin>end){
                    int temp = begin;
                    begin = end;
                    end = temp;
                }
                int lenght = end-begin;
                int[] cut = new int[lenght];

                //Mapowanie
                for (int i = begin; i < end; i++) {
                    map1.put(path2.path[i],path1.path[i]);
                    map2.put(path1.path[i],path2.path[i]);
                }

                //Ciecie
                System.arraycopy(path1.path,begin,cut,0,lenght);
                System.arraycopy(path2.path,begin,path1.path,begin,lenght);
                System.arraycopy(cut,0,path2.path,begin,lenght);

                //Naprawianie
                for (int i = 0; i < begin; i++) {
                    while(map1.containsKey(path1.path[i]))
                        path1.path[i] = map1.get(path1.path[i]);
                    while(map2.containsKey(path2.path[i]))
                        path2.path[i] = map2.get(path2.path[i]);
                }
                for (int i = end; i < path1.path.length; i++) {
                    while(map1.containsKey(path1.path[i]))
                        path1.path[i] = map1.get(path1.path[i]);
                    while(map2.containsKey(path2.path[i]))
                        path2.path[i] = map2.get(path2.path[i]);
                }
                path1.actualizeFitnessScore();
                path2.actualizeFitnessScore();
            }
        };

        public void crossover(Genetic.Path path1, Genetic.Path path2){

        }

    }
    public enum Mutation{
        Swap{
            @Override
            public Path mutate(Path path){
                int nodeCount = path.path.length;
                Random random = new Random();
                int first, second;
                first = random.nextInt(nodeCount);
                do {
                    second = random.nextInt(nodeCount);
                }while(first==second);

                int temp = path.path[first];
                path.path[first] = path.path[second];
                path.path[second] = temp;

                super.mutate(path);
                return path;
            }
        },
        Inverse{
            @Override
            public Path mutate(Path path){

                int nodeCount = path.path.length;
                Random random = new Random();
                int begin, end;
                begin = random.nextInt(nodeCount);
                do {
                    end = random.nextInt(nodeCount);
                }while(begin==end);

                if(begin>end){
                    int temp = begin;
                    begin = end;
                    end = temp;
                }
                int lenght = end - begin;
                int[] temp = new int[lenght];
                System.out.println("begin end: " + begin + " " + end);
                System.arraycopy(path.path,begin,temp,0, lenght);
                for (int i = 0; i < lenght; i++) {
                    path.path[end-i-1] = temp[i];
                }

                super.mutate(path);
                return path;
            }
        };

        public Path mutate(Path path){
            path.actualizeFitnessScore();
            return null;
        }
    }

    public Genetic(int[][] graph, int populationSize, long timeLimit, Mutation mutation) {
        this.mutation = mutation;
        this.graph = graph;
        this.nodeCount = graph.length;
        this.populationSize = populationSize;
        this.timeLimit = timeLimit;

    }
}
