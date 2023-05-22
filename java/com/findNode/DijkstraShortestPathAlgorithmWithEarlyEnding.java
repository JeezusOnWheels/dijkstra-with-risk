package com.findNode;

import com.entity.Edge;
import com.entity.Vertex;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Find a route with shortest length.
 */
public class DijkstraShortestPathAlgorithmWithEarlyEnding implements DijkstraAlgorithm {
    @Override
    public void calculateRoute(Vertex startVertex, Vertex endVertex) {
        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getDistance));
        pq.add(startVertex);
        int visitCounter = 0;
        while (!pq.isEmpty()) {
            visitCounter++;
            Vertex currentVertex = pq.remove();
            double currentDistance = currentVertex.getDistance();
            if (currentVertex == endVertex) {
                System.out.println(visitCounter);
                return;
            }
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
                double distance = currentDistance + currentRoad.getLength();
                if (distance < otherEnd.getDistance()) {
                    otherEnd.setDistance(distance);
                    otherEnd.setRiskDistance(currentVertex.getRiskDistance() + currentRoad.getRisk());
                    otherEnd.setPreviousVertex(currentVertex);
                    //System.out.println("Set "+currentVertex.getId()+ "(->)"+ otherEnd.getId());
                    pq.add(otherEnd);
                }
            }
        }
        System.out.println(visitCounter);
    }
}
