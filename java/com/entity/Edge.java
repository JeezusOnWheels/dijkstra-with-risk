package com.entity;

public class Edge {
    private final double risk;
    private final double length;
    private double lengthRiskFactor;
    private final Vertex[] incidentPlaces;

    public Edge(final double roadLength, final Vertex firstPlace, final Vertex secondPlace, final double risk) {
        this.risk = risk;
        length = roadLength;
        incidentPlaces = new Vertex[]{firstPlace, secondPlace};
    }

    public double getLengthRiskFactor() {
        return lengthRiskFactor;
    }

    public void setLengthRiskFactor(double lengthRiskFactor) {
        this.lengthRiskFactor = lengthRiskFactor;
    }

    public Vertex getFirstVertex() {
        return incidentPlaces[0];
    }

    public Vertex getSecondVertex() {
        return incidentPlaces[1];
    }

    public double getLength() {
        return length;
    }

    public double getRisk() {
        return risk;
    }
}
