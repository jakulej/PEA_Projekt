package kulej.algorithms;

import java.util.*;

public class Genetic {
    int[][] graph;
    int nodeCount;
    int populationSize;

    public int[] resolve(){
        Path path1 = new Path();
        Path path2 = new Path();
        path1.printPath();
        path2.printPath();
        Mutation mutation = Mutation.Inverse;
        Mutation mutation2 = Mutation.Swap;
        mutation.mutate(path1);
        mutation2.mutate(path2);

        path1.printPath();
        path2.printPath();
        return new int[0];
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
        for (int i = 0; i < populationSize/2; i++) {
            population.remove(getWorsePath(population.get(i),population.get(i+1)));
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
        public void printPath(){
            for (int i = 0; i < path.length; i++) {
                System.out.print(path[i] + " -> ");
            }
                System.out.print(path[0]+"\n");
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
            }
        };

        public void crossover(Genetic.Path path1, Genetic.Path path2){

        }

    }
    private enum Mutation{
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

                return path;
            }
        };

        public Path mutate(Path path){

            return null;
        }
    }

    public Genetic(int[][] graph, int populationSize) {
        this.graph = graph;
        this.nodeCount = graph.length;
        this.populationSize = populationSize;

    }
}
