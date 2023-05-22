package com.findNode;

import com.entity.Edge;
import com.entity.Vertex;

import java.util.List;
import java.util.PriorityQueue;

public class AlphaStarShortestPathAlgorithm implements DijkstraAlgorithm {
    @Override
    public void calculateRoute(Vertex startVertex, Vertex endVertex) {
        EuclideanComparator euc = new EuclideanComparator(endVertex);
        PriorityQueue<Vertex> pq = new PriorityQueue<>(euc);
        int visitCounter = 0;
        pq.add(startVertex);
        while (!pq.isEmpty()) {
            visitCounter++;
            Vertex currentVertex = pq.remove();
            double currentDistance = currentVertex.getDistance();
            if (currentVertex == endVertex) {
                System.out.println(visitCounter);
                return;
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
                    //System.out.println(pq.toString());
                }
            }
        }
    }
}
