package Polygons;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Polygon {
	
	private Face _outer_face;
	private int _num;
	
	Polygon(){}
	
	void print_polygon() {
		// I will just print half_edges, nothing more
		System.out.println("Printing polygon");
		System.out.println("_____________");
		_outer_face.print_face();
		System.out.println("_____________");
		System.out.println();
	}
	
	void set_face(Face f) {
		_outer_face = f;
	}
	
	Face get_outer_face(){
		return _outer_face;
	}
	
	void set_num(int n) {
		_num = n;
	}
	
	int get_num() {
		return _num;
	}
	
	Polygon(Polygon pol){
		_outer_face = new Face(pol._outer_face);
	}
	
	void read_file() {}
	
	int number_of_edges() {
		int i = 0;
		Half_edge it = _outer_face.get_inner_component();
		do {
			i++;
			it = it.get_next();
		} while (it != _outer_face.get_inner_component());
		return i;
	}
	
	// only for triangles
	
	boolean is_in_interior(Vertex v, Half_edge e) {
		Half_edge it = e;
		
		for (int i = 0; i < 1; i++){
			
			double first = (it.get_next().get_origin().x_coordinate - it.get_origin().x_coordinate) * (v.x_coordinate - it.get_origin().x_coordinate);
			double second = (it.get_next().get_origin().y_coordinate - it.get_origin().y_coordinate) * (v.y_coordinate - it.get_origin().y_coordinate);
			double scalar_product = first + second;
			
			if (scalar_product <= 0) {
				return false;
			}
			
			it = it.get_next();
		}
		
		double first = (e.get_origin().x_coordinate - it.get_origin().x_coordinate) * (v.x_coordinate - it.get_origin().x_coordinate);
		double second = (e.get_origin().y_coordinate - it.get_origin().y_coordinate) * (v.y_coordinate - it.get_origin().y_coordinate);
		double scalar_product = first + second;
		
		if (scalar_product < 0) {
			return false;
		}
		
		return true;
	}
	
	List<Vertex> ear_tips(){
		
		List<Vertex> e = new ArrayList<Vertex>();
		Half_edge it = _outer_face.get_inner_component();
		
		do {
			if (it.is_origin_convex()) {
				Half_edge sec_it = it.get_next();

				boolean is_ear_tip = true;
				
				// TODO: out of range FIX
				while (sec_it.get_next() != it.get_prev()) {
					if (!is_in_interior(it.get_origin(), sec_it)) {
						is_ear_tip = false;
					}
					sec_it = sec_it.get_next();
				}
				
				if (is_ear_tip) {
					e.addLast(it.get_origin());
				}
			}
			it = it.get_next();
		} while (it != _outer_face.get_inner_component());
		return e;
	}
	
	boolean is_ear_tip(Vertex v) {
		
		Half_edge ed = v._incident_edge;
		
		if (ed.is_origin_convex()) {
			Half_edge it = ed.get_next().get_next();
				
			while (it != ed.get_prev()) {
				if (!is_in_interior(it.get_origin(), ed)) {
					return false;
				}
				it = it.get_next();
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	Dual_graph triangulation() {
		
		Dual_graph T = new Dual_graph();
		
		int num = number_of_edges();
		int num_tr = 0;
		
		Polygon new_pol = new Polygon(this);
		
		List<Vertex> ears = new_pol.ear_tips();
		Half_edge out = ears.getFirst()._incident_edge;
		
		HashMap edge_to_tr = new HashMap<Half_edge, Polygon>();
		
		while (num_tr < num - 2) {
			
			double min_angle = out.get_interior_angle();
			double possible_min_angle = min_angle;
			Vertex min_ang_v = out.get_origin();
			Half_edge it = out;
			if (out != null) {
				while (it.get_next() != out) {
					it = it.get_next();
					possible_min_angle = it.get_interior_angle();
					if (possible_min_angle < min_angle && ears.contains(it.get_origin())) {
						min_angle = possible_min_angle;
						min_ang_v = it.get_origin();
					}
				}
			}
			
			num_tr ++;
			
			if (num_tr != num-2) {
				// initialisation of triangle
			
				Half_edge act_prev_prev = min_ang_v._incident_edge.get_prev().get_prev();
				Half_edge act_next = min_ang_v._incident_edge.get_next().get_next();
			
				Half_edge clone_prev = new Half_edge(min_ang_v._incident_edge.get_prev());
				Half_edge clone_this = new Half_edge(min_ang_v._incident_edge);
			
				Vertex new_v_next = new Vertex(act_next.get_origin());
				Half_edge new_edge_tr = new Half_edge(new_v_next);
			
				// initialisation of triangle
			
				clone_prev.set_next(clone_this);
				clone_this.set_next(new_edge_tr);
				new_edge_tr.set_next(clone_this);
			
				Polygon triangle = new Polygon();
				Face f_tr = new Face();
				f_tr.set_inner_components(clone_this);
				
				triangle.set_face(f_tr);
				triangle.set_num(num_tr);
			
				// add this triangle
				T.add_vertex(triangle);
			
				// idea : create HashTable in order to store edges as keys and triangles as values
			
				if (edge_to_tr.get(min_ang_v._incident_edge.get_prev()) != null) {
					T.add_edge(triangle, (Polygon) edge_to_tr.get(min_ang_v._incident_edge.get_next()));
				}
				
				if (edge_to_tr.get(min_ang_v._incident_edge) != null) {
					T.add_edge(triangle, (Polygon) edge_to_tr.get(min_ang_v._incident_edge));
				}
			
				// update polygon		
				Half_edge new_edge = new Half_edge(min_ang_v._incident_edge.get_prev().get_origin());
			
				act_prev_prev.set_next(new_edge);
				new_edge.set_next(act_next);
			
				edge_to_tr.put(new_edge, triangle);
				
				// update status for v_{i-1} and v_{i+1} in new polygon
				if (is_ear_tip(act_prev_prev.get_origin())) {
					ears.add(act_prev_prev.get_origin());
				}
				
				if (is_ear_tip(act_next.get_origin())) {
					ears.add(act_next.get_origin());
				}
			
				// delete 
			
				ears.remove(min_ang_v);
			}
			
			else {
				new_pol.set_num(num_tr);
				T.add_vertex(new_pol);
				Half_edge last_tr_e = new_pol._outer_face.get_inner_component();
				if ( edge_to_tr.get(last_tr_e) != null) {
					T.add_edge(new_pol, (Polygon) edge_to_tr.get(last_tr_e));
				}
				if ( edge_to_tr.get(last_tr_e.get_next()) != null) {
					T.add_edge(new_pol, (Polygon) edge_to_tr.get(last_tr_e.get_next()));
				}
				if ( edge_to_tr.get(last_tr_e.get_prev()) != null) {
					T.add_edge(new_pol, (Polygon) edge_to_tr.get(last_tr_e.get_prev()));
				}
			}
			
		}
		
		return T;
	}
}
