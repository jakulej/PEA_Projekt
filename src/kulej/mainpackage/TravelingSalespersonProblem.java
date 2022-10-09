package kulej.mainpackage;

import kulej.algorithms.BruteForce;

import java.io.File;
import java.io.FileNotFoundException;


public class TravelingSalespersonProblem {
    public static void main(String[] args) {

        try {
            Graph cities = new Graph("tsp_10.txt");
            //System.out.println(cities);
            BruteForce bruteForce = new BruteForce(cities);
            int[] soultion = bruteForce.getSolution();

            for (int i = 0; i < soultion.length; i++) {
                System.out.print(soultion[i] + " ");
            }

        } catch (FileNotFoundException e){
            System.out.println("Nie ma takiego pliku");
        }
    }

}
