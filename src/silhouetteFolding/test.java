package silhouetteFolding;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.io.File;

public class test {
	public static void main(String arg[]) {

		boolean test_general_classes_and_triangulation = false;
		if (test_general_classes_and_triangulation) {
			double x_1 = 3.0;
			double y_1 = 4.0;

			double x_2 = 5.0;
			double y_2 = 10.0;

			Vertex f = new Vertex(x_1, y_1);

			Vertex s = new Vertex(x_2, y_2);

			Half_edge e_1 = new Half_edge(f);

			// e_1.print_edge(true);

			// e_1.print_edge(true);

			double x_3 = -1.0;
			double y_3 = 3.0;

			Vertex t = new Vertex(x_3, y_3);
			Vertex fo = new Vertex(5.0, 0.0);

			// orientation is important!!!

			e_1.set_next(s);
			e_1.get_next().set_next(t);
			e_1.get_next().get_next().set_next(fo);
			e_1.get_next().get_next().get_next().set_next(e_1);
			// e_1.print_edge(true);

			Polygon pol = new Polygon();
			Face i = new Face();
			i.set_inner_components(e_1);
			pol.set_face(i);

			Dual_graph_general T = pol.triangulation_general();
			T.print_dual(true);

		}

		boolean test_of_triangulation = false;

		if (test_of_triangulation) {
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

		boolean test_is_in_interior_function = false;

		if (test_is_in_interior_function) {
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

		boolean test_dfs = false;

		if (test_dfs) {
			Graph<Integer> g = new Graph<Integer>();
			g._is_directed = false;
			g.add_vertex(1);
			g.add_vertex(2);
			g.add_vertex(3);
			g.add_vertex(4);
			g.add_vertex(5);

			g.add_edge(1, 2);
			g.add_edge(2, 3);
			g.add_edge(3, 4);
			g.add_edge(3, 5);
			g.add_edge(4, 1);

			Graph<Integer> t = g.depth_search(1);

			for (Integer it : t._edges.keySet()) {

				System.out.println("Vertex: " + it);

				List<Integer> _neigh = t._edges.get(it);

				for (Integer sec_it : _neigh) {
					System.out.println(it + " -> " + sec_it);
				}

				System.out.println("_____________");
			}
		}

		boolean test_sequence_of_triangles_func = false;

		if (test_sequence_of_triangles_func) {

			// Polygon eingabe

			Half_edge e1 = new Half_edge(5.0, 0.0);
			Half_edge e3 = new Half_edge(0.0, 3.0);
			Half_edge e5 = new Half_edge(-4.0, 0.0);
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

			Polygon p = new Polygon();
			p.set_face(e1.get_twin().get_incident_face());

			List<Help_structure> points = p.sequence_of_points();
			for (int i = 0; i < points.size(); i++) {
				points.get(i).print_str();
			}
		}

		boolean set_polygon = false;

		if (set_polygon) {
			Vertex first = new Vertex(0.0, 1.0);
			Vertex second = new Vertex(0.0, -1.0);
			Vertex third = new Vertex(1.0, 0.0);
			List<Vertex> l = new ArrayList<Vertex>();

			l.add(third);
			l.add(second);
			l.add(first);

			Polygon p = new Polygon();
			p.set_polygon(l);
		}

		boolean read_from_file = false;

		if (read_from_file) {
			File test = new File("test2.cp");
			Polygon p = new Polygon();
			p.create_polygon_from_file(test);
			p.triangulation().create_file();
		}

		boolean test_sequence_new = false;

		if (test_sequence_new) {
			Half_edge e1 = new Half_edge(0.0, 0.0);
			Half_edge e2 = new Half_edge(3.0, 0.0);
			Half_edge e3 = new Half_edge(4.5, 3.0);
			Half_edge e4 = new Half_edge(1.5, 3.0);
			e1.set_next(e2);
			e2.set_next(e3);
			e3.set_next(e4);
			e4.set_next(e1);
			Polygon p = new Polygon();
			p.set_face(e1.get_twin().get_incident_face());
			List<Help_structure> points = p.sequence_of_points();
			for (int i = 0; i < points.size(); i++) {
				points.get(i).print_str();
			}
		}
		boolean triangle_fill = false;
		if (triangle_fill) {
			Half_edge e1 = new Half_edge(0.0, 0.0);
			Half_edge e2 = new Half_edge(3.0, 0.0);
			Half_edge e3 = new Half_edge(3.0, 3.0);
			Half_edge e4 = new Half_edge(0.0, 3.0);
			e1.set_next(e2);
			e2.set_next(e3);
			e3.set_next(e4);
			e4.set_next(e1);
//		Polygon p = new Polygon();
//		p.set_face(e1.get_twin().get_incident_face());
//		int j;
//		List<Help_structure> points = p.sequence_of_points();
//			for (int i = 0; i < points.size(); i++) {
//				j=i+1;
//				points.get(i).print_str();
//				System.out.println("Winkel von start "+Calculator.calculation_of_angle(points.get(i).get_start(),
//						points.get(i).get_goal().get_p1(),
//						points.get(i).get_goal().get_p2()));
//				System.out.println("Winkel von start "+Calculator.calculation_of_angle(
//						points.get(i).get_goal().get_p2(),
//						points.get(i).get_start(),
//						points.get(i).get_goal().get_p1()));
//				if (points.size() != j)
//				points.get(j).get_start().print_coords();
//			}
			Coordinates p1 = new Coordinates(1.5, 0.5);
			Coordinates p2 = new Coordinates(3.5, 3.5);
			Coordinates p3 = new Coordinates(0.5, 3.5);

//			double hight = Calculator.calculateHeight(p2, p3, p1);
//			System.out.println("j= " + hight);
//			double hight2 = Calculator.calculateHeight(p3, p2, p1);
//			System.out.println("j= " + hight2);
//			double a = Calculator.calculation_of_angle(p2, p1, p3);
//			double b = Calculator.calculation_of_angle(p3, p2, p1);
//			double c = Calculator.calculation_of_angle(p2, p3, p1);
//
//			System.out.println("a= " + Math.toDegrees(Calculator.calculation_of_angle(p2, p1, p3)));
//			System.out.println("b= " + Math.toDegrees(Calculator.calculation_of_angle(p3, p2, p1)));
//			System.out.println("c= " + Math.toDegrees(Calculator.calculation_of_angle(p2, p3, p1)));
//			double zahl = (1.0 + (2.0 / 3.0)) / (Math.tan(b));
//			double newzahl = (1.0 + (4.0 / 3.0)) / (Math.tan(b)) + (1.0 + (6.0 / 3.0)) / (Math.tan(c));
//			System.out.println(newzahl + " zz " + Calculator.distance(p3, p2));

			List<Help_structure> triangles = new ArrayList<Help_structure>();
			Help_structure tri = new Help_structure();
			tri.set_goal(p2, p3);
			tri.set_start(p1);
			triangles.add(tri);


//			Coordinates p4 = new Coordinates(1.5, 0.5);
//			Coordinates p5 = new Coordinates(3.5, 3.5);
//			Coordinates p6 = new Coordinates(0.5, 3.5);
//
//			Help_structure tri2 = new Help_structure();
//			tri2.set_goal(p5, p4);
//			tri2.set_start(p6);
//			triangles.add(tri2);

			OutputHandler.createCreasepatern(triangles);

//			OutputHandler.add(1, 1, 1, 2, 1);
//			OutputHandler.add(1, 2, 2, 2, 1);
//			OutputHandler.add(1, 2, 2, 1, 2);
//			OutputHandler.add(1, 1, 1, 1, 2);

		}
		boolean drawTool = true;
		if (drawTool) {
			SwingUtilities.invokeLater(() -> {
				JFrame frame = new JFrame("Polygon Drawer");
				PolygonDrawer panel = new PolygonDrawer();

				frame.add(panel);
				frame.setSize(800, 600);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			});
		}
	}

}
