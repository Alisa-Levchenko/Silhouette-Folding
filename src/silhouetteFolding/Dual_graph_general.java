package silhouetteFolding;

import java.util.List;

public class Dual_graph_general extends Graph<Polygon>{
	
	boolean _is_directed = false;
	
	Dual_graph_general(){ super(); }
	
	void print_dual(boolean full_pr) {
		if (full_pr) {
			for (Polygon it : _edges.keySet()) {
				System.out.println("This is polygon number: " + it.get_num());
				System.out.println();
				it.print_polygon();
			}
			
			System.out.println("Then edges: ");
			System.out.println();
			
			for (Polygon it : _edges.keySet()) {

				List<Polygon> _neigh= _edges.get(it);
				
				for (Polygon sec_it : _neigh) {
						System.out.println(it.get_num() + " -> " + sec_it.get_num());
				}
				
				System.out.println("_____________");
				System.out.println();
			}
		}
		else {
			for (Polygon it : _edges.keySet()) {
				
				System.out.println("Vertex: " + it.get_num());
				
				List<Polygon> _neigh= _edges.get(it);
				
				for (Polygon sec_it : _neigh) {
						System.out.println(it.get_num() + " -> " + sec_it.get_num());
				}
				
				System.out.println("_____________");
			}
		}
	}

}
