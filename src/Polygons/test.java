package Polygons;

public class test {
	public static void main(String arg[]) {
		
		double x_1 = 3.0;
		double y_1 = 4.0;
		
		double x_2 = 5.0;
		double y_2 = 10.0;
		
		Vertex f = new Vertex(x_1,y_1);
		
		Vertex s = new Vertex(x_2,y_2);
		
		Half_edge e_1 = new Half_edge(f);
		
		// e_1.print_edge(true);
		
		// e_1.print_edge(true);
		
		double x_3 = -1.0;
		double y_3 = 3.0;
		
		Vertex t = new Vertex(x_3, y_3);
		Vertex fo = new Vertex(5.0, 0.0);
		
		e_1.set_next(s);
		e_1.get_next().set_next(fo);
		e_1.get_next().get_next().set_next(t);
		e_1.get_next().get_next().set_next(e_1);
		// e_1.print_edge(true);
		
		Polygon pol = new Polygon();
		Face i = new Face();
		i.set_inner_components(e_1);
		pol.set_face(i);
		
		//Polygon pol1 = new Polygon(pol);
		//pol1.get_outer_face().print_face();
		
		Dual_graph T = pol.triangulation();
		T.print_dual(false);
	}
}
