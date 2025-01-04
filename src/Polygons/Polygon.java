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
		System.out.println();
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
		
		if (_outer_face == null || _outer_face.get_inner_component() == null) {
			return 0;
		}
		
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
		
		double d1, d2, d3;
		
		d1 = v.sign(e.get_prev().get_origin(), e.get_origin());
		d2 = v.sign(e.get_origin(), e.get_next().get_origin());
		d3 = v.sign(e.get_next().get_origin(), e.get_prev().get_origin());
		
		boolean has_neg = (d1 < 0 ) || (d2 < 0) || (d3 < 0);
		boolean has_pos = (d1 > 0 ) || (d2 > 0) || (d3 > 0);
		
		return !(has_neg && has_pos);
	}
	
	List<Vertex> ear_tips(){
		
		List<Vertex> e = new ArrayList<Vertex>();
		Half_edge it = _outer_face.get_inner_component();
		
		do {
			if (it.is_origin_convex()) {
				Half_edge sec_it = it.get_next().get_next();

				boolean is_ear_tip = true;
			
				while (sec_it != it.get_prev()) {
					if (is_in_interior(sec_it.get_origin(), it)) {
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
				if (is_in_interior(it.get_origin(), ed)) {
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
	
	Dual_graph triangulation(){
		Dual_graph T = new Dual_graph();
		
		int num = number_of_edges();
		int num_tr = 0;
		
		Polygon new_pol = new Polygon(this);
		
		List<Vertex> ears = new_pol.ear_tips();
		
		HashMap edge_to_tr = new HashMap<Half_edge, Triangle>();
		
		while (num_tr < num - 2) {
			
			Half_edge out = ears.getFirst()._incident_edge;
			double min_angle = out.get_interior_angle();
			double possible_min_angle = min_angle;
			Vertex min_ang_v = out.get_origin();
			Half_edge it = out;

			do {
				it = it.get_next();
				possible_min_angle = it.get_interior_angle();
				if (possible_min_angle < min_angle && ears.contains(it.get_origin())) {
					min_angle = possible_min_angle;
					min_ang_v = it.get_origin();
				}
				
			} while (it != out);
			
			num_tr ++;
			
			if (num_tr != num-2) {
				
				Half_edge act_prev_prev = min_ang_v._incident_edge.get_prev().get_prev();
				Half_edge act_next = min_ang_v._incident_edge.get_next();
			
				// initialisation of triangle
			
				Triangle triangle = new Triangle();
				
				triangle.set_p1(min_ang_v);
				triangle.set_p2(min_ang_v._incident_edge.get_prev().get_origin());
				triangle.set_p3(min_ang_v._incident_edge.get_next().get_origin());
				triangle.set_num(num_tr);
				
				// add this triangle
				T.add_vertex(triangle);
			
				// idea : create HashMap in order to store edges as keys and triangles as values
			
				if (edge_to_tr.containsKey(min_ang_v._incident_edge.get_prev())) {
					T.add_edge(triangle, (Triangle) edge_to_tr.get(min_ang_v._incident_edge.get_prev()));
				}
				
				if (edge_to_tr.containsKey(min_ang_v._incident_edge)) {
					T.add_edge(triangle, (Triangle) edge_to_tr.get(min_ang_v._incident_edge));
				}
			
				// update polygon		
				Half_edge new_edge = new Half_edge(min_ang_v._incident_edge.get_prev().get_origin());
			
				act_prev_prev.set_next(new_edge);
				new_edge.set_next(act_next);
				
				Face f = new Face();
				f.set_inner_components(new_edge);
				new_pol.set_face(f);
				
				edge_to_tr.put(new_edge, triangle);
				
				// update status for v_{i-1} and v_{i+1} in new polygon
				if (is_ear_tip(new_edge.get_origin())) {
					ears.add(new_edge.get_origin());
				}
				
				if (is_ear_tip(act_next.get_origin())) {
					ears.add(act_next.get_origin());
				}
			
				// delete 
			
				ears.remove(min_ang_v);
			}
			
			else {
				new_pol.set_num(num_tr);
				Triangle triangle = new Triangle();
				
				Half_edge e = new_pol.get_outer_face().get_inner_component();
				
				triangle.set_p1(e.get_origin());
				triangle.set_p2(e.get_next().get_origin());
				triangle.set_p3(e.get_prev().get_origin());
				triangle.set_num(num_tr);
				
				T.add_vertex(triangle);
				
				Half_edge last_tr_e = new_pol._outer_face.get_inner_component();
				if (edge_to_tr.containsKey(last_tr_e)) {
					T.add_edge(triangle, (Triangle) edge_to_tr.get(last_tr_e));
				}
				if (edge_to_tr.containsKey(last_tr_e.get_next())) {
					T.add_edge(triangle, (Triangle) edge_to_tr.get(last_tr_e.get_next()));
				}
				if (edge_to_tr.containsKey(last_tr_e.get_prev())) {
					T.add_edge(triangle, (Triangle) edge_to_tr.get(last_tr_e.get_prev()));
				}
			}
			
		}
		
		return T;
	}
	
	Dual_graph_general triangulation_general() {
		
		Dual_graph_general T = new Dual_graph_general();
		
		int num = number_of_edges();
		int num_tr = 0;
		
		Polygon new_pol = new Polygon(this);
		
		List<Vertex> ears = new_pol.ear_tips();
		
		HashMap edge_to_tr = new HashMap<Half_edge, Polygon>();
		
		while (num_tr < num - 2) {
			
			Half_edge out = ears.getFirst()._incident_edge;
			double min_angle = out.get_interior_angle();
			double possible_min_angle = min_angle;
			Vertex min_ang_v = out.get_origin();
			Half_edge it = out;

			do {
				it = it.get_next();
				possible_min_angle = it.get_interior_angle();
				if (possible_min_angle < min_angle && ears.contains(it.get_origin())) {
					min_angle = possible_min_angle;
					min_ang_v = it.get_origin();
				}
				
			} while (it != out);
			
			num_tr ++;
			
			if (num_tr != num-2) {
				// initialisation of triangle
			
				Half_edge act_prev_prev = min_ang_v._incident_edge.get_prev().get_prev();
				Half_edge act_next = min_ang_v._incident_edge.get_next();
			
				Half_edge clone_prev = new Half_edge(min_ang_v._incident_edge.get_prev());
				Half_edge clone_this = new Half_edge(min_ang_v._incident_edge);
			
				Vertex new_v_next = new Vertex(act_next.get_origin());
				Half_edge new_edge_tr = new Half_edge(new_v_next);
			
				// initialisation of triangle
			
				clone_prev.set_next(clone_this);
				clone_this.set_next(new_edge_tr);
				new_edge_tr.set_next(clone_prev);
			
				Polygon triangle = new Polygon();
				Face f_tr = new Face();
				f_tr.set_inner_components(clone_this);
				
				triangle.set_face(f_tr);
				triangle.set_num(num_tr);
			
				// add this triangle
				T.add_vertex(triangle);
			
				// idea : create HashMap in order to store edges as keys and triangles as values
			
				if (edge_to_tr.containsKey(min_ang_v._incident_edge.get_prev())) {
					T.add_edge(triangle, (Polygon) edge_to_tr.get(min_ang_v._incident_edge.get_prev()));
				}
				
				if (edge_to_tr.containsKey(min_ang_v._incident_edge)) {
					T.add_edge(triangle, (Polygon) edge_to_tr.get(min_ang_v._incident_edge));
				}
			
				// update polygon		
				Half_edge new_edge = new Half_edge(min_ang_v._incident_edge.get_prev().get_origin());
			
				act_prev_prev.set_next(new_edge);
				new_edge.set_next(act_next);
				
				Face f = new Face();
				f.set_inner_components(new_edge);
				new_pol.set_face(f);
				
				edge_to_tr.put(new_edge, triangle);
				
				// update status for v_{i-1} and v_{i+1} in new polygon
				if (is_ear_tip(new_edge.get_origin())) {
					ears.add(new_edge.get_origin());
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
				if (edge_to_tr.containsKey(last_tr_e)) {
					T.add_edge(new_pol, (Polygon) edge_to_tr.get(last_tr_e));
				}
				if (edge_to_tr.containsKey(last_tr_e.get_next())) {
					T.add_edge(new_pol, (Polygon) edge_to_tr.get(last_tr_e.get_next()));
				}
				if (edge_to_tr.containsKey(last_tr_e.get_prev())) {
					T.add_edge(new_pol, (Polygon) edge_to_tr.get(last_tr_e.get_prev()));
				}
			}
			
		}
		
		return T;
	}
	
	List<Help_structure> sequence_of_points() {
		ArrayList<Help_structure> p = new ArrayList<Help_structure>();
		
		Dual_graph t = triangulation();
		
		ArrayList<Triangle> L = t.sequence_of_t();
		
		int num = L.size();
		
		for (int i = 0; i < num; i++) {
			if ((i == 0 || t._edges.get(L.get(i)).size() != 1) && i != num - 1) {
				Edge e = L.get(i).which_edge(L.get(i+1));
				Coordinates s = L.get(i).opposite_vertex(e);
				Help_structure h = new Help_structure();
				h.set_start(s);
				h.set_goal(e);
				p.add(h);
			}
			
			else if (i == num - 1) {
				Help_structure h = new Help_structure();
				Coordinates v = p.getLast().get_goal().get_p1();
				Edge e = L.get(i).opposite_edge(v);
				h.set_goal(e);
				h.set_start(v);
				p.add(h);
			}
			
			else {
				Help_structure h = new Help_structure();
				Coordinates v = p.getLast().get_goal().get_p1();
				Edge e = L.get(i).opposite_edge(v);
				h.set_goal(e);
				h.set_start(v);
				p.add(h);
				
				Help_structure h_n = new Help_structure();
				Edge e_n = L.get(i).which_edge(L.get(i+1));
				Coordinates s_n = L.get(i).opposite_vertex(e_n);
				h_n.set_start(s_n);
				h_n.set_goal(e_n);
				p.add(h_n);
			}
			
		}
		
		return p;
	}
}
