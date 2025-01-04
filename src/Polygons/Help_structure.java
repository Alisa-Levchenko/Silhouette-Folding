package Polygons;

public class Help_structure {
	private Edge goal = new Edge();
	private Coordinates start = new Coordinates();
	
	Help_structure(){}
	
	void set_goal(Edge e) {
		goal = e;
	}
	
	void set_goal(Coordinates p1, Coordinates p2) {
		goal.set_p1(p1);
		goal.set_p2(p2);
	}
	
	void set_start(Coordinates p) {
		start = p;
	}
	
	Edge get_goal() {
		return goal;
	}
	
	Coordinates get_start() {
		return start;
	}
	
	void print_str() {
		System.out.println("_____________");
		System.out.println("Start vertex: ");
		start.print_coords();
		System.out.println("Goal edge: ");
		goal.print();
		System.out.println("_____________");
	}
}
