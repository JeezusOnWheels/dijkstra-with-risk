package com.findNode;

import com.entity.Vertex;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;

import java.util.Comparator;

public class EuclideanComparator implements Comparator<Vertex> {
    private final GlobalPosition destination;
    private final GeodeticCalculator geodeticCalculator = new GeodeticCalculator();

    public EuclideanComparator(Vertex destination) {
        this.destination = new GlobalPosition(destination.getLatitude(), destination.getLongitude(), 0.0);
    }

    double euclideanDistance(Vertex vertex) {
        Ellipsoid reference = Ellipsoid.WGS84;
        GlobalPosition v = new GlobalPosition(vertex.getLatitude(), vertex.getLongitude(), 0.0);
        //TODO verify unit
        return geodeticCalculator.calculateGeodeticCurve(reference, v, destination).getEllipsoidalDistance() / 1000;
    }

    @Override
    public int compare(Vertex v1, Vertex v2) {
        double h1 = v1.getDistance() + euclideanDistance(v1);
        double h2 = v2.getDistance() + euclideanDistance(v2);
        // System.out.println(v1.getId()+ "distance: "+h1 + " "+v2.getId()+"distance: "+h2);
        return Double.compare(h1, h2);
    }
}
