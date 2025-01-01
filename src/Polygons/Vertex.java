package Polygons;

public class Vertex extends Coordinates{
	
	protected Half_edge _incident_edge;
	
	Vertex (Vertex v){
		
		// for cloning 
		
		super(v.x_coordinate, v.y_coordinate);
		_incident_edge = v._incident_edge;
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
