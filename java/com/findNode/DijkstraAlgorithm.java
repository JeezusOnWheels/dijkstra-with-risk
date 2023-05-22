package com.findNode;

import com.entity.Vertex;

/**
 * Dijkstra or dijkstra-like algorithm used for finding path between two nodes.
 */
@FunctionalInterface
public interface DijkstraAlgorithm {
    /**
     * The route finding algorithm. The algorithm body should set up the previousVertex attribute in the vertex class,
     * so the route can be constructed later.
     *
     * @param startVertex start Vertex
     * @param endVertex   end Vertex
     */
    public abstract void calculateRoute(Vertex startVertex, Vertex endVertex);
}
