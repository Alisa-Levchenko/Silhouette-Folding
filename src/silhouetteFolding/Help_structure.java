package silhouetteFolding;

import java.util.Arrays;
import java.util.List;

public class Help_structure {
	private GeometricEdge goal = new GeometricEdge();
	private Coordinates start = new Coordinates();

	public Help_structure() {
	}

	public Help_structure(Coordinates start, GeometricEdge goal) {
		this.goal = goal;
		this.start = start;
	}

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

	public static boolean areTrianglesIdentical(Help_structure t1, Help_structure t2) {
		Coordinates[] tri1 = { t1.get_start(), t1.get_goal().get_p1(), t1.get_goal().get_p2() };
		Coordinates[] tri2 = { t2.get_start(), t2.get_goal().get_p1(), t2.get_goal().get_p2() };
		boolean[] matched = new boolean[3]; // um Duplikate zu vermeiden

		// Für jeden Punkt in t1, suche ein passendes Gegenstück in t2
		for (Coordinates p1 : tri1) {
			boolean found = false;
			for (int i = 0; i < 3; i++) {
				if (!matched[i] && p1.compare(tri2[i])) {
					matched[i] = true;
					found = true;
					break;
				}
			}
			if (!found)
				return false; // kein passender Punkt gefunden
		}

		return true; // alle Punkte stimmen überein
	}

	@Override
	public String toString() {
		return "p1 " + this.get_start() + " p2 " + this.get_goal().get_p1() + " p3 " + this.get_goal().get_p2();
	}

	// p1 start = this.start
	// p3 ziel = next triangle.punkt der nicht start ist
	// p2 startpunkt von next triangle
	public List<Help_structure> splitAtCentroid(Help_structure duplicate) {
		Coordinates goalPoint;
		if (!this.get_start().equals(duplicate.get_goal().get_p1())) {
			goalPoint = new Coordinates(duplicate.get_goal().get_p1().get_x(), duplicate.get_goal().get_p1().get_y());
		} else {
			goalPoint = new Coordinates(duplicate.get_goal().get_p2().get_x(), duplicate.get_goal().get_p2().get_y());
		}
		// Schwerpunkt berechnen
		double gx = (this.get_start().get_x() + this.get_goal().get_p1().get_x() + this.get_goal().get_p2().get_x())/ 3;
		double gy = (this.get_start().get_y() + this.get_goal().get_p1().get_y() + this.get_goal().get_p2().get_y())/ 3;
		Coordinates g = new Coordinates(gx, gy);

		// zus
		//  neue Dreiecke erstellen
		Help_structure t1 = new Help_structure(this.get_start(), new GeometricEdge(g, goalPoint));
		Help_structure t2 = new Help_structure(goalPoint, new GeometricEdge(g, duplicate.get_start()));
		Help_structure t3 = new Help_structure(duplicate.get_start(), new GeometricEdge(g, this.get_start()));
		Help_structure t4 = new Help_structure(g, new GeometricEdge(this.get_start(), goalPoint));

		return Arrays.asList(t1, t2, t3, t4);
	}
}
