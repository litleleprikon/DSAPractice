package assignments.assignment4;

import algorithms.HeapSort;
import data_srtuctures.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by litleleprikon on 30/11/15.
 * Solution for A problem
 */

class ProblemAGraph<Data extends Comparable<Data>> extends Graph<Data> {
    /**
     * Added special for first problem method of graph
     * @param data data to search vertex
     * @return sorted array of vertex neighbours
     */
    public Data[] getVertexSortedNeigbours(Data data) {
        Vertex vertex = getVertex(data);
        checkVertex(vertex, data);
        Data[] neighbours = vertex.getNeighbours();
        new HeapSort().sort(neighbours);
        return neighbours;
    }

    /**
     * Overloaded Method to load graph data from file
     * File must contain two lines:
     * First line contains names of all vertices in graph splitted by whitespace
     * Second line contains edges between vertices. Edges represented as pairs of names of vertices
     * @param path path to file
     * @return New graph, builded from data in file
     */
    public static ProblemAGraph<String> loadFromFile(String path) throws FileNotFoundException {
        // reading data from file
        File file = new File(path);
        String vertices[];
        String edges[];
        Scanner sc = new Scanner(file);
        vertices = sc.nextLine().split(" ");
        edges = sc.nextLine().split(" ");
        sc.close();

        // creation and initialisation of graph
        ProblemAGraph<String> graph = new ProblemAGraph<>();

        for(String data : vertices) {
            graph.addData(data);
        }

        for(int i = 0; i < edges.length; i+=2) {
            graph.addEdge(edges[i], edges[i+1]);
        }

        return graph;
    }
}

public class ProblemA {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        ProblemAGraph<String> graph;
        graph = ProblemAGraph.loadFromFile("cities.txt");
        String[] cities = new String[]{"Donetsk-DU", "Kiev-U", "Lviv-U", "Batumi-G", "Rostov-R"};
        String[][] results = new String[cities.length][];
        for(int i = 0; i < cities.length; i++) {
            try {
                Comparable[] temp = graph.getVertexSortedNeigbours(cities[i]);
                results[i] = Arrays.copyOf(temp, temp.length, String[].class);
            }
            catch (Graph.GraphNotContainsThisVertexError ex) {
                System.out.println("Attempt to access to undefined city: " + cities[i]);
                results[i] = new String[0];
            }
        }
        writeResults(results);
    }

    private static void writeResults(String[][] results) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer;
        writer = new PrintWriter("around.txt", "UTF-8");
        for(String[] line : results) {
            String strRes = "";
            for (String temp : line) {
                strRes += temp + " ";
            }
            strRes.trim();
            writer.println(strRes);
        }
        writer.close();
    }
}
