package kulej.mainpackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class Graph {
    public int getNodeCount() {
        return nodeCount;
    }

    int nodeCount;
    int edgeCount;
    int[][] adjacencyMatrix;

    public Graph(String filename) throws FileNotFoundException {
        Scanner read = new Scanner(new File(filename));
        this.nodeCount = read.nextInt();
        this.adjacencyMatrix = new int[this.nodeCount][this.nodeCount];

        for(int i = 0; i < this.nodeCount; ++i) {
            for(int j = 0; j < this.nodeCount; ++j) {
                this.adjacencyMatrix[i][j] = read.nextInt();
            }
        }

    }
    public Graph(int size){
        Random random = new Random();
        this.nodeCount = size;
        this.adjacencyMatrix = new int[this.nodeCount][this.nodeCount];

        for(int i = 0; i < this.nodeCount; ++i) {
            for(int j = 0; j < this.nodeCount; ++j) {
                if(i==j)
                    this.adjacencyMatrix[i][j] = -1;
                else
                    this.adjacencyMatrix[i][j] = random.nextInt(101);
            }
        }
    }
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < this.nodeCount; ++i) {
            for(int j = 0; j < this.nodeCount; ++j) {
                stringBuilder.append(this.adjacencyMatrix[i][j] + " ");
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
    public int[][] getAdjacencyMatrix(){
        return adjacencyMatrix;
    }

}

