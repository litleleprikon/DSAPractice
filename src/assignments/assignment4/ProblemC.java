package assignments.assignment4;

import data_srtuctures.Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Overrided class of Graph special for Problem C
 */
class ProblemCGraph<Data extends Comparable<Data>> extends Graph<Data> {

    /**
     * Method to get country code from city name
     * @param city city name
     * @return country code
     */
    private String getCoutry(String city) {
        return city.substring(city.lastIndexOf('-') + 1);
    }

    /**
     * Method to set weights in all vertices to infinities
     */
    private void setAllWeightsToinfinity(){
        for(Vertex vertex : vertices) {
            vertex.setWeight(Integer.MAX_VALUE);
        }
    }

    /**
     * Method, that visits all vertices in graph and sets it to unvisited
     */
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

    private Vertex getFirstUnvisitedVertex() {
        for(Vertex vertex : vertices) {
            if(!vertex.isVisited()) {
                return vertex;
            }
        }
        return null;
    }

    private Vertex getVertexWithMinimalWeight() {
        Vertex minimal = getFirstUnvisitedVertex();
        if(minimal == null) {
            return null;
        }
        for(Vertex vertex : vertices) {
            if(vertex.getWeight() < minimal.getWeight() && !vertex.isVisited()) {
                minimal = vertex;
            }
        }
        return minimal;
    }

    private LinkedList<Vertex> getPath(Vertex vertexStart, Vertex vertexFinish ) {
        LinkedList<Vertex> result;
        if(vertexFinish == vertexStart) {
            result = new LinkedList<>();
            result.add(vertexFinish);
            return result;
        }
        vertexFinish.setVisited();
        result = getPath(vertexStart, vertexFinish.getEdgeWithMinimalNotVisitedVertex().getTo());
        result.add(vertexFinish);
        return result;
    }

    public LinkedList<Vertex> dijkstra(Data from, Data to) {
        setAllToUnvisited();
        setAllWeightsToinfinity();
        Vertex fromVertex = getVertex(from);
        checkVertex(fromVertex, from);

        fromVertex.setWeight(0);

        Vertex toVertex = getVertex(to);
        checkVertex(toVertex, to);

        Vertex temp;

        while (true){
            temp = getVertexWithMinimalWeight();
            if(temp == null) {
                break;
            }
            temp.dijkstra();
        }
        setAllToUnvisited();
        return getPath(fromVertex, toVertex);
    }

    /**
     * Method to load graph data from file
     * File must contain two lines:
     * First line contains names of all vertices in graph splitted by whitespace
     * Second line contains edges between vertices. Edges represented as pairs of names of vertices
     * @param path path to file
     * @return New graph, builded from data in file
     */
    public static ProblemCGraph<String> loadFromFile(String path) throws FileNotFoundException {
        // reading data from file
        File file = new File(path);
        String vertices[];
        String edges[];
        Scanner sc = new Scanner(file);
        vertices = sc.nextLine().split(" ");
        edges = sc.nextLine().split(" ");
        sc.close();

        // creation and initialisation of graph
        ProblemCGraph<String> graph = new ProblemCGraph<>();

        for(String data : vertices) {
            graph.addData(data);
        }

        for(int i = 0; i < edges.length; i+=2) {
            graph.addEdge(edges[i], edges[i+1]);
        }

        return graph;
    }
}

/**
 * Created by litleleprikon on 01/12/15.
 * Class to solve problem C
 */
public class ProblemC {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        ProblemCGraph<String> graph = ProblemCGraph.loadFromFile("cities.txt");
        LinkedList<Graph<String>.Vertex> first = graph.dijkstra("Melitopol-U", "Rostov-R");
        LinkedList<Graph<String>.Vertex> second = graph.dijkstra("Sukhumi-DG", "Lugansk-DU");
        writeResults(first, second);
    }

    private static void writeResults(LinkedList<Graph<String>.Vertex> first, LinkedList<Graph<String>.Vertex> second)
            throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer;
        writer = new PrintWriter("travel.txt", "UTF-8");
        writer.print(first.size()-1 + " ");
        for(Graph.Vertex vertex : first) {
            writer.print(vertex.getData() + " ");
        }
        writer.println();

        writer.print(second.size()-1 + " ");
        for(Graph.Vertex vertex : second) {
            writer.print(vertex.getData() + " ");
        }
        writer.println();
        writer.close();
    }
}
