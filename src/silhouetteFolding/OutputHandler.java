package Polygons;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputHandler {
	private static final String FILE_NAME = "output.cp";
	private static boolean isFirstWrite = true;

	public static void appendToFile(String text) {
		try {
			if (isFirstWrite) {
				new File(FILE_NAME).delete(); // Delete file if it exists to start fresh
				isFirstWrite = false;
			}
			try (FileWriter writer = new FileWriter(FILE_NAME, true)) { // 'true' enables appending mode
				writer.write(text + System.lineSeparator()); // Adds the text and a newline
				System.out.println("Text appended to file successfully.");
			}
		} catch (IOException e) {
			System.err.println("An error occurred while writing to the file: " + e.getMessage());
		}
	}

	// a = B,M,V Rest sind Koordinaten.
	public static void add(int a, double x1, double y1, double x2, double y2) {
		appendToFile(a + " " + x1 + " " + y1 + " " + x2 + " " + y2);
	}

	// Methode zur Erstellung des finalen Outputs
	public static void createCreasepatern(List<Help_structure> a) {

		double actionPoint = 0; // das ist unsere "SweepLine"
		double old_actionPoint = actionPoint;
		double w = maxStreifenBreite(a); // Streifenbreite
		Coordinates p1; // Startvertex vom Dreieck
		Coordinates p2; // Teil d. Zielkante
		Coordinates p3; // Zielvertex vom Dreieck

		// berechne Streifenbreite
		for (int i = 0; i < a.size(); i++) {
			int j = i + 1;
//			a.get(i).print_str();
			p1 = a.get(i).get_start();
			p2 = a.get(i).get_goal().get_p1();
			p3 = a.get(i).get_goal().get_p2();

			if (j != a.size()) {// Damit habe ich sichergestellt, dass p3 immer Zielvertex ist.
				if (p3.get_x() != a.get(j).get_start().get_x() || p3.get_y() != a.get(j).get_start().get_y()) {
					p2 = p3; // tausche p2 mit p3
					p3 = a.get(j).get_start();
				}
				System.out.println("x= " + p3.get_x() + " y= " + p3.get_y());
			}

		}
		System.out.println("FINAL Min heigt " + w);

		// star Vertex p1, ZielKante p2,p3

		if (w == 2.4961508830135313) { // Calculator.distance(p2, p3)
			// no parity needed only hiding gadget.
//			actionPoint = old_actionPoint + Calculator.distance(p3, p2); // next point
			OutputHandler.add(1, 0, 0, 0, w); // add border left side start
			OutputHandler.add(1, actionPoint, 0, old_actionPoint, 0);
			OutputHandler.add(1, actionPoint, w, old_actionPoint, w);

			old_actionPoint = actionPoint;
		}
		OutputHandler.add(1, actionPoint, 0, actionPoint, w); // das letze Endstück hinzufügen

	}

	public static double maxStreifenBreite(List<Help_structure> a) {
		double w = Double.MAX_VALUE;
		// berechne die kleinste Höhe jeders Dreiecks
		for (int i = 0; i < a.size(); i++) {
			w = Math.min(w, Calculator.calculateMinimumHeight(a.get(i).get_start(), a.get(i).get_goal().get_p1(),
					a.get(i).get_goal().get_p2()));
//			System.out.println("THIOS " + Calculator.calculateMinimumHeight(a.get(i).get_start(),
//					a.get(i).get_goal().get_p1(), a.get(i).get_goal().get_p2()));
		}
		return w;
	}
}
