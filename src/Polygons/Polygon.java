package Polygons;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.IndexOutOfBoundsException;
import java.io.FileWriter;
import java.io.IOException;

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
	
	void set_polygon(List<Vertex> l) {
		// be careful: not desired output, if it is general polygon (i. e. crossing edges)
		Half_edge first = new Half_edge(l.get(0));
		for (int i = 1; i < l.size(); i++) {
			Half_edge e = new Half_edge(l.get(i));
			l.get(i - 1)._incident_edge.set_next(e);
		}
		l.getLast()._incident_edge.set_next(l.getFirst()._incident_edge);
		
		double sum_of_angles = 0;
		
		for (int i = 0; i < l.size(); i++) {
			sum_of_angles += l.get(i)._incident_edge.get_interior_angle();
		}
		
		if (sum_of_angles == Math.PI * (l.size()-2)) {
			this.set_face(l.getFirst()._incident_edge.get_twin().get_incident_face());
		}
		else {
			Face new_inn = l.getFirst()._incident_edge.get_twin().get_incident_face();
			Face new_out = l.getFirst()._incident_edge.get_incident_face();
			
			new_inn.set_outer_component(new_inn.get_inner_component().get_twin());
			new_inn.set_inner_components(null);
			
			new_out.set_inner_components(new_out.get_outer_component().get_twin());
			new_out.set_outer_component(null);
			
			this.set_face(new_out);
		}
	}
	
	// Attention: polygon must be given with edges as mountains
	void create_polygon_from_file(File data_of_polygon_in_cp) {
		try {
			
			Scanner reader = new Scanner(data_of_polygon_in_cp);
			List<List<Vertex>> edges = new ArrayList<List<Vertex>>();
			
			// get information about edges 
			while (reader.hasNextLine()) {
				String current_line = reader.nextLine();
				if (current_line.startsWith("2")) {
					String[] edge_information = current_line.split("\\ ");
					try {
						List<Vertex> edge = new ArrayList<Vertex>();
						edge.add(new Vertex(Double.parseDouble(edge_information[1]), Double.parseDouble(edge_information[2])));
						edge.add(new Vertex(Double.parseDouble(edge_information[3]), Double.parseDouble(edge_information[4])));
						edges.add(edge);
					}
					catch(IndexOutOfBoundsException e) {
						System.out.println("File is corrupted");
						e.printStackTrace();
					}
				}
			}
			reader.close();
			
			List<Vertex> oriented_list_of_vertices = new ArrayList<Vertex>();
			int number_of_edges = edges.size();
			
			for (int i = 0; i < number_of_edges - 1; i++) {
				if (i == 0) {
					oriented_list_of_vertices.add(edges.get(0).get(0));
					oriented_list_of_vertices.add(edges.get(0).get(1));
					edges.remove(0);
				}
				else {
					boolean found = false;
					int it = 0;
					while (!found && it < edges.size()) {
						if (oriented_list_of_vertices.getLast().equals(edges.get(it).get(0))) {
							found = true;
						}
						else if (oriented_list_of_vertices.getLast().equals(edges.get(it).get(1))) {
							found = true;
						}
						it++;
					}
					
					if (found) {
						if (oriented_list_of_vertices.getLast().equals(edges.get(it-1).get(0))) {
							oriented_list_of_vertices.add(edges.get(it-1).get(1));
						}
						else {
							oriented_list_of_vertices.add(edges.get(it-1).get(0));
						}
						edges.remove(it-1);
					}
				}
			}
			
			this.set_polygon(oriented_list_of_vertices);
			
		} catch (FileNotFoundException e) {
			System.out.println("File was not given");
			e.printStackTrace();
		}
		
	}
	
	void create_file() {
		try {
			FileWriter writer = new FileWriter("polygon.cp");
			Half_edge it = this._outer_face.get_inner_component();
			
			do {
				double x_coord_f = it.get_origin().x_coordinate;
				if (x_coord_f >= 200.0 || x_coord_f <= -200.0) {
					throw new IOException();
				}
				double y_coord_f = it.get_origin().y_coordinate;
				if (y_coord_f >= 200.0 || y_coord_f <= -200.0) {
					throw new IOException();
				}
				it = it.get_next();
				double x_coord_s = it.get_origin().x_coordinate;
				if (x_coord_s >= 200.0 || x_coord_s <= -200.0) {
					throw new IOException();
				}
				double y_coord_s = it.get_origin().y_coordinate;
				if (x_coord_s >= 200.0 || x_coord_s <= -200.0) {
					throw new IOException();
				}
				writer.write("2" + " " + x_coord_f + " " + y_coord_f + " " + x_coord_s + " " + y_coord_s + "\n");
			} while (it != this._outer_face.get_inner_component());
			
			writer.write("1 -200.0 -200.0 -200.0 200.0\n");
			writer.write("1 -200.0 -200.0 200.0 -200.0\n");
			writer.write("1 200.0 200.0 -200.0 200.0\n");
			writer.write("1 200.0 200.0 200.0 -200.0\n");
			
			writer.close();
		} catch (IOException e) {
			System.out.println("An error occupied: point was out of range of the square");
			e.printStackTrace();
		}
	}
	
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
				
				new_pol.set_face(new_edge.get_twin().get_incident_face());
				
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
				
				new_pol.set_face(new_edge.get_twin().get_incident_face());
				
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
		if (num > 1) {
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
		} 
		
		else {
			Help_structure h = new Help_structure();
			Coordinates s = L.getFirst().get_p1();
			Edge e = L.getFirst().opposite_edge(s);
			h.set_start(s);
			h.set_goal(e);
			p.add(h);
		}
		
		return p;
	}
}
