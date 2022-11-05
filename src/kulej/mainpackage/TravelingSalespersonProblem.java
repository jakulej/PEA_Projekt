package kulej.mainpackage;

import kulej.algorithms.BruteForce;

import java.io.File;
import java.io.FileNotFoundException;


public class TravelingSalespersonProblem {
    public static void main(String[] args) {

        System.out.println("Program wylicza problem komiwojażera. Wybierz opcje: ");

        System.out.println("1.Wczytaj z pliku");
        System.out.println("2.Wygenerowanie danych losowych");
        System.out.println("3.Wyświetlenie ostatnio wczytanych lub wygenerowanych danych");
        System.out.println("4.Uruchomienie danego algorytmu dla ostatnio wczytanych lub wygenerowanych danych i wyświetlenie wyników.");


        //Graph graph = new Graph(10);
        try {
            if(false){
                Graph cities = new Graph("tsp_10.txt");
            }

            Graph cities = new Graph(10);
            BruteForce bruteForce = new BruteForce(cities);
            BruteForce.Path path = bruteForce.getSolution();
            int[] soultion = path.getCurrentPath().stream().mapToInt(i->i).toArray();

            System.out.println("Najkrotsza sciezka: ");
            for (int i = 0; i < soultion.length; i++) {
                System.out.print(soultion[i] + " -> ");
            }
            System.out.println(soultion[0]);
            System.out.println("Koszt: "+ path.getCCost());

        } catch (FileNotFoundException e){
            System.out.println("Nie ma takiego pliku");
        }
    }

}
