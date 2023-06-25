import java.util.*;
import java.io.*;



class CustomComparator implements Comparator<Vertex>{
	public int compare(Vertex v1, Vertex v2){
	 //compare objects wrt pathLength
		if (v1.pathLength > v2.pathLength){
			return 1;//v1 longer
		}
		if (v2.pathLength > v1.pathLength){
			return -1;//v2 longer
		}
		return 0;
	}
 }

// A class that represents a sparse matrix
public class shortestPathWithRisk {

	// Default constructor
	public shortestPathWithRisk() {
		places = new ArrayList<Vertex>();
		roads = new ArrayList<Edge>();
	}

	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a map and print information: java RoadMap -i <MapFile>");
		System.err.println(
				" - Read a map and find shortest path between two vertices: java RoadMap -s <MapFile> <StartVertexIndex> <EndVertexIndex> <mode>.\nMode 0 = shortest path. Mode 1 = least risk. Mode 2 = combination.");
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 2 && args[0].equals("-i")) {
			RoadMap map = new RoadMap();
			try {
				map.loadMap(args[1]);
			} catch (Exception e) {
				System.err.println("Error in reading map file");
				System.exit(-1);
			}

			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();
		} else if (args.length == 5 && args[0].equals("-s")) {
			//5 args (-s stridx endidx )
			RoadMap map = new RoadMap();
			map.loadMap(args[1]);
			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();

			int startVertexIdx = -1, endVertexIdx = -1, mode = -1;
			try {
				startVertexIdx = Integer.parseInt(args[2]);
				endVertexIdx = Integer.parseInt(args[3]);
				mode = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				System.err.println("Error: start vertex and end vertex must be specified using their indices");
				System.exit(-1);
			}

			if (startVertexIdx < 0 || startVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for start vertex");
				System.exit(-1);
			}

			if (endVertexIdx < 0 || endVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for end vertex");
				System.exit(-1);
			}

			Vertex startVertex = map.getPlace(startVertexIdx);
			Vertex endVertex = map.getPlace(endVertexIdx);
			
			ArrayList<Vertex> path = map.shortestPathWithChargingStations(startVertex, endVertex, mode);
			System.out.println();
			System.out.println("Shortest path between " + startVertex.getName() + " and "
					+ endVertex.getName() + ":");
			map.printPath(path);

		} else {
			printCommandError();
			System.exit(-1);
		}
	}

	// Load matrix entries from a text file
	public void loadMap(String filename) {
		File file = new File(filename);
		places.clear();
		roads.clear();
		try {
			Scanner sc = new Scanner(file);

			// Read the first line: number of vertices and number of edges
			int numVertices = sc.nextInt();
			int numEdges = sc.nextInt();

			for (int i = 0; i < numVertices; ++i) {
				// Read the vertex name and its charing station flag
				String placeName = sc.next();
				Vertex vertex = new Vertex(placeName, i);
				places.add(i, vertex);
			}

			for (int j = 0; j < numEdges; ++j) {
				// Read the edge length and the indices for its two vertices
				int vtxIndex1 = sc.nextInt();
				int vtxIndex2 = sc.nextInt();
				int length = sc.nextInt();
				int risk = sc.nextInt();
				Vertex vtx1 = places.get(vtxIndex1);
				Vertex vtx2 = places.get(vtxIndex2);
				// Add your code here to create a new edge using the information above and add
				// it to roads
				// You should also set up incidentRoads for each vertex
				Edge edge = new Edge(length, risk, vtx1, vtx2);
				roads.add(edge);
				vtx1.addIncidentRoad(edge);
				vtx2.addIncidentRoad(edge);
			}

			sc.close();

			// Add your code here if approparite
		} catch (Exception e) {
			e.printStackTrace();
			places.clear();
			roads.clear();
		}
	}


	//use recursion and 
	public ArrayList<Vertex> shortestPathWithChargingStations(Vertex startVertex, Vertex endVertex, int mode) {
		//mode 0 = shortest path, mode 1 = lowest risk, mode 2 = weighted combination

		// Initialize an empty path
		PriorityQueue<Vertex> path = new PriorityQueue<Vertex>(new CustomComparator());//maybe work ?
		path.add(startVertex);

		// Sanity check for the case where the start vertex and the end vertex are the
		if (startVertex.getIndex() == endVertex.getIndex()) {
			System.out.println("start and end vertices are the same!");
			ArrayList<Vertex> p = new ArrayList<Vertex>();
			p.add(startVertex);
			return p;
		}
		// implement dijkstra
		boolean done = false;
		String word = "";
		
		while (path.size() > 0){
			Vertex currentVertex = path.poll();// why is it called poll???
			ArrayList<Edge> iRoads = currentVertex.getIncidentRoads();
			currentVertex.visited = true;
			for (int i = 0; i < iRoads.size(); i++){
				//get incident vertex
				Edge e = iRoads.get(i);
				Vertex v1 = e.getFirstVertex();
				Vertex v2 = e.getSecondVertex();
				Vertex incidentVertex;
				if (v1 == currentVertex){
					incidentVertex = v2;
				}
				else{
					incidentVertex = v1;
				}
				//depending on mode use different path length!
				int newPathLength = currentVertex.pathLength;
				if (mode == 0){
					word = "length";
					newPathLength = currentVertex.pathLength + e.getLength();
				}
				if (mode == 1){
					word = "risk";
					newPathLength = currentVertex.pathLength + e.getRisk();
				}
				if (mode == 2){
					word = "length and risk";
					newPathLength = currentVertex.pathLength + e.getLength() + e.getRisk();
				}
				// check that we have found a new shortest path to the node
				if ( (incidentVertex.prevVertex == null) || (incidentVertex.pathLength > newPathLength) ){
					incidentVertex.pathLength = newPathLength;
					incidentVertex.prevVertex = currentVertex;
					//next we can simply reinsert the incidentVertex in pq (yes, this leaves a duplicate but this is the cleanest solution)
					path.add(incidentVertex);
				}
			}

			if (currentVertex == endVertex){
				done = true;
			}
		}
		ArrayList<Vertex> arr_path = new ArrayList<Vertex>();
		ArrayList<Vertex> path_ordered = new ArrayList<Vertex>();
		if (done == false){
			System.out.println("\nPath NOT found!!! ERROR!!!");
		}
		else{
			//backtrack
			//System.out.println("\nPath found!");
			int path_length = endVertex.pathLength;
			Vertex currVertex = endVertex;
			while(startVertex != currVertex){
				arr_path.add(currVertex);
				currVertex = currVertex.prevVertex;
			}
			arr_path.add(startVertex);
			
			for(int i = arr_path.size()-1; i>=0; i--){
				path_ordered.add(arr_path.get(i));
			}
			
			

			System.out.println("\nTotal path "+word+": " + path_length);
		}
		return path_ordered;
		
	}

	
	public void printMap() {
		System.out.println("The map contains " + this.numPlaces() + " places and " + this.numRoads() + " roads");
		System.out.println();

		System.out.println("Places:");

		for (Vertex v : places) {
			System.out.println("- name: " + v.getName());
		}

		System.out.println();
		System.out.println("Roads:");

		for (Edge e : roads) {
			System.out.println("- (" + e.getFirstVertex().getName() + ", " + e.getSecondVertex().getName()
					+ "), length: " + e.getLength() + ", risk:" +e.getRisk());
		}
	}

	public void printPath(ArrayList<Vertex> path) {
		System.out.print("(  ");

		for (Vertex v : path) {
			System.out.print(v.getName() + "  ");
		}

		System.out.println(")");
	}

	public int numPlaces() {
		return places.size();
	}

	public int numRoads() {
		return roads.size();
	}

	public Vertex getPlace(int idx) {
		return places.get(idx);
	}

	private ArrayList<Vertex> places;
	private ArrayList<Edge> roads;
}