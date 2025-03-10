package silhouetteFolding;

public class Help_structure {
	private GeometricEdge goal = new GeometricEdge();
	private Coordinates start = new Coordinates();
	
	public Help_structure(){}
	
	public void set_goal(GeometricEdge e) {
		goal = e;
	}
	
	public void set_goal(Coordinates p1, Coordinates p2) {
		goal.set_p1(p1);
		goal.set_p2(p2);
	}
	
	public void set_start(Coordinates p) {
		start = p;
	}
	
	public GeometricEdge get_goal() {
		return goal;
	}
	
	public Coordinates get_start() {
		return start;
	}
	
	public void print_str() {
		System.out.println("_____________");
		System.out.println("Start vertex: ");
		start.print_coords();
		System.out.println("Goal edge: ");
		goal.print();
		System.out.println("_____________");
	}
}
