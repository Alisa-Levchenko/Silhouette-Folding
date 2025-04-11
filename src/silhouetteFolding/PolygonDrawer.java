package silhouetteFolding;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PolygonDrawer extends JPanel {
	private final List<Point> points = new ArrayList<>();
	private final List<Edge> edges = new ArrayList<>();
	private boolean closed = false;
	private static final int CLOSE_DISTANCE = 10; // Abstand für das Schließen des Polygons
	List<Help_structure> triangles = new ArrayList<Help_structure>();
	List<Help_structure> updatedTriangles = new ArrayList<Help_structure>();

	public PolygonDrawer() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (closed)
					return;

				if (!points.isEmpty() && isNearFirstPoint(e.getPoint())) {
					closed = true;
					edges.add(new Edge(points.get(points.size() - 1), points.get(0))); // Letzte Kante schließen, fuer
																						// Zeichnung
					Polygon p = pointsToPolygon(points); // verbinde Halfedges zu Polygon
					try {
						triangles = p.sequence_of_points();
						// add triangeles for Each double Tri
						for (int i = 0; i < triangles.size() - 1; i++) {

							Help_structure t1 = triangles.get(i);
							Help_structure t2 = triangles.get(i + 1);

							if (Help_structure.areTrianglesIdentical(t1, t2)) {
								System.out.println(i + " das ist es!");
								// In kleinere Dreiecke zerlege ASSUMING LAST TRIANGLE IS NOT DUPLICATE
								List<Help_structure> subTriangles = t1.splitAtCentroid(t2,triangles.get(i+2).get_start()); // Verfeinerung der Dreiecke

								for (Help_structure sub : subTriangles) {
									updatedTriangles.add(sub);
								}
								i = i + 1; // ueberspringe das naechste Dreieck, da es ersetzt wurde.
							} else {
								updatedTriangles.add(t1); // Original behalten
							}
						}
						updatedTriangles.add(triangles.get(triangles.size() - 1)); // letztes Dreieck manuell
																					// hinzufuegen wegen schleife bis
																					// size-1

						for (int i = 0; i < triangles.size(); i++) {
							System.out.println("Dreieck# "+i +"\t"+triangles.get(i).toString());
						}
						System.out.println("\n\n");
						for (int i = 0; i < updatedTriangles.size(); i++) {
							System.out.println("upDreieck# "+i +"\t"+updatedTriangles.get(i).toString());
						}
						OutputHandler.createCreasepatern(updatedTriangles); // !!! hier wird der Papierstreifen erstellt!!!
					} catch (NullPointerException ex) {
						System.out.print("oh, leider zu wenige Punkte! Breche hier ab!");
					}
					repaint();
					printEdges();
				} else {
					if (!points.isEmpty()) {
						edges.add(new Edge(points.get(points.size() - 1), e.getPoint())); // Neue Kante hinzufügen
					}
					points.add(e.getPoint());
				}
				repaint();
			}

			// null wenn wir zu weniger als 3 Punkte haben
			private Polygon pointsToPolygon(List<Point> points) {
				Polygon p = new Polygon();
				List<Half_edge> edges = new ArrayList<Half_edge>();
				// points to half edge
				if (points.size() > 2) {
					for (int i = 0; (i < points.size()); i++) {
						Half_edge edge = new Half_edge(points.get(i).getX(), points.get(i).getY());
						edges.add(edge);
					}
					for (int i = 0; (i < points.size() - 1); i++) {
						edges.get(i).set_next(edges.get(i + 1));
					}
					edges.get(points.size() - 1).set_next(edges.get(0));
					p.set_face(edges.get(0).get_twin().get_incident_face());
					return p;
				}
				return null;
			}

		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);

		// Punkte zeichnen
		for (Point p : points) {
			g.fillOval(p.x - 3, p.y - 3, 6, 6);
		}

		// Kanten zeichnen
		g.setColor(Color.BLUE);
		for (Edge edge : edges) {
			g.drawLine(edge.start.x, edge.start.y, edge.end.x, edge.end.y);
		}

		// Falls die Maus nahe am ersten Punkt ist, diesen hervorheben
		if (!points.isEmpty()) {
			Point first = points.get(0);
			g.setColor(Color.RED);
			g.drawOval(first.x - CLOSE_DISTANCE, first.y - CLOSE_DISTANCE, CLOSE_DISTANCE * 2, CLOSE_DISTANCE * 2);
		}
	}

	private boolean isNearFirstPoint(Point p) {
		if (points.isEmpty())
			return false;
		return p.distance(points.get(0)) < CLOSE_DISTANCE;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	private void printEdges() {
		System.out.println("Polygon Edges:");
		for (Edge edge : edges) {
			System.out.println(edge);
		}
	}
}
