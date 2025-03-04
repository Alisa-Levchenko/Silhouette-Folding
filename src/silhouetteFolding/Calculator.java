package Polygons;

public class Calculator {
	
	// Height of any triangle from Base p1, p2 to the height of the point p3
	public static double calculateHeight(Coordinates p1, Coordinates p2, Coordinates p3) {
		double side = Calculator.distance(p1,p2);
		double area = Calculator.calculateArea(p1, p2, p3);
		return (area*2) / side;
	}
	
    // Function to calculate the height of the triangle corresponding to the longest side 
	public static double calculateMinimumHeight(Coordinates p1, Coordinates p2, Coordinates p3) {
        // Berechnung der Längen der drei Seiten
        double side1 = distance(p1, p2);
        double side2 = distance(p2, p3);
        double side3 = distance(p3, p1);

        // Bestimme die längste Seite
        double base = Math.max(side1, Math.max(side2, side3));


        // Berechne die Höhe zur längsten Seite
        double area = calculateArea(p1, p2, p3);
        double height = (2 * area) / base;

        return height;
    }

    // Distanz zwischen zwei Punkten
    public static double distance(Coordinates p1, Coordinates p2) {
        return Math.sqrt(Math.pow(p2.x_coordinate - p1.x_coordinate, 2) + Math.pow(p2.y_coordinate - p1.y_coordinate, 2));
    }

    // Fläche eines Dreiecks, Formel für die Fläche eines Dreiecks aus drei Punkten
    public static double calculateArea(Coordinates p1, Coordinates p2, Coordinates p3) {
        return Math.abs(p1.x_coordinate * (p2.y_coordinate - p3.y_coordinate) + p2.x_coordinate * (p3.y_coordinate - p1.y_coordinate) + p3.x_coordinate * (p1.y_coordinate - p2.y_coordinate)) / 2.0;
    }
	
    // Function to calculate the angle between two vectors formed at Point_2
    public static double calculation_of_angle(Coordinates Point_1, Coordinates Point_2, Coordinates Point_3) {
        // Calculate the vector from Point_2 to Point_1
        double x1 = Point_1.x_coordinate - Point_2.x_coordinate;
        double y1 = Point_1.y_coordinate - Point_2.y_coordinate;

        // Calculate the vector from Point_2 to Point_3
        double x2 = Point_3.x_coordinate - Point_2.x_coordinate;
        double y2 = Point_3.y_coordinate - Point_2.y_coordinate;

        // Compute the dot product of the two vectors
        double scalar_product = (x1 * x2) + (y1 * y2);

        // Compute the magnitudes of the two vectors
        double magnitude_1 = Math.sqrt(x1 * x1 + y1 * y1);
        double magnitude_2 = Math.sqrt(x2 * x2 + y2 * y2);

        // Calculate the cosine of the angle using the dot product formula
        double cos_theta = scalar_product / (magnitude_1 * magnitude_2);

        // Handle possible floating point errors where cos_theta might exceed the valid range of acos
        cos_theta = Math.min(1.0, Math.max(-1.0, cos_theta));

        // Compute the angle in radians
        double angle = Math.acos(cos_theta);

        // Check for convexity by computing the cross product to determine the sign of the angle
//        double cross_product = (x1 * y2) - (x2 * y1);
//
//        // If the cross product is negative, it indicates a concave angle
//        if (cross_product < 0) {
//            angle = 2 * Math.PI - angle;  // Adjust to get the interior angle
//        }

        // Return the calculated angle in radians
        return angle; //Math.toDegrees(angle)
    }
}