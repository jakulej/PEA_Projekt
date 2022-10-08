package kulej.mainpackage;

import java.io.File;
import java.io.FileNotFoundException;


public class TravelingSalespersonProblem {
    public static void main(String[] args) {



        //System.out.println(lista);
        System.out.println(new File(".").getAbsolutePath());
        try {
            Graph cities = new Graph("tsp_10.txt");
            System.out.println(cities);
        } catch (FileNotFoundException e){
            System.out.println("Nie ma takiego pliku");
        }
    }

}
