import java.util.*;
import java.io.*;

class Vertex {

	// Constructor: set name, chargingStation and index according to given values,
	// initilaize incidentRoads as empty array
	public Vertex(String placeName, int idx) {
		name = placeName;
		incidentRoads = new ArrayList<Edge>();
		index = idx;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Edge> getIncidentRoads() {
		return incidentRoads;
	}

	// Add a road to the array incidentRoads
	public void addIncidentRoad(Edge road) {
		incidentRoads.add(road);
	}

	public int getIndex() {
		return index;
	}

	public Vertex getCopy(){
		//method that returns a copy of the current vertex
		Vertex vcopy 		= new Vertex(name, index);
		vcopy.incidentRoads = incidentRoads;
		vcopy.visited 	 	= visited;
		vcopy.pathLength 	= pathLength;
		vcopy.prevVertex 	= prevVertex;
		return vcopy;
	}

	public String name; // Name of the place
	public ArrayList<Edge> incidentRoads; // Incident edges
	public int index; // Index of this vertex in the vertex array of the map

	//vars for dijkstra's algo
	public boolean visited = false; // useful to keep track for Dijkstra's algo
	public int pathLength = 0;
	public Vertex prevVertex = null;
	
}
