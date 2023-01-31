package kulej.mainpackage;

import kulej.algorithms.Annealing;
import kulej.algorithms.BandB;
import kulej.algorithms.BruteForce;
import kulej.algorithms.Genetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class TravelingSalespersonProblem {
    public static void main(String[] args) {
        int choise = 0;
        Graph cities = null;
        do {
            try {
                System.out.println("Program wylicza problem komiwojażera. Wybierz opcje: ");
                System.out.println("1.Wczytaj z pliku.");
                System.out.println("2.Wygenerowanie danych losowych.");
                System.out.println("3.Wyświetlenie ostatnio wczytanych lub wygenerowanych danych.");
                System.out.println("4.Uruchomienie danego algorytmu dla ostatnio wczytanych lub wygenerowanych danych i wyświetlenie wyników.");
                System.out.println("5. Wybierz dane");
                Scanner keyboard = new Scanner(System.in);
                choise = keyboard.nextInt();
                keyboard.nextLine();
                System.out.println(choise);
                long seconds = 60L;
                long time = seconds * 1000000000L;
                int temperature = 2293000;
                double cooler = 0.9;
                int period = 50;
                int population = 1000;
                int selectionSize = 100;


                switch (choise) {
                    case 1:
                        System.out.println("Podaj nazwe pliku wraz z formatem");
                        String nazwa = keyboard.nextLine();
                        cities = new Graph(nazwa);
                        break;
                    case 2:
                        System.out.println("Podaj liczbe wierzchołków");
                        cities = new Graph(keyboard.nextInt());
                        keyboard.nextLine();
                        break;
                    case 3:
                        if (cities != null)
                            System.out.println(cities);
                        else
                            System.out.println("Nie wczytano/wylosowano żadnych danych");
                        break;
                    case 4:
                        Genetic genetic = new Genetic(cities.getAdjacencyMatrix(),population, selectionSize,time, Genetic.Mutation.Inverse);
                        int[] solution = genetic.resolve();
                        for (int i = 0; i < solution.length; i++) {
                            System.out.print(solution[i] + " -> ");
                        }
                        System.out.println(" ");
                        System.out.println(calculateCost(solution,cities.getAdjacencyMatrix()));

                        break;
                    case 5:
                        System.out.println("Podaj kolejno temperature, chlodzenie, czas populacji, czas trwania programu");
                        temperature = keyboard.nextInt();
                        cooler = keyboard.nextDouble();
                        period = keyboard.nextInt();
                        time = keyboard.nextLong();
                    case 6:


                        break;
                }
            } catch (FileNotFoundException | InputMismatchException e) {
                if (e instanceof FileNotFoundException)
                    System.out.println("Nie ma takiego pliku");
                else
                    System.out.println("Podano złe dane");

            }
            System.out.println("\n\n");
        } while (choise != 7);
    }

    public static int calculateCost(int[] path, int[][] graph){
        int cost = 0;
        for (int i = 1; i < graph.length; i++) {
            cost += graph[path[i-1]][path[i]];
        }
        cost += graph[path[path.length-1]][path[0]];
        return cost;
    }

}
