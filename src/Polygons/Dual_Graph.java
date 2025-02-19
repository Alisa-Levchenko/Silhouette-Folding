package Polygons;

import java.util.ArrayList;
import java.util.List;

public class Dual_graph extends Graph<Triangle>{
	
	boolean _is_directed = false;
	
	Dual_graph(){ super(); }
	
	void print_dual(boolean full_pr) {
		if (full_pr) {
			for (Triangle it : _vertices) {
				System.out.println("This is polygon number: " + it.get_num());
				System.out.println();
				it.print_triangle();
			}
			
			System.out.println("Then edges: ");
			System.out.println();
			
			for (Triangle it : _vertices) {

				List<Triangle> _neigh= _edges.get(it);
				
				for (Triangle sec_it : _neigh) {
						System.out.println(it.get_num() + " -> " + sec_it.get_num());
				}
				
				System.out.println("_____________");
				System.out.println();
			}
		}
		else {
			for (Triangle it : _edges.keySet()) {
				
				System.out.println("Vertex: " + it.get_num());
				
				List<Triangle> _neigh= _edges.get(it);
				
				for (Triangle sec_it : _neigh) {
						System.out.println(it.get_num() + " -> " + sec_it.get_num());
				}
				
				System.out.println("_____________");
			}
		}
	}
	
}
