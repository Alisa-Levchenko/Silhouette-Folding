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
	Help_structure tri = new Help_structure();

	public PolygonDrawer() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (closed)
					return;

				if (!points.isEmpty() && isNearFirstPoint(e.getPoint())) {
					closed = true;
					edges.add(new Edge(points.get(points.size() - 1), points.get(0))); // Letzte Kante schließen
					tri.set_goal(new Coordinates(points.get(1).getX(), points.get(1).getY()),
							new Coordinates(points.get(2).getX(), points.get(2).getY()));
					tri.set_start(new Coordinates(points.get(0).getX(), points.get(0).getY()));
					triangles.add(tri);
					repaint();
					OutputHandler.createCreasepatern(triangles);

					printEdges();
				} else {
					if (!points.isEmpty()) {
						edges.add(new Edge(points.get(points.size() - 1), e.getPoint())); // Neue Kante hinzufügen
					}
					points.add(e.getPoint());
				}
				repaint();
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
