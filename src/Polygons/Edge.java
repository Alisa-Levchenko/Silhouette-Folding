package Polygons;

public class Edge {
	private Coordinates p1 = new Coordinates();
	private Coordinates p2 = new Coordinates();
	
	Edge(){}
	
	Edge(Coordinates p_1, Coordinates p_2){
		p1 = p_1;
		p2 = p_2;
	}
	
	void set_p1(Coordinates p) {
		p1 = p;
	}
	
	void set_p1(double x, double y) {
		p1.x_coordinate = x; 
		p1.y_coordinate = y;
	}
	
	Coordinates get_p1() {
		return p1;
	}
	
	void set_p2(Coordinates p) {
		p2 = p;
	}
	
	Coordinates get_p2() {
		return p2;
	}
	
	void set_p2(double x, double y) {
		p2.x_coordinate = x;
		p2.x_coordinate = y;
	}
	
	void print() {
		System.out.println("First point: ");
		p1.print_coords();
		System.out.println("Second point");
		p2.print_coords();
	}
}
