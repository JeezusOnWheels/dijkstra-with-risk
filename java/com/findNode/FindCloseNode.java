package com.findNode;

import com.entity.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FindCloseNode {

    private static Logger logger = LoggerFactory.getLogger(FindCloseNode.class);

    /**
     * For a given latitude and longitude, find the closest node in the map data.
     *
     * @param lat      latitude of the given geographical point
     * @param lon      longitude of the given geographical point
     * @param vertices vertices in the map data
     * @return the object of closest vertex
     */
    public static Vertex closestNode(final double lat, final double lon, final Map<Long, Vertex> vertices) {
        double shortestDist = Double.MAX_VALUE;
        Vertex closestVertex = null;
        for (Vertex vertex : vertices.values()) {
            double dist = Math.sqrt(Math.pow(vertex.getLatitude() - lat, 2) + Math.pow(vertex.getLongitude() - lon, 2));
            if (dist < shortestDist) {
                shortestDist = dist;
                closestVertex = vertex;
            }
        }
        //TODO null catcher
        return closestVertex;
    }
}
