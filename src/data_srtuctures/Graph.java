package data_srtuctures;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by litleleprikon on 27/11/15.
 * Simple class of Graph
 */
public class Graph<Data extends Comparable<Data>> {
    /**
     * Error class to handle cases of access to vertex with data that not contained in this graph
     */
    public static class GraphNotContainsThisVertexError extends Error {
        public GraphNotContainsThisVertexError(String data) {
            super("Graph not contains vertex with this data: " + data);
        }
    }

    /**
     * Raised in Dijkstra algorithm in case of vertex have not any edges
     */
    public static class VertexHaveNoEdges extends Error {
        public VertexHaveNoEdges() {
            super("Vertex have no edges");
        }
    }

    /**
     * Error class to handle cases of access to vertex with data that not contained in this graph
     */
    public static class GraphNotContainsThisEdgeError extends Error {
        public GraphNotContainsThisEdgeError(String from, String to) {
            super("Graph not contains edge with this vertexes from: " + from + " to: " + to);
        }
    }

    /**
     * Exception that throws in case of adding vertex vith existing data
     */
    public static class GraphAlreadyContainsThisVertexError extends Error {
        public GraphAlreadyContainsThisVertexError() {
            super("Graph already contains this vertex");
        }
    }
    /**
     * List of all vertexes in graph
     */
    protected List<Vertex> vertices = new ArrayList<>();

    /**
     * Class of vertex of Graph
     */
    public class Vertex implements Comparable<Vertex> {
        /**
         * Contained data
         */
        private Data data;

        /**
         * Weight of way to this vertex from start vertex
         * Default value is infinity. In case of java ve can use Integer.MAX_VALUE as infinity
         */
        private int weight = Integer.MAX_VALUE;

        /**
         * In case that we set edges to null on removing ve must to find first not null edge
         * @return first not null edge
         */
        private Edge getFirstNotNullNotVisitedEdge() {
            for(Edge edge : incidents) {
                if(edge != null && !edge.getTo().isVisited()) {
                    return edge;
                }
            }
            throw new VertexHaveNoEdges();
        }

        /**
         * Method with veeeeery long name
         * For Dijkstra algorithm search of not visited neighbour vertex with minimal weight
         * @return edge with end vertex with minimal weight
         */
        public Edge getEdgeWithMinimalNotVisitedVertex() {
            Edge minimal;
            try {
                minimal = getFirstNotNullNotVisitedEdge(); // By default set minimal to first edge
            }
            catch(VertexHaveNoEdges ex) {
                return null;  // if vertex have not any unvisited neighbours return null
            }
            for(Edge edge : incidents) {
                if (edge == null) {
                    continue;
                }
                if(edge.getTo().getWeight() < minimal.getTo().getWeight() && !edge.getTo().isVisited()) {
                    minimal = edge;
                }
            }
            return minimal;
        }

        /**
         * Fill all
         */
        public void dijkstra() {
            int neighbourWeight = weight + Edge.EDGE_WEIGHT;
            for(Edge edge : incidents) {
                if(edge == null || edge.getTo().isVisited()) {
                    continue;
                }
                if(edge.getTo().getWeight() > neighbourWeight) {
                    edge.getTo().setWeight(neighbourWeight);
                }
            }
            setVisited();
        }

        /**
         * Field to set true when visit this vertex
         */
        private boolean visited = false;

        /**
         * Checks is this vertex visited
         * @return trues if visited else false
         */
        public boolean isVisited() {
            return visited;
        }

        /**
         * Sets visited to false
         */
        public void clearVisited() {
            visited = false;
        }

        /**
         * Method to set current vertex visible
         */
        public void setVisited() {
            visited = true;
        }

        /**
         * Depth first search algorithm
         * Used to set all vertexes that reachable to visited
         */
        public void dfs() {
            setVisited();
            for(Edge edge : incidents) {
                if(edge == null) {  // deleted edge
                    continue;
                }
                if(!edge.getTo().isVisited()) {
                    edge.getTo().dfs();
                }
            }
        }

        /**
         * Method to delete edge to specific vertex
         * @param to specific vertex
         */
        public void deleteEdge(Vertex to) {
            Edge edgeTo = getEdge(to);
            incidents.set(edgeTo.getIndex(), null);
        }

        public void deleteAllEdges() {
            for(Edge edge : incidents) {
                edge.getTo().deleteEdge(this);
            }
            incidents = new ArrayList<>();
        }

        /**
         * List of all edges from this vertex
         */
        private List<Edge> incidents = new ArrayList<>();

        /**
         * Index to position of this vertex in list of vertexes in class Graph
         */
        private int index = -1;

        /**
         * Metohd to add edge to vertex
         */
        public void addEdge(Vertex to) {
            Edge edge = new Edge(this, to, incidents.size());
            incidents.add(edge);
        }

        /**
         * Method to get all incident vertices
         * @return Linked list of neighbour vertices
         */
        public LinkedList<Vertex> getNeighbourVertices() {
            LinkedList<Vertex> neighbours = new LinkedList<>();
            for (Edge edge : incidents) {
                if(edge != null) {
                    neighbours.add(edge.getTo());
                }
            }
            return neighbours;
        }

        public Data[] getNeighbours() {
            Comparable[] result = new Comparable[incidents.size()];
            int i = 0;
            for(Edge edge : incidents) {
                result[i] = edge.getTo().getData();
                i++;
            }
            Data[] temp = (Data[]) Arrays.copyOf(result, result.length, ((Data[]) new Comparable[incidents.size()]).getClass());
            return temp;
        }

        /**
         * Method to find edge
         * @param to vertex that should be in other side of this edge
         * @return edge
         */
        public Edge getEdge(Vertex to) {
            for(Edge edge : incidents) {
                if(edge == null) {
                    continue;
                }
                if(edge.getTo().equals(to)) {
                    return edge;
                }
            }
            return null;
        }

        /**
         * Method to set edge to null without removing it from list of edges
         * @param edge edge to set null
         */
        public void setEdgeToNull(Edge edge) {
            incidents.set(edge.getIndex(), null);
        }

        /**
         * Getter for index of vertex
         * @return index of vertex
         */
        public int getIndex() {
            return index;
        }

        /**
         * Overrided method of Object class
         * @param o other object to compare
         * @return true if both objects equals else false
         */
        @Override
        public boolean equals(Object o) {
            try {
                Vertex other = (Vertex)o;
                return other.data == data;
            }
            catch (ClassCastException e) {
                return false;
            }
        }

        /**
         * Default constructor of vertex
         * @param data
         */
        public Vertex(Data data, int index) {
            this.data = data;
            this.index = index;
        }

        /**
         * Accessor to data
         * @return data
         */
        public Data getData() {
            return data;
        }

        /**
         * Method to get all neighbours of this vertex
         * @return list of
         */
        public List<Vertex> adjacent() {
            return incidents.stream().map(Edge::getTo).collect(Collectors.toList());
        }


        @Override
        public int compareTo(Vertex o) {
            return data.compareTo(o.getData());
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "data=" + data +
                    '}';
        }

        /**
         * getter of weight
         * @return weight
         */
        public int getWeight() {
            return weight;
        }

        /**
         * setter of weight
         * @param weight
         */
        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    /**
     * Class to represent edge between tvo vertexes
     */
    public class Edge {

        /**
         * Weight of one edge in graph
         * Because we have unweighted graph, we have constant weight
         */
        private static final int EDGE_WEIGHT = 1;

        /**
         * Start vertex of edge
         */
        private Vertex from;

        /**
         * End vertex of edge
         */
        private Vertex to;

        /**
         * Index of this edge in list of edges of start vertex
         */
        private int index = -1;

        /**
         * Constructor of edge
         * @param from start vertex
         * @param to end vertex
         * @param index index in list of edges in start vertex
         */
        public Edge(Vertex from, Vertex to, int index) {
            this.from = from;
            this.to = to;
            this.index = index;
        }

        /**
         * Getter to start vertex
         * @return start vertex
         */
        public Vertex getFrom() {
            return from;
        }

        /**
         * Getter to end vertex
         * @return end vertex
         */
        public Vertex getTo() {
            return to;
        }

        /**
         * Getter to index
         * @return index
         */
        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", index=" + index +
                    '}';
        }
    }

    /**
     * Method to get vertex by data
     * @param data data to be searched by
     * @return Vertex with this data or null if vertex with this data is not exists
     */
    public Vertex getVertex(Data data) {
        for(Vertex current : vertices) {
            if(current.getData().equals(data)) {
                return current;
            }
        }
        return null;
    }

    /**
     * method to check if graph contain vertex
     * @param data data to check
     * @return true if graph contain vertex with this data, else false
     */
    public boolean containData(Data data) {
        return getVertex(data) != null;
    }

    /**
     * Method to add data to graph
     * Creates new vertex for this data
     * @param data data to add
     */
    public void addData(Data data) {
        if(containData(data)) {
            throw new GraphAlreadyContainsThisVertexError();
        }
        Vertex vertex = new Vertex(data, vertices.size());
        vertices.add(vertex);
    }

    /**
     * Removes vertex with given data
     * @param data data to find vertex
     * @return true if vertex with this data exists else false
     */
    public void removeData(Data data) {
        Vertex vertex = getVertex(data);
        if(vertex == null) {
            throw new GraphNotContainsThisVertexError(data.toString());
        }
        this.vertices.set(vertex.getIndex(), null);
    }

    /**
     * Method to check if vertex is null and raise exception if it null
     * @param vertex Vertex to check
     * @param data What data probably must contain this vertex, used to generate exception
     */
    protected void checkVertex(Vertex vertex, Data data) {
        if(vertex == null) {
            throw new GraphNotContainsThisVertexError(data.toString());
        }
    }

    /**
     * Method to raise exception if edge is not exists
     * @param edge edge to check
     * @param from Vertex from, used to generate exception
     * @param to Vertex to, used to generate exception
     */
    protected void checkEdge(Edge edge, Vertex from, Vertex to) {
        if(edge == null) {
            throw new GraphNotContainsThisEdgeError(from.toString(), to.toString());
        }
    }

    /**
     * removes edge between vertices, that found by given data
     * @param from data to find start vertex of edge
     * @param to data to find the end vertex of edge
     * @return true if this edge exists else false
     */
    public void removeEdge(Data from, Data to){

        // Find and check vertices
        Vertex vertexFrom = getVertex(from);
        checkVertex(vertexFrom, from);
        Vertex vertexTo = getVertex(to);
        checkVertex(vertexTo, to);

        // Find and check edges
        Edge edgeFrom = vertexFrom.getEdge(vertexTo);
        checkEdge(edgeFrom, vertexFrom, vertexTo);
        Edge edgeTo = vertexTo.getEdge(vertexFrom);
        checkEdge(edgeFrom, vertexTo, vertexFrom);


        // Removing of edges
        vertexFrom.setEdgeToNull(edgeFrom);
        vertexTo.setEdgeToNull(edgeTo);
    }

    /**
     * Method to add edge between two vertices
     * @param from data, contained in start vertex
     * @param to data, contained in end vertex
     */
    protected void addEdge(Data from, Data to) {
        // Find and check vertices
        Vertex vertexFrom = getVertex(from);
        checkVertex(vertexFrom, from);
        Vertex vertexTo = getVertex(to);
        checkVertex(vertexTo, to);

        vertexFrom.addEdge(vertexTo);
        vertexTo.addEdge(vertexFrom);
    }

    /**
     * Method to load graph data from file
     * File must contain two lines:
     * First line contains names of all vertices in graph splitted by whitespace
     * Second line contains edges between vertices. Edges represented as pairs of names of vertices
     * @param path path to file
     * @return New graph, builded from data in file
     */
    public static Graph<String> loadFromFile(String path) throws FileNotFoundException {
        // reading data from file
        File file = new File(path);
        String vertices[];
        String edges[];
        Scanner sc = new Scanner(file);
        vertices = sc.nextLine().split(" ");
        edges = sc.nextLine().split(" ");
        sc.close();

        // creation and initialisation of graph
        Graph<String> graph = new Graph<>();

        for(String data : vertices) {
            graph.addData(data);
        }

        for(int i = 0; i < edges.length; i+=2) {
            graph.addEdge(edges[i], edges[i+1]);
        }

        return graph;
    }
}
