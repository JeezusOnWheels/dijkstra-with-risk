package com.findNode;

import com.entity.Edge;
import com.entity.Route;
import com.entity.Routes;
import com.entity.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A class that represents a sparse matrix
 */
public class RoadMap {
    private final List<Edge> roads;
    private final Map<Long, Vertex> vertices;

    public RoadMap(List<Edge> edges, Map<Long, Vertex> vertices) {
        this.vertices = vertices;
        this.roads = edges;
    }

    /**
     * Calculate the path between two vertex, using three different algorithms: shortest-length, minimised-risk and trade-off.
     *
     * @param startVertex start Vertex
     * @param endVertex   end Vertex
     * @return An List containing routes, each contains vertices id from the start vertex to the end vertex.
     */
    public final Routes routeCalculation(Vertex startVertex, Vertex endVertex) {
        Routes routes = new Routes();

        if (roads.size() == 0 || vertices.size() == 0) {
            throw new AssertionError("The RoadMap hasn't initialised");
        }

        resetAllNodes();
        printPath(calculatePath(startVertex, endVertex, new DijkstraShortestPathAlgorithmWithEarlyEnding()));
        //calculate the shortest-length path
        resetAllNodes();
        List<Vertex> shortestPath = calculatePath(startVertex, endVertex, new AlphaStarShortestPathAlgorithm());
        if (shortestPath == null) return null;
        System.out.println();
        System.out.println("Shortest path between " + startVertex.getId() + " and " + endVertex.getId());
        printPath(shortestPath);
        double length1 = shortestPath.get(shortestPath.size() - 1).getDistance();
        double risk1 = shortestPath.get(shortestPath.size() - 1).getRiskDistance();
        System.out.println(length1 + " is the length of shortest path");
        System.out.println(risk1 + " is the risk of shortest path.");

        //calculate minimised-risk path
        resetAllNodes();
        List<Vertex> risklessPath = calculatePath(startVertex, endVertex, new MinimisedRiskAlgorithm());
        System.out.println();
        System.out.println("Riskless path between " + startVertex.getId() + " and "
                + endVertex.getId());
        printPath(risklessPath);
        double length2 = risklessPath.get(risklessPath.size() - 1).getDistance();
        double risk2 = risklessPath.get(risklessPath.size() - 1).getRiskDistance();
        System.out.println(risklessPath.get(risklessPath.size() - 1).getDistance() + " is the length of risk-less path");
        System.out.println(risklessPath.get(risklessPath.size() - 1).getRiskDistance() + " is the risk of the risk-less path");

        Route shortestRoute = new Route();
        for (Vertex vertex : shortestPath) {
            shortestRoute.addVertex(vertex.getLatitude(), vertex.getLongitude());
            shortestRoute.setRisk(risk1);
            shortestRoute.setLength(length1);
        }
        routes.add(shortestRoute);

        //Compare whether the two path is equal
        //If equal, return first path only
        boolean isDifferentPath = false;
        Route risklessRoute = new Route();
        risklessRoute.setLength(length2);
        risklessRoute.setRisk(risk2);
        for (int i = 0; i < risklessPath.size(); i++) {
            risklessRoute.addVertex(risklessPath.get(i).getLatitude(), risklessPath.get(i).getLongitude());
            if (!isDifferentPath && !risklessRoute.getVertices().get(i).equals(shortestRoute.getVertices().get(i))) {
                isDifferentPath = true;
            }
        }
        if (isDifferentPath) {
            System.out.println("Paths 1 and 2 are different!");
            routes.add(risklessRoute);
        } else {
            System.out.println("Paths 1 and 2 are the same!");
            resetAllNodes();
            return routes;
        }

        //calculate trade-off path
        resetAllNodes();
        List<Vertex> tradeoffPath = sum_rec(risk1, length1, risk2, length2, startVertex, endVertex);
        if (tradeoffPath == null) {
            resetAllNodes();
            System.out.println("The sum_rec is NaN");
            return routes;
        }
        printPath(tradeoffPath);
        System.out.println(tradeoffPath.get(tradeoffPath.size() - 1).getDistance() + " is the risk/length of path 3");

        Route tradeoffRoute = new Route();
        tradeoffRoute.setRisk(tradeoffPath.get(tradeoffPath.size() - 1).getDistance());
        tradeoffRoute.setLength(tradeoffPath.get(tradeoffPath.size() - 1).getDistance());
        boolean differentToDistAndRisk = false;
        for (int i = 0; i < tradeoffPath.size(); i++) {
            tradeoffRoute.addVertex(tradeoffPath.get(i).getLatitude(), tradeoffPath.get(i).getLongitude());
            if (!differentToDistAndRisk && !tradeoffRoute.getVertices().get(i).equals(shortestRoute.getVertices().get(i))) {
                differentToDistAndRisk = true;
            }
            if (!differentToDistAndRisk && isDifferentPath && !tradeoffRoute.getVertices().get(i).equals(risklessRoute.getVertices().get(i))) {
                differentToDistAndRisk = true;
            }
        }
        if (differentToDistAndRisk) {
            System.out.println("Paths 3 is unique!");
            routes.add(tradeoffRoute);
        } else {
            System.out.println("Path 3 is not unique!");
        }
        resetAllNodes();
        return routes;
    }

    /**
     * The algorithm suggested by (Galbrun,2014).
     */
    //ArrayList<ArrayList<Vertex>> Lower_Hull = new ArrayList<ArrayList<Vertex>>();
    //have a path that contains the nodes, then have 2 more array lists (1 with)
    private List<Vertex> sum_rec(double risk1, double length1, double risk2, double length2, Vertex start, Vertex end) {
        double lambda = (risk1 - risk2) / (length1 - length2);
        //check nan
        if (lambda != lambda) return null;
        for (Edge edge : roads) {
            edge.setLengthRiskFactor(edge.getRisk() - lambda * edge.getLength());
        }
        resetAllNodes();
        return calculatePath(start, end, new LengthRiskTradeOffAlgorithm());
    }

    /**
     * Calculate path with an specific routing algorithm. It is in charge of determining whether the
     * start vertex is the end vertex, and re-constructing the road by tracing back from the end Vertex, using previous
     * Vertex attribute.
     *
     * @param startVertex       start Vertex
     * @param endVertex         end Vertex
     * @param dijkstraAlgorithm dijkstra Algorithm used to calculate the path.
     * @return list of vertices representing the route, including the start vertex and end vertex.
     */
    private List<Vertex> calculatePath(Vertex startVertex, Vertex endVertex, DijkstraAlgorithm dijkstraAlgorithm) {
        //init PriorityQueue and path List
        List<Vertex> path = new ArrayList<>();

        //return immediately if start == end
        //TODO override equals()
        if (startVertex.getId() == endVertex.getId()) {
            path.add(startVertex);
            return path;
        }

        startVertex.setDistance(0);
        startVertex.setRiskDistance(0);
        dijkstraAlgorithm.calculateRoute(startVertex, endVertex);

        List<Vertex> verticesToReturn = new ArrayList<>();
        //add in code that follows the trail starting from end
        Vertex currentNode = endVertex;
        while (currentNode != startVertex) {
            //System.out.println("retro-tracing: "+currentNode.getId()+"(->)"+ currentNode.getPreviousVertex().getId());
            if (currentNode == null) { // If the backtracking fails, probably due to unterminated path caused by isolated map area, return null
                return null;
            }
            verticesToReturn.add(currentNode);
            currentNode = currentNode.getPreviousVertex();
        }
        verticesToReturn.add(startVertex);
        Collections.reverse(verticesToReturn);//must reverse the list because end => start!
        return verticesToReturn;
    }

    /**
     * Reset the nodes.
     */
    public void resetAllNodes() {
        for (Vertex v : vertices.values()) {
            v.setDistance(Double.MAX_VALUE);
            v.setRiskDistance(Double.MAX_VALUE);
            v.setPreviousVertex(null);
        }
    }

    public void printPath(List<Vertex> path) {
        if (path == null) return;

        System.out.print("(  ");
        for (Vertex v : path) {
            System.out.print(v.getId() + "  ");
        }
        System.out.println(")");
    }

}
