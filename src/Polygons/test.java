package Polygons;

public class test {
	public static void main(String arg[]) {
		
		boolean test1 = false;
		if (test1) {
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
		
		// orientation is important!!!
		
			e_1.set_next(fo);
			e_1.get_next().set_next(t);
			e_1.get_next().get_next().set_next(s);
			e_1.get_next().get_next().get_next().set_next(e_1);
		// e_1.print_edge(true);
		
			Polygon pol = new Polygon();
			Face i = new Face();
			i.set_inner_components(e_1);
			pol.set_face(i);
		
			Dual_graph T = pol.triangulation();
			T.print_dual(true);
		}
		
		boolean test2 = true;
		
		if (test2) {
			Half_edge e1 = new Half_edge(3.0, 0.0);
			Half_edge e3 = new Half_edge(0.0, 3.0);
			Half_edge e5 = new Half_edge(-3.0, 0.0);
			Half_edge e7 = new Half_edge(0.0, -3.0);
			Half_edge e2 = new Half_edge(1.0, 1.0);
			Half_edge e4 = new Half_edge(-1.0, 1.0);
			Half_edge e6 = new Half_edge(-1.0, -1.0);
			Half_edge e8 = new Half_edge(1.0, -1.0);
			e1.set_next(e2);
			e2.set_next(e3);
			e3.set_next(e4);
			e4.set_next(e5);
			e5.set_next(e6);
			e6.set_next(e7);
			e7.set_next(e8);
			e8.set_next(e1);
			Face f = new Face();
			f.set_inner_components(e8);
			Polygon p = new Polygon();
			p.set_face(f);
			Dual_graph t = p.triangulation();
			t.print_dual(true);
		}
		
		boolean test3 = false;
		
		if (test3) {
			Half_edge e3 = new Half_edge(0.0, 0.0);
			Half_edge e2 = new Half_edge(2.0, 0.0);
			Half_edge e1 = new Half_edge(1.0, 2.0);
			
			e1.set_next(e3);
			e3.set_next(e2);
			e2.set_next(e1);
			
			Face f = new Face();
			f.set_inner_components(e1);
			Polygon p = new Polygon();
			p.set_face(f);
			System.out.println(p.is_in_interior(new Vertex(1.0, 1.0), e1));
		}
	}
}
