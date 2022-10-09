package kulej.mainpackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

