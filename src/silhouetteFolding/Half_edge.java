package silhouetteFolding;
import java.lang.Math;

public class Half_edge{
	
	private Vertex _origin;
	private Half_edge _twin;
	private Face _incident_face;
	private Half_edge _next;
	private Half_edge _prev;
	
	// needed for triangulation, given in rad
	
	private double _interior_angle;
	
	// needed for computation of max width
	
	private double _length;
	
	Half_edge(){}
	
	void print_edge(boolean full_pr) {
		
		System.out.println("Printing edge");
		System.out.println("_____________");
		System.out.println("origin: ");
		_origin.print_vertex_coord();
		System.out.println();
		
		System.out.println("Length: " + _length);
		System.out.println("Angle: " + _interior_angle);
		System.out.println();
		
		if (full_pr) {
			if (_next != null) {
				System.out.println("Pointed to: ");
				_next._origin.print_vertex_coord();
				System.out.println();
			}
			
			else {
				System.out.println("This edge is final");
			}
			
			if (_prev != null) {
				System.out.println("Previous's origin: ");
				_prev._origin.print_vertex_coord();
				System.out.println();
			}
			
			else {
				System.out.println("This edge is initial");
			}
			
			if (_incident_face != null) {
				System.out.println("This edge forms a face");
			}
		}
		System.out.println("_____________");
		System.out.println();
	}
	
	Half_edge(double x, double y){
		Vertex v = new Vertex(x, y);
		_origin = v;
		v._incident_edge = this;
	}
	
	Half_edge(Vertex v) {
		_origin = v;
		v._incident_edge = this;
	}
	
	
	Half_edge(Half_edge e){ 
		
		// ONLY FOR CLONING
		Vertex _new_v = new Vertex(e._origin);
		_origin = _new_v;
		_origin._incident_edge = this;
	}
	
	boolean is_origin_convex() {
		return _interior_angle < Math.PI;
	}
	
	Half_edge get_next() {
		return _next;
	}
	
	Half_edge get_twin() {
		return _twin;
	}
	
	Face get_incident_face() {
		return _incident_face;
	}
	
	Half_edge get_prev() {
		return _prev;
	}
	
	double get_lenght() {
		return _length;
	}
	
	double get_interior_angle() {
		return _interior_angle;
	}
	
	Vertex get_origin() {
		return _origin;
	}
	
	void set_next(Vertex v) {
		Half_edge e = new Half_edge(v);
		set_next(e);
	}
	
	void calculation_of_angle() {
		if (_prev != null) {
			
			double first = (_next._origin.x_coordinate - _origin.x_coordinate) * (_prev._origin.x_coordinate - _origin.x_coordinate);
			double second = (_next._origin.y_coordinate - _origin.y_coordinate) * (_prev._origin.y_coordinate - _origin.y_coordinate);
			double scalar_product = first + second;
			
			_interior_angle = Math.acos(scalar_product / (_length * _prev._length));
			
			// check if convex
			
			double x_1 = _next._origin.x_coordinate - _origin.x_coordinate;
			double y_1 = _next._origin.y_coordinate - _origin.y_coordinate;

			double x_2 = _prev._origin.x_coordinate - _origin.x_coordinate;
			double y_2 = _prev._origin.y_coordinate - _origin.y_coordinate;
			
			double area = x_1 * y_2 - x_2 * y_1;
			
			if (area < 0) {
				_interior_angle = 2 * Math.PI - _interior_angle;
			}
		}
	}
	
	void set_next(Half_edge _new) {
		
		_next = _new;
		_next._prev = this;
		
		double x = Math.pow(_origin.x_coordinate - _next._origin.x_coordinate, 2);
		double y = Math.pow(_origin.y_coordinate - _next._origin.y_coordinate, 2);
		_length = Math.sqrt(x + y);
		
		this.calculation_of_angle();

		// need to define twin
		Half_edge twin = new Half_edge(_next._origin.x_coordinate, _next._origin.y_coordinate);
		_twin = twin;
		
		if (_next._next != null) {
			_next.calculation_of_angle();
			_twin._interior_angle = 2 * Math.PI - _next._interior_angle;
		}
		
		if (_prev != null) {
			_twin._next = _prev._twin;
			_prev._twin._interior_angle = 2 * Math.PI - _interior_angle;
		}
		
		if (_next._next != null) {
			_twin._prev = _next._twin;
			_next._twin._next = _twin;
		}
		_twin._twin = this;
		
		// I don't know if it could be any faster but I think it is necessary every time to run and test
		// if there appears a new face (general case)
		// P.S. in computations, when we know that some of the faces would one more time appear
		// after we add just 1-2 half edges might be implemented another method
		// (without going through all of the half edges, O(1))
		
		Half_edge it = _next;
		
		boolean is_cycle = true;
		
		while (it != this && is_cycle) {
			
			if (it.get_next() == null) {
				is_cycle = false;
				it = this;
			}
			
			it = it.get_next();
		}
		
		if (is_cycle) {
			
			it = this;
			
			Face f = new Face();
			f.set_outer_component(this);
			
			Face f_tw = new Face();
			f_tw.set_inner_components(this);
			
			do {
				it._incident_face = f;
				it._twin._incident_face = f_tw;
				it = it.get_next();
			} while (it != this);
		}
	}
	
}
