package assignments.assignment4;

import data_srtuctures.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by litleleprikon on 01/12/15.
 * Class to solve problem B
 */
public class ProblemB {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        ProblemBGraph<String> graph = ProblemBGraph.loadFromFile("cities.txt");
        String[] results = new String[2];
        results[0] = graph.canVisitAllFrom("Rostov-R");
        try {
            graph.removeEdge("Vladikavkaz-R", "Tbilisi-G");
            results[1] = graph.canVisitAllFrom("Rostov-R");
        }
        catch (Graph.GraphNotContainsThisEdgeError e) {
            results[1] = results[0]; // just a little optimisation
        }
        writeResults(results);
    }

    private static void writeResults(String[] results) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer;
        writer = new PrintWriter("able.txt", "UTF-8");
        writer.println(results[0] + " " + results[1]);
        writer.close();
    }
}

/**
 * Overrided class of Graph special for Problem B
 */
class ProblemBGraph<Data extends Comparable<Data>> extends Graph<Data> {

    /**
     * Method to get country code from city name
     * @param city city name
     * @return country code
     */
    private String getCoutry(String city) {
        return city.substring(city.lastIndexOf('-') + 1);
    }

    private void setAllToUnvisited() {
        for(Vertex vertex : vertices) {
            vertex.clearVisited();
        }
    }

    /**
     * Method to delete all edges between from U cities if was transfer from R city to DU
     * @param from from city
     * @param to to city
     */
    private void firstConstraint(Data from, Data to) {
        String fromCountry = getCoutry((String)from);
        String toCountry = getCoutry((String)to);
        if(Objects.equals(fromCountry, "R") && Objects.equals(toCountry, "DU")) {
            removeEdge(from, to);
        }
    }

    /**
     * Method to delete all edges between from G cities if was transfer from R city to DG
     * @param from from city
     * @param to to city
     */
    private void secondConstraint(Data from, Data to) {
        String fromCountry = getCoutry((String)from);
        String toCountry = getCoutry((String)to);
        if(Objects.equals(fromCountry, "R") && Objects.equals(toCountry, "DG")) {
            removeEdge(from, to);
        }
    }

    /**
     * Method to define,
     * @return
     */
    public String canVisitAllFrom(Data from) {
        Vertex start = getVertex(from);
        checkVertex(start, from);
        start.dfs();
        for(Vertex vertex : vertices) {
            if(!vertex.isVisited()) {
                setAllToUnvisited();
                return "no";
            }
        }
        setAllToUnvisited();
        return "yes";
    }

    /**
     * Method, that defines is exists the way between two vertices
     * @param from data to found vertex from
     * @param to data to found vertex to
     * @return
     */
    private boolean haveWayBetween(Data from, Data to) {
        Vertex vertexFrom = getVertex(from);
        checkVertex(vertexFrom, from);
        return vertexFrom.dijkstraAlgorithm(to) != null;
    }

    private void runFirstConstraint() {
        for(Vertex from : vertices) {
            for(Vertex to : from.getNeighbourVertices()) {
                firstConstraint(from.getData(), to.getData());
            }
        }
    }

    private void runSecondConstraint() {
        for(Vertex from : vertices) {
            for(Vertex to : from.getNeighbourVertices()) {
                secondConstraint(from.getData(), to.getData());
            }
        }
    }

    /**
     * Method to load graph data from file
     * File must contain two lines:
     * First line contains names of all vertices in graph splitted by whitespace
     * Second line contains edges between vertices. Edges represented as pairs of names of vertices
     * @param path path to file
     * @return New graph, builded from data in file
     */
    public static ProblemBGraph<String> loadFromFile(String path) throws FileNotFoundException {
        // reading data from file
        File file = new File(path);
        String vertices[];
        String edges[];
        Scanner sc = new Scanner(file);
        vertices = sc.nextLine().split(" ");
        edges = sc.nextLine().split(" ");
        sc.close();

        // creation and initialisation of graph
        ProblemBGraph<String> graph = new ProblemBGraph<>();

        for(String data : vertices) {
            graph.addData(data);
        }

        for(int i = 0; i < edges.length; i+=2) {
            graph.addEdge(edges[i], edges[i+1]);
        }

        graph.runFirstConstraint();
        graph.runSecondConstraint();

        return graph;
    }
}