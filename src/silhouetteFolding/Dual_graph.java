package silhouetteFolding;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dual_graph extends Graph<Triangle>{
	
	boolean _is_directed = false;
	
	Dual_graph(){ super(); }
	
	void create_file() {
		try {
			FileWriter writer = new FileWriter("dual_graph.cp");
			List<List<Triangle>> to_print = new ArrayList<List<Triangle>>();
			
			for (Triangle it : this._vertices) {
				double x_f = it.get_p1().x_coordinate;
				double y_f = it.get_p1().y_coordinate;
				
				double x_s = it.get_p2().x_coordinate;
				double y_s = it.get_p2().y_coordinate;
				
				double x_th = it.get_p3().x_coordinate;
				double y_th = it.get_p3().y_coordinate;
				writer.write("2" + " " + x_f + " " + y_f + " " + x_s + " " + y_s + "\n");
				writer.write("2" + " " + x_f + " " + y_f + " " + x_th + " " + y_th + "\n");
				writer.write("2" + " " + x_th + " " + y_th + " " + x_s + " " + y_s + "\n");
			}
			
			writer.write("1 -200.0 -200.0 -200.0 200.0\n");
			writer.write("1 -200.0 -200.0 200.0 -200.0\n");
			writer.write("1 200.0 200.0 -200.0 200.0\n");
			writer.write("1 200.0 200.0 200.0 -200.0\n");
			
			writer.close();
			this._mask.clear();
		} catch (IOException e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
	}
	
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
