package silhouetteFolding;

public class Vertex extends Coordinates{
	
	protected Half_edge _incident_edge;
	protected boolean is_ear = false;
	
	Vertex (Vertex v){
		
		// for cloning 
		super(v.x_coordinate, v.y_coordinate);
		
	}
	
	boolean equals(Vertex p) {
		return super.equals(p);
	}
	
	Vertex (double x, double y) {
		super(x,y);
	}
	
	void set_incident_edge(Half_edge e) {
		_incident_edge = e;
	}
	
	void print_vertex_coord() {
		System.out.println("x-coordinate: " + x_coordinate);
		System.out.println("y-coordinate: " + y_coordinate);
	}
}
