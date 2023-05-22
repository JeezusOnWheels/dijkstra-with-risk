package com.findNode;

import com.entity.Edge;
import com.entity.Vertex;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Find a route whose length and risk is balanced.
 *
 * @
 */
public class LengthRiskTradeOffAlgorithm implements DijkstraAlgorithm {
    @Override
    public void calculateRoute(Vertex startVertex, Vertex endVertex) {
        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getDistance));
        pq.add(startVertex);
        while (!pq.isEmpty()) {
            Vertex currentVertex = pq.remove();
            double currentDistance = currentVertex.getDistance();
            if (currentDistance > currentVertex.getDistance()) {
                continue;
            }
            List<Edge> closeRoads = currentVertex.getIncidentRoads();
            for (Edge currentRoad : closeRoads) {//for neighbour, weight
                Vertex otherEnd;
                if (currentRoad.getFirstVertex() == currentVertex) {
                    otherEnd = currentRoad.getSecondVertex();
                } else {
                    otherEnd = currentRoad.getFirstVertex();
                }
                double distance = currentDistance + currentRoad.getLengthRiskFactor();
                if (distance < otherEnd.getDistance()) {
                    otherEnd.setDistance(distance);
                    otherEnd.setRiskDistance(currentVertex.getRiskDistance() + currentRoad.getRisk());
                    otherEnd.setPreviousVertex(currentVertex);
                    pq.add(otherEnd);
                }
            }
        }
    }
}


