package silhouetteFolding;

public class Coordinates {

	protected double x_coordinate;
	protected double y_coordinate;

	Coordinates() {
	}

	public Coordinates(double x, double y) {
		x_coordinate = x;
		y_coordinate = y;
	}

	boolean equals(Coordinates p) {
		return (p.x_coordinate == x_coordinate && p.y_coordinate == y_coordinate);
	}

	double sign(Coordinates p2, Coordinates p3) {
		return (x_coordinate - p3.x_coordinate) * (p2.y_coordinate - p3.y_coordinate)
				- (p2.x_coordinate - p3.x_coordinate) * (y_coordinate - p3.y_coordinate);
	}

	double get_x() {
		return x_coordinate;
	}

	double get_y() {
		return y_coordinate;
	}

	void print_coords() {
		System.out.println("x-coordinate: " + x_coordinate);
		System.out.println("y-coordinate: " + y_coordinate);
	}

	public boolean compare(Coordinates get_p1) {
		if (y_coordinate == get_p1.y_coordinate && x_coordinate == get_p1.x_coordinate)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "x-coordinate: " + x_coordinate + " y-coordinate: " + y_coordinate;
	}
}
