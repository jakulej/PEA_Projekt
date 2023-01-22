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


    long timeLimit;
    public int[] resolve(){

        ArrayList<Path> population = new ArrayList<>();
        long start = System.nanoTime();
        bestSolution = new Path();
        Path best = genetic(generatePopulation());

        do {
            population.add(best);
            while (population.size()!=populationSize){
                population.add(genetic(generatePopulation()));
            }
            best = genetic(population);
            population.clear();
        }while(System.nanoTime()-start<timeLimit);

       return bestSolution.path;
    }
    private Path genetic(ArrayList<Path> population){
        Path best;
        Random random = new Random();
        //Krzyzowanie
        population = selection(population);
        if(random.nextDouble()<crossoverRatio)
            Crossover.PMX.crossover(population.get(0),population.get(1));

        if(population.get(0).fitnessScore>population.get(1).fitnessScore)
            best = population.get(0);
        else
            best = population.get(1);

        //Mutacja
        if(random.nextDouble()<mutationRatio)
            mutation.mutate(best);

        if(best.fitnessScore> bestSolution.fitnessScore)
            bestSolution = new Path(best);
        return best;
    }
    int getFitnessScore(int[] path){
        int summary = 0;
        for (int i = 1; i < nodeCount; i++) {
            summary+= graph[path[i-1]][path[i]];
        }
        summary+=graph[path[nodeCount-1]][path[0]];
        return 1/summary;
    }
    ArrayList<Path> generatePopulation(){
        ArrayList<Path> population = new ArrayList<Path>();
        while (population.size()!=populationSize){
            population.add(new Path());
        }
        return population;
    }
    ArrayList<Path> generatePopulation(Path path){
        ArrayList<Path> population = new ArrayList<Path>();
        population.add(path);
        while (population.size()!=populationSize){
            population.add(new Path());
        }
        return population;
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
    ArrayList<Path> selection(ArrayList<Path> population){
        while(population.size()>2) {
            for (int i = 0; i < population.size() / 2; i++) {
                population.remove(getWorsePath(population.get(i), population.get(i + 1)));
            }
        }
        return population;
    }
    Path getWorsePath(Path path1, Path path2){
        if (path1.fitnessScore< path2.fitnessScore){
            return path1;
        }
        else
            return path2;
    }
    private class Path{
        int[] path;
        int fitnessScore;

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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path1 = (Path) o;
            return Arrays.equals(path, path1.path);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(path);
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
