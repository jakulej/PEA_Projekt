package kulej.mainpackage;

import kulej.algorithms.BandB;
import kulej.algorithms.BruteForce;

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
                System.out.println("5. Zakończ program.");
                Scanner keyboard = new Scanner(System.in);
                choise = keyboard.nextInt();
                keyboard.nextLine();
                System.out.println(choise);

                switch (choise) {
                    case 1:
                        System.out.println("Podaj nazwe pliku wraz z formatem");
                        cities = new Graph(keyboard.nextLine());
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
                        BandB bandb = new BandB(cities);
                        Path path = bandb.resolve();
                        int[] soultion = path.getCurrentPath().stream().mapToInt(i -> i).toArray();
                        /*BruteForce bruteForce = new BruteForce(cities);
                        BruteForce.Path path = bruteForce.getSolution();
                        int[] soultion = path.getCurrentPath().stream().mapToInt(i -> i).toArray();
                           */
                        System.out.println("Najkrotsza sciezka: ");
                        for (int i = 0; i < soultion.length; i++) {
                            System.out.print(soultion[i] + " -> ");
                        }
                        System.out.println(soultion[0]);
                        System.out.println("Koszt: " + BandB.getCost(cities.getAdjacencyMatrix(),soultion));
                        break;
                    case 6:
                        Graph random;
                        int size = 10;
                        long start = 0,stop,sumBF = 0, sumBnB = 0;
                        for (int i = 0; i < 150; i++) {
                            random = new Graph(size);
                            if(i>49)
                                start = System.nanoTime();
                            BruteForce bruteForce = new BruteForce(random);
                            Path pathBF = bruteForce.getSolution();
                            stop = System.nanoTime();
                            if(i>49)
                                sumBF += stop - start;
                        }
                        System.out.println("SumBF "+sumBF);

                        for (int i = 0; i < 150; i++) {
                            random = new Graph(size);
                            if(i>49)
                                start = System.nanoTime();
                            BruteForce bruteForce = new BruteForce(random);
                            Path pathBF = bruteForce.getSolution();
                            stop = System.nanoTime();
                            if(i>49)
                                sumBnB += stop - start;
                        }
                        System.out.println("SumBn "+sumBnB);

                        break;
                }
            } catch (FileNotFoundException | InputMismatchException e) {
                if (e instanceof FileNotFoundException)
                    System.out.println("Nie ma takiego pliku");
                else
                    System.out.println("Podano złe dane");

            }
            System.out.println("\n\n");
        } while (choise != 5);
    }

}
