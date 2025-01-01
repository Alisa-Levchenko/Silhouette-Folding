package Polygons;

import java.util.List;

public class Dual_graph extends Graph<Polygon>{
	
	boolean _is_directed = false;
	
	Dual_graph(){ super(); }
	
	void print_dual(boolean full_pr) {
		for (Polygon it : _edges.keySet()) {
			if (full_pr) {
				it.print_polygon();
				
				System.out.println("Edges:");
				
				List<Polygon> _neigh= _edges.get(it);
				
				for (Polygon it_2 : _neigh) {
					it_2.print_polygon();
				}
				System.out.println("__________");
			}
			
			else {
				System.out.println(it.get_num());
				
				System.out.println("Edges:");
				
				List<Polygon> _neigh= _edges.get(it);
				
				for (Polygon it_2 : _neigh) {
					System.out.println(it_2.get_num());
				}
				System.out.println("__________");
			}
		}
	}
}
