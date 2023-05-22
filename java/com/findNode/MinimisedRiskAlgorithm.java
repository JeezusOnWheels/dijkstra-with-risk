package com.findNode;

import com.entity.Edge;
import com.entity.Vertex;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Find a route with minimised risk.
 */
public class MinimisedRiskAlgorithm implements DijkstraAlgorithm {
    @Override
    public void calculateRoute(Vertex startVertex, Vertex endVertex) {
        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getRiskDistance));
        pq.add(startVertex);
        while (!pq.isEmpty()) {
            Vertex currentVertex = pq.remove();
            double currentRisk = currentVertex.getRiskDistance();
            if (currentRisk > currentVertex.getRiskDistance()) {
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
                double risk = currentRisk + currentRoad.getRisk();
                if (risk < otherEnd.getRiskDistance()) {
                    otherEnd.setRiskDistance(risk);
                    otherEnd.setDistance(currentVertex.getDistance() + currentRoad.getLength());
                    otherEnd.setPreviousVertex(currentVertex);
                    pq.add(otherEnd);
                }
            }
        }
    }
}
