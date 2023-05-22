package com.entity;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

	private double distance = Double.MAX_VALUE;//initialise the distance to all nodes as the maximum value of a 32 bit integer.
	private double riskDistance = Double.MAX_VALUE;//init risk dist. Make dijkstra update all of the risk dist
	private Vertex previousVertex = null;
	private final long id; // Name of the place
	private final List<Edge> incidentRoads = new ArrayList<>(4); // Incident edges
	private final double latitude;
	private final double longitude;

	// Constructor: set name, chargingStation and index according to given values,
	// initialise incidentRoads as empty array
	public Vertex(final long id, final double latitude, final double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vertex)) {
			return false;
		}
		return ((Vertex) obj).id == this.id;
	}

	@Override
	public String toString() {
		//TODO
		return String.valueOf(getId());
	}

	public long getId() {
		return id;
	}

	public List<Edge> getIncidentRoads() {
		return incidentRoads;
	}

	// Add a road to the array incidentRoads
	public void addIncidentRoad(Edge road) {
		incidentRoads.add(road);
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getRiskDistance() {
		return riskDistance;
	}

	public void setRiskDistance(double riskDistance) {
		this.riskDistance = riskDistance;
	}

	public Vertex getPreviousVertex() {
		return previousVertex;
	}

	public void setPreviousVertex(Vertex previousVertex) {
		this.previousVertex = previousVertex;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}


}
