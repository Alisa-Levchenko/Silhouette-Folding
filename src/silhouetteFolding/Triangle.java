package silhouetteFolding;

public class Triangle{
	
	private Coordinates p1;
	private Coordinates p2;
	private Coordinates p3;
	
	// add goal edge and start vertex
	
	private int _num;
	
	public Triangle(){}
	
	void print_triangle() {
		System.out.println("_____________");
		System.out.println();
		
		System.out.println("First point: ");
		
		p1.print_coords();
		
		System.out.println("Second point: ");
		
		p2.print_coords();
		
		System.out.println("Third point: ");
		
		p3.print_coords();
		
		System.out.println("_____________");
	}
	
	void set_num(int n) {
		_num = n;
	}
	
	int get_num() {
		return _num;
	}
	
	Coordinates get_p1() {
		return p1;
	}
	
	Coordinates get_p2() {
		return p2;
	}
	
	Coordinates get_p3() {
		return p3;
	}
	
	void set_p1(double x, double y) {
		p1 = new Coordinates(x, y);
	}
	
	void set_p1(Coordinates point) {
		p1 = point;
	}
	
	void set_p2(double x, double y) {
		p2 = new Coordinates(x, y);
	}
	
	void set_p2(Coordinates point) {
		p2 = point;
	}
	
	void set_p3(double x, double y) {
		p3 = new Coordinates(x, y);
	}
	
	void set_p3(Coordinates point) {
		p3 = point;
	}
	
	GeometricEdge which_edge(Triangle t) {
		GeometricEdge e = new GeometricEdge();
		
		if (t.get_p1().x_coordinate == p1.x_coordinate && t.get_p1().y_coordinate == p1.y_coordinate) {
			e.set_p1(p1);
			
			if (t.get_p2().x_coordinate == p2.x_coordinate && t.get_p2().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p3().x_coordinate == p2.x_coordinate && t.get_p3().y_coordinate == p2.y_coordinate){
				e.set_p2(p2);
			}
			else {
				e.set_p2(p3);
			}
		}
		else if (t.get_p2().x_coordinate == p2.x_coordinate && t.get_p2().y_coordinate == p2.y_coordinate) {
			e.set_p1(p2);
			
			if (t.get_p1().x_coordinate == p3.x_coordinate && t.get_p1().y_coordinate == p3.y_coordinate) {
				e.set_p2(p3);
			}
			else if (t.get_p3().x_coordinate == p1.x_coordinate && t.get_p3().y_coordinate == p1.y_coordinate){
				e.set_p2(p1);
			}
			else {
				e.set_p2(p3);
			}
		}
		else if (t.get_p3().x_coordinate == p3.x_coordinate && t.get_p3().y_coordinate == p3.y_coordinate) {
			e.set_p1(p3);
			
			if (t.get_p1().x_coordinate == p2.x_coordinate && t.get_p1().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p3().x_coordinate == p1.x_coordinate && t.get_p3().y_coordinate == p1.y_coordinate){
				e.set_p2(p1);
			}
			else {
				e.set_p2(p2);
			}
		}
		else if (t.get_p1().x_coordinate == p2.x_coordinate && t.get_p1().y_coordinate == p2.y_coordinate) {
			e.set_p1(p2);
			
			if (t.get_p2().x_coordinate == p1.x_coordinate && t.get_p2().y_coordinate == p1.y_coordinate) {
				e.set_p2(p1);
			}
			else if (t.get_p2().x_coordinate == p3.x_coordinate && t.get_p2().y_coordinate == p3.y_coordinate) {
				e.set_p2(p3);
			}
			else if (t.get_p3().x_coordinate == p1.x_coordinate && t.get_p3().y_coordinate == p1.y_coordinate) {
				e.set_p2(p1);
			}
			else {
				e.set_p2(p3);
			}
		}
		
		else if (t.get_p2().x_coordinate == p1.x_coordinate && t.get_p2().y_coordinate == p1.y_coordinate) {
			e.set_p1(p1);
			
			if (t.get_p1().x_coordinate == p2.x_coordinate && t.get_p1().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p3().x_coordinate == p2.x_coordinate && t.get_p3().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p1().x_coordinate == p3.x_coordinate && t.get_p1().y_coordinate == p3.y_coordinate) {
				e.set_p2(p3);
			}
			else {
				e.set_p2(p3);
			}
		}
		else if (t.get_p1().x_coordinate == p3.x_coordinate && t.get_p1().y_coordinate == p3.y_coordinate) {
			e.set_p1(p3);
			
			if (t.get_p2().x_coordinate == p1.x_coordinate && t.get_p2().y_coordinate == p1.y_coordinate) {
				e.set_p2(p1);
			}
			else if (t.get_p2().x_coordinate == p2.x_coordinate && t.get_p2().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p3().x_coordinate == p1.x_coordinate && t.get_p3().y_coordinate == p1.y_coordinate) {
				e.set_p2(p1);
			}
			else {
				e.set_p2(p2);
			}
		}
		
		else if (t.get_p3().x_coordinate == p1.x_coordinate && t.get_p3().y_coordinate == p1.y_coordinate) {
			e.set_p1(p1);
			
			if (t.get_p1().x_coordinate == p2.x_coordinate && t.get_p1().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p2().x_coordinate == p2.x_coordinate && t.get_p2().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p1().x_coordinate == p3.x_coordinate && t.get_p1().y_coordinate == p3.y_coordinate) {
				e.set_p2(p3);
			}
			else {
				e.set_p2(p3);
			}
		}
		
		else if (t.get_p3().x_coordinate == p2.x_coordinate && t.get_p3().y_coordinate == p2.y_coordinate) {
			e.set_p1(p2);
			
			if (t.get_p2().x_coordinate == p1.x_coordinate && t.get_p2().y_coordinate == p1.y_coordinate) {
				e.set_p2(p1);
			}
			else if (t.get_p2().x_coordinate == p3.x_coordinate && t.get_p2().y_coordinate == p3.y_coordinate) {
				e.set_p2(p3);
			}
			else if (t.get_p1().x_coordinate == p1.x_coordinate && t.get_p1().y_coordinate == p1.y_coordinate) {
				e.set_p2(p1);
			}
			else {
				e.set_p2(p3);
			}
		}
		
		else {
			e.set_p1(p3);
				
			if (t.get_p1().x_coordinate == p2.x_coordinate && t.get_p1().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p3().x_coordinate == p2.x_coordinate && t.get_p3().y_coordinate == p2.y_coordinate) {
				e.set_p2(p2);
			}
			else if (t.get_p1().x_coordinate == p1.x_coordinate && t.get_p1().y_coordinate == p1.y_coordinate) {
				e.set_p2(p1);
			}
			else {
				e.set_p2(p2);
			}
		}
		
		return e;
	}

	Coordinates opposite_vertex(GeometricEdge e) {
		if (p1.x_coordinate == e.get_p1().x_coordinate && p1.y_coordinate == e.get_p1().y_coordinate) {
			if (p2.x_coordinate == e.get_p2().x_coordinate && p2.y_coordinate == e.get_p2().y_coordinate) {
				return p3;
			}
			else {
				return p2;
			}
		}
		else if (p2.x_coordinate == e.get_p1().x_coordinate && p2.y_coordinate == e.get_p1().y_coordinate) {
			if (p1.x_coordinate == e.get_p2().x_coordinate && p1.y_coordinate == e.get_p2().y_coordinate) {
				return p3;
			}
			else {
				return p1;
			}
		}
		else {
			if (p1.x_coordinate == e.get_p2().x_coordinate && p1.y_coordinate == e.get_p2().y_coordinate) {
				return p2;
			}
			else {
				return p1;
			}
		}
	}

	GeometricEdge opposite_edge(Coordinates v) {
		if (v.x_coordinate == p1.x_coordinate && v.y_coordinate == p1.y_coordinate) {
			return new GeometricEdge(p2, p3);
		}
		else if (v.x_coordinate == p2.x_coordinate && v.y_coordinate == p2.y_coordinate) {
			return new GeometricEdge(p1, p3);
		}
		else {
			return new GeometricEdge(p1, p2);
		}
	}
}
