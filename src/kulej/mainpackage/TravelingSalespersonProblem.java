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
                long seconds = 160L;
                long time = seconds * 1000000000L;
                int temperature = 2293000;
                double cooler = 0.9;
                int period = 50;
                int population = 10;


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
                        Genetic genetic = new Genetic(cities.getAdjacencyMatrix(),population);
                        genetic.resolve();

                        break;
                    case 5:
                        System.out.println("Podaj kolejno temperature, chlodzenie, czas populacji, czas trwania programu");
                        temperature = keyboard.nextInt();
                        cooler = keyboard.nextDouble();
                        period = keyboard.nextInt();
                        time = keyboard.nextLong();
                    case 6:
                        //testSingleBnB(13);

                        for (int i = 5; i < 12; i++) {
                            testBF(i);
                            testBnB(i);
                        }
                        testBnB(12);

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

    private static void testBF(int node){
        Graph random;
        long start = 0,stop,sumBF = 0;
        for (int i = 0; i < 150; i++) {
            random = new Graph(node);
            if(i>49)
                start = System.nanoTime();
            BruteForce bruteForce = new BruteForce(random);
            Path pathBF = bruteForce.getSolution();
            stop = System.nanoTime();
            if(i>49)
                sumBF += stop - start;
        }
        System.out.println(node+" AvargeBF "+sumBF/100);
    }
    private static void testBnB(int node){
        Graph random;
        int correct = 100;
        long start = 0,stop,sumBnB = 0;
        for (int i = 0; i < 150; i++) {
            random = new Graph(node);
            if(i>49)
                start = System.nanoTime();
            BruteForce bruteForce = new BruteForce(random);
            Path pathBF = bruteForce.getSolution();
            stop = System.nanoTime();
            if(pathBF==null){
                correct--;
                start =0;
                stop = 0;
            }
            if(i>49)
                sumBnB += stop - start;
        }
        System.out.println(node + " AvargeBnB: "+sumBnB/correct + " Correct: " + correct);
    }
    private static void testSingleBnB(int node){
        Graph random;
        long start = 0,stop,sumBnB = 0;
            random = new Graph(node);
            start = System.nanoTime();
            BruteForce bruteForce = new BruteForce(random);
            Path pathBF = bruteForce.getSolution();
            stop = System.nanoTime();
            sumBnB += stop - start;

        System.out.println("Avarge: "+sumBnB);
    }

}
