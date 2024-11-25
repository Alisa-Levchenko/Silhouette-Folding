package Polygons;

import java.util.Vector;

public class Polygon {
	Vector<Vertex> vertices;
	Vector<Geometric_Edge> edges;
	
	public Polygon(Vector<Vertex> v, Vector<Geometric_Edge> e) { // already sorted
		vertices = v;
		edges = e;
	}
	
	public Polygon() {
		vertices = null;
		edges = null;
	}
	
	void set_vertices(Vector<Vertex> v) {
		vertices = v;
	}
	
	void set_edges(Vector<Geometric_Edge> e) {
		edges = e;
	}
	
	
	boolean is_vertex_convex(Vertex e_1, Vertex e_2, Vertex e_3) {
		double area = e_1.x_coordinate * (e_3.y_coordinate - e_2.y_coordinate);
		area += e_2.x_coordinate * (e_1.y_coordinate - e_3.y_coordinate);
		area += e_3.x_coordinate * (e_2.y_coordinate - e_1.y_coordinate);
		return area < 0;
	}
	
	boolean is_point_in_interior(Vertex v) { //this method is only for triangles
		for (int i = 0; i < vertices.size(); i++) {
			
			if (i < vertices.size() - 1) {
				if (is_vertex_convex(vertices.elementAt(i), v, vertices.elementAt(i+1))) { //either _ or !
					return false;
				}
			}
			
			else {
				if (is_vertex_convex(vertices.elementAt(i), v, vertices.elementAt(0))) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Dual Graph is better than Vector<Polygon>
	
	Vector<Polygon> triangulation_simple_polygon(){
		
		Polygon pol = new Polygon(vertices, edges);
		Vector<Integer> convex = new Vector<>(); // potential ear tips
		Vector<Integer> reflex = new Vector<>(); // needed for check if convex edge is also a ear tip
		
		for (int i = 0; i < vertices.size(); i++) { 
			if (i < vertices.size() - 1 && i != 0) {
				if (this.is_vertex_convex(vertices.elementAt(i-1), vertices.elementAt(i), vertices.elementAt(i+1))) {
					convex.addElement(i);
				}
				else {
					reflex.addElement(i);
				}
			}
			
			else if (i == 0) {
				if (this.is_vertex_convex(vertices.elementAt(vertices.size() - 1), vertices.elementAt(i), vertices.elementAt(i+1))) {
					convex.addElement(i);
				}
				else {
					reflex.addElement(i);
				}
			}
			
			else {
				if (this.is_vertex_convex(vertices.elementAt(i - 1), vertices.elementAt(i), vertices.elementAt(0))) {
					convex.addElement(i);
				}
				else {
					reflex.addElement(i);
				}
			}
		}
		
		Vector<Vertex> ear_tips = new Vector<Vertex>();
		
		Polygon possible_triangle = new Polygon();
		
		for (int i = 0; i < convex.size() ;i ++) {
			if (convex.elementAt(i) != 0 && convex.elementAt(i) != vertices.size()-1) {
				
				Geometric_Edge e_1 = new Geometric_Edge(vertices.elementAt(convex.elementAt(i)-1), vertices.elementAt(convex.elementAt(i)));
				Geometric_Edge e_2 = new Geometric_Edge(vertices.elementAt(convex.elementAt(i)), vertices.elementAt(convex.elementAt(i)+1));
				Geometric_Edge e_3 = new Geometric_Edge(vertices.elementAt(convex.elementAt(i)+1), vertices.elementAt(convex.elementAt(i)-1));
				
				Vector<Geometric_Edge> edgs = new Vector<Geometric_Edge>();
				Vector<Vertex> verts = new Vector<Vertex>();
				
				edgs.addElement(e_1);
				edgs.addElement(e_2);
				edgs.addElement(e_3);
				
				verts.addElement(vertices.elementAt(convex.elementAt(i)-1));
				verts.addElement(vertices.elementAt(convex.elementAt(i)));
				verts.addElement(vertices.elementAt(convex.elementAt(i)+1));
				
				possible_triangle.set_edges(edgs);
				possible_triangle.set_vertices(verts);
			}
			else if (convex.elementAt(i) == 0) {
				Geometric_Edge e_1 = new Geometric_Edge(vertices.elementAt(vertices.size()-1), vertices.elementAt(convex.elementAt(i)));
				Geometric_Edge e_2 = new Geometric_Edge(vertices.elementAt(convex.elementAt(i)), vertices.elementAt(convex.elementAt(i)+1));
				Geometric_Edge e_3 = new Geometric_Edge(vertices.elementAt(convex.elementAt(i)+1), vertices.elementAt(convex.elementAt(i)-1));
				
				Vector<Geometric_Edge> edgs = new Vector<Geometric_Edge>();
				Vector<Vertex> verts = new Vector<Vertex>();
				
				edgs.addElement(e_1);
				edgs.addElement(e_2);
				edgs.addElement(e_3);
				
				verts.addElement(vertices.elementAt(vertices.size()- 1));
				verts.addElement(vertices.elementAt(0));
				verts.addElement(vertices.elementAt(1));
				
				possible_triangle.set_edges(edgs);
				possible_triangle.set_vertices(verts);
			}
			else {
				Geometric_Edge e_1 = new Geometric_Edge(vertices.elementAt(convex.elementAt(i)-1), vertices.elementAt(convex.elementAt(i)));
				Geometric_Edge e_2 = new Geometric_Edge(vertices.elementAt(convex.elementAt(i)), vertices.elementAt(0));
				Geometric_Edge e_3 = new Geometric_Edge(vertices.elementAt(0), vertices.elementAt(convex.elementAt(i)-1));
				
				Vector<Geometric_Edge> edgs = new Vector<Geometric_Edge>();
				Vector<Vertex> verts = new Vector<Vertex>();
				
				edgs.addElement(e_1);
				edgs.addElement(e_2);
				edgs.addElement(e_3);
				
				verts.addElement(vertices.elementAt(convex.elementAt(i)-1));
				verts.addElement(vertices.elementAt(convex.elementAt(i)));
				verts.addElement(vertices.elementAt(0));
				
				possible_triangle.set_edges(edgs);
				possible_triangle.set_vertices(verts);
			}
			
			boolean ear_tip = true;
			
			for (int j = 0; j < reflex.size(); j++) {
				if (convex.elementAt(i) != 0 && convex.elementAt(i) != vertices.size() - 1) {
					if (vertices.elementAt(convex.elementAt(i) - 1) != vertices.elementAt(reflex.elementAt(j)) || vertices.elementAt(convex.elementAt(i) + 1) != vertices.elementAt(reflex.elementAt(j))) {
						if (possible_triangle.is_point_in_interior(vertices.elementAt(j))) {
							ear_tip = false;
						}
					}
				}
				else if (convex.elementAt(i) == 0) {
					if (vertices.elementAt(vertices.size() - 1) != vertices.elementAt(reflex.elementAt(j)) || vertices.elementAt(convex.elementAt(i) + 1) != vertices.elementAt(reflex.elementAt(j))) {
						if (possible_triangle.is_point_in_interior(vertices.elementAt(j))) {
							ear_tip = false;
						}
					}
				}
				else {
					if (vertices.elementAt(convex.elementAt(i) - 1) != vertices.elementAt(reflex.elementAt(j)) || vertices.elementAt(0) != vertices.elementAt(reflex.elementAt(j))) {
						if (possible_triangle.is_point_in_interior(vertices.elementAt(j))) {
							ear_tip = false;
						}
					}
				}
			}
			if (ear_tip) {
				ear_tips.addElement(vertices.elementAt(convex.elementAt(i)));
			}
		}
		
		// so here we are finished with ear_tips
		
		//assume ear_tips is sorted. Later I will add sorting here.
		
		
		int triang = 0;
		Vector<Polygon> triangulation = new Vector<Polygon>();
		while (triang < vertices.size() - 2){
			//TODO
		}
		
		return triangulation;
	}
	
	Vector<Polygon> triangulation(){
		Polygon pos = new Polygon(vertices, edges);
		
		// TODO check if polygon is simple
		// if not, then add edges/vertices
		
		return pos.triangulation_simple_polygon();
	}
}
