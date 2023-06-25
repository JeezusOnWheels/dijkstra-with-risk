class Edge {
	public Edge(int roadLength, int risk, Vertex firstPlace, Vertex secondPlace) {
		this.risk = risk;
		length = roadLength;
		incidentPlaces = new Vertex[] { firstPlace, secondPlace };
	}

	public Vertex getFirstVertex() {
		return incidentPlaces[0];
	}

	public Vertex getSecondVertex() {
		return incidentPlaces[1];
	}

	public int getLength() {
		return length;
	}

	public int getRisk(){
		return risk;
	}

	private int length;
	private int risk;
	private Vertex[] incidentPlaces;
}
