package Polygons;

import java.util.Vector;

public class Dual_Graph {
	
	Vector<Polygon> dual_vertices;
	Vector<Edge<Polygon>> dual_edges;
	
	public Dual_Graph(Vector<Polygon> dv, Vector<Edge<Polygon>> de) {
		dual_vertices = dv;
		dual_edges = de;
	}
	
	// TODO
	
	Dual_Graph depth_search() {
		return this;
	}
	
	void hamiltonian_refirement() {
		
	}
	
}
