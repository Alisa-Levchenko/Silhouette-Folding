package Polygons;

public class Coordinates {
	
	protected double x_coordinate;
	protected double y_coordinate;
	
	public Coordinates(double x, double y) {
		x_coordinate = x;
		y_coordinate = y;
	}
	
	double sign(Coordinates p2, Coordinates p3) {
		return (x_coordinate - p3.x_coordinate) * (p2.y_coordinate - p3.y_coordinate) - (p2.x_coordinate - p3.x_coordinate) * (y_coordinate - p3.y_coordinate);
	}
}
