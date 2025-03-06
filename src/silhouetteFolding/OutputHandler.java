package silhouetteFolding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputHandler {
	private static final String FILE_NAME = "output.cp";
	private static boolean isFirstWrite = true;
//	System.out.println("x= " + p3.get_x() + " y= " + p3.get_y());
//	System.out.println("p2 " + p2Winkel + "p3 " + p3Winkel);	
//	System.out.println("FINAL Min heigt " + w);
	
	// Methode zur Erstellung des finalen Outputs
	public static void createCreasepatern(List<Help_structure> a) {

		double actionPoint = 0; // das ist unsere "SweepLine"
		double old_actionPoint = actionPoint;
//		double w = maxStreifenBreite(a); // Streifenbreite
		double w = 1;
		Coordinates p1; // Startvertex vom Dreieck
		Coordinates p2; // Teil d. Zielkante
		Coordinates p3; // Zielvertex vom Dreieck
		double segNr; // Anzahl Papierstreifensegmente pro Dreieck
		double h; // hoehe des jetzigen Dreiecks
		double m; // hoehe des Zusatzes pro Papierstreifensegment
		boolean obenUnten = false;
		Streifenvar var = null; // Rueckgabe der Fuellen Methode fuer Streifen konsistenz

		// für jedes Dreieck in der Hilfsstruktur
		for (int i = 0; i < a.size(); i++) {

			int j = i + 1;
			// fuelle Coordniaten mit Dreiecksdaten
			p1 = a.get(i).get_start();
			p2 = a.get(i).get_goal().get_p1();
			p3 = a.get(i).get_goal().get_p2();

			// Damit habe ich sichergestellt, dass p3 immer Zielvertex ist.
			if (j != a.size()) {
				if (p3.get_x() != a.get(j).get_start().get_x() || p3.get_y() != a.get(j).get_start().get_y()) {
					p2 = p3; // tausche p2 mit p3
					p3 = a.get(j).get_start();
				}
			}
			// berechne Segmentanzahl
			h = Calculator.calculateHeight(p3, p2, p1); // h von Zielkante aus
			segNr = Math.ceil(h / w);
			if (i == 0) // es gibt keine vorherige Zielkante, also nim p3,p1 als Startkante
				segNr = segNr + segNr % 2; // addiere Parritaet, sodass gerade
			else
				segNr = segNr + Calculator.getParitaet(p3, a.get((i - 1)).get_goal(), segNr); // addiere Parritaet

			m = (h - w) / (segNr - 1);
			System.out.println("m= " + m + " h= " + h + " w ist " + w + " mod ist: " + (h / segNr));
			// if h!=w dann normalfall
			// erstmal simpel: Winkelbefuellen:
			double p3Winkel = Calculator.calculation_of_angle(p2, p3, p1);
//			double p2Winkel = Calculator.calculation_of_angle(p3, p2, p1);

			if (i == 0) { // Streifeninitiirung und add Startsegmemt
				OutputHandler.add(1, 0, 0, 0, w); // add border left side start
				actionPoint = Calculator.seitenlaenge(w, m, p3Winkel, i);
				OutputHandler.add(1, actionPoint, 0, old_actionPoint, 0);
				OutputHandler.add(1, actionPoint, w, old_actionPoint, w);
				old_actionPoint = actionPoint;
				var = fillDreieck(p1, p2, p3, actionPoint, null, segNr, obenUnten, w, m);
			}
		}
		OutputHandler.add(1, var.actionPoint, 0, var.actionPoint, w); // das letze Endstück hinzufügen

	}

// TODO	// w Streifenbreite, m Papierstreifensegementzusatz
//	private static void turnarroundGadget(double w, double m, double actionPoint) {
//
//		if(w==m) // V-Fall
//			;
//		else { // X-Fall
//			;
//		}
//	}
	// if h==w dann "passen wir nur ein mal rein, muessen aber je nach Paritaet noch
	// mal durch.
	// TODO if (h == w) // Fall, dass wir schon im ersten Iterationsschritt fertig
	// sind. Fall a) mit paritaet b) OHNE
	// TODO else // Fall, müssen
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

	public static boolean addV(boolean obenUnten, double actionPoint, double w) {
		if (obenUnten) {
			OutputHandler.add(1, actionPoint, 0, (actionPoint + 2 * w), 0); // unterer Teil
			OutputHandler.add(1, actionPoint, w, (actionPoint + w), w);// oberer Teil1
			OutputHandler.add(1, (actionPoint + w), w, (actionPoint + 2 * w), w); // oberer Teil2
			OutputHandler.add(2, actionPoint, 0, (actionPoint + w), w); // Creases
			OutputHandler.add(2, (actionPoint + w), w, (actionPoint + 2 * w), 0);
		} else {
			OutputHandler.add(1, actionPoint, w, (actionPoint + 2 * w), w);
			OutputHandler.add(1, actionPoint, 0, (actionPoint + w), 0);// oberer Teil1
			OutputHandler.add(1, (actionPoint + w), 0, (actionPoint + 2 * w), 0); // oberer Teil2
			OutputHandler.add(2, actionPoint, w, (actionPoint + w), 0); // Creases
			OutputHandler.add(2, (actionPoint + w), 0, (actionPoint + 2 * w), w);
		}
		return !obenUnten;
	}

	// TODO IST NOCH QUARK
	public static boolean addX(boolean obenUnten, double actionPoint, double w, double m) {
		if (obenUnten) {
			double x = (w + m) / 2;
			double y = (w / 2) + (m / 2);
			OutputHandler.add(1, actionPoint, 0, (actionPoint + w + m), 0); // unterer Teil
			OutputHandler.add(1, actionPoint, w, (actionPoint + m), w);// oberer Teil1
			OutputHandler.add(1, (actionPoint + m), w, (actionPoint + w), w); // oberer Teil2
			OutputHandler.add(1, actionPoint + w, w, (actionPoint + w + m), w); // oberer Teil3
			OutputHandler.add(2, actionPoint, 0, (actionPoint + x), x);// Creases
			OutputHandler.add(2, (actionPoint + x), x, (actionPoint + w + m), 0); //
			OutputHandler.add(2, (actionPoint + m), w, (actionPoint + x), x);//
			OutputHandler.add(3, (actionPoint + x), x, (actionPoint + w), w); //
		} else {
			double x = (w + m) / 2;
			double y = x;
			x = w - x;
			OutputHandler.add(1, actionPoint, w, (actionPoint + w + m), w); // oberer Teil
			OutputHandler.add(1, actionPoint, 0, (actionPoint + m), 0);// unterer Teil1
			OutputHandler.add(1, (actionPoint + m), 0, (actionPoint + w), 0); // unterer Teil2
			OutputHandler.add(1, actionPoint + w, 0, (actionPoint + w + m), 0); // unterer Teil3
			OutputHandler.add(2, actionPoint, w, (actionPoint + y), x);// Creases
			OutputHandler.add(2, (actionPoint + y), x, (actionPoint + w + m), w); //
			OutputHandler.add(2, (actionPoint + m), 0, (actionPoint + y), x);//
			OutputHandler.add(3, (actionPoint + y), x, (actionPoint + w), 0); //
		}
		return !obenUnten;
	}

	// starte filling immer ab Hoehenlinie, sodass an AlternatingTurnGadget
	// angeschlossen
	public static Streifenvar fillDreieck(Coordinates p1, Coordinates p2, Coordinates p3, double actionPoint,
			Edge vorherigeZielkante, double segNr, boolean obenUnten, double w, double m) {
		double winkel1, winkel2;
		double old_actionPoint = actionPoint;
		double actionPoint1 = actionPoint;
		boolean obenUnten1 = obenUnten;

		// Fall Dreieckwinkel an Zielkante stumpf
		// TODO
		if (Math.toDegrees(Calculator.calculation_of_angle(p2, p3, p1)) > 90
				|| Math.toDegrees(Calculator.calculation_of_angle(p3, p2, p1)) > 90) {
			System.out.println("nich implementiert! ein Winkel am ziel groesser als 90Grad");
			return new Streifenvar(obenUnten1, actionPoint1);

		} else { // normales Dreieck
			// setze Winkel richtig, so dass das Startsegment als "bereits berechnet"
			// gesehen wird
			if (vorherigeZielkante == null || Calculator.istPunktAufKante(vorherigeZielkante, p2)) { // Error:
																										// Dreiecknr1?
				winkel1 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel2 = Calculator.calculation_of_angle(p3, p2, p1);
			} else {
				winkel2 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel1 = Calculator.calculation_of_angle(p3, p2, p1);
			}
			boolean LR = true;
			for (int k = 0; k < (segNr - 1); k++) {
				if (k == 0) { // erster schritt, da "bereits berechnet nur ein
					actionPoint1 = actionPoint1 + Calculator.seitenlaenge(w, m, winkel2, (k + 1));
					OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
					OutputHandler.add(1, actionPoint1, w, old_actionPoint, w);
					old_actionPoint = actionPoint1;
				}
				if (k > 0) {
					if (LR) {
						actionPoint1 = actionPoint1 + Calculator.seitenlaenge(w, m, winkel2, k)
								+ Calculator.seitenlaenge(w, m, winkel1, (k + 1));
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w);
						LR = false;
						old_actionPoint = actionPoint1;
					} else {
						actionPoint1 = actionPoint1 + Calculator.seitenlaenge(w, m, winkel1, k)
								+ Calculator.seitenlaenge(w, m, winkel2, (k + 1));
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w);
						old_actionPoint = actionPoint1;
						LR = true;
					}
				}

				// hier Add turnArround? JA! w==m volle Streifenbreite genutzt.
				if (w == m) {
					obenUnten1 = addV(obenUnten1, actionPoint1, w);
					actionPoint1 = actionPoint1 + w + m;
					old_actionPoint = actionPoint1;
				} else {
					obenUnten1 = addX(obenUnten1, actionPoint1, w, m);
					actionPoint1 = actionPoint1 + w + m;
					old_actionPoint = actionPoint1;
				}
			}
			// hier add final piece, mit dist.
			OutputHandler.add(1, old_actionPoint, w, (actionPoint1 + Calculator.distance(p2, p3)), w);
			OutputHandler.add(1, old_actionPoint, 0, (actionPoint1 + Calculator.distance(p2, p3)), 0);
			actionPoint1 = actionPoint1 + Calculator.distance(p3, p2); // ach ja, updaten!

			return new Streifenvar(obenUnten1, actionPoint1);

		}

//		switch(){
//		
//	}
	}

	// a = B,M,V Rest sind Koordinaten.
	public static void add(int a, double x1, double y1, double x2, double y2) {
		appendToFile(a + " " + x1 + " " + y1 + " " + x2 + " " + y2);
	}

	public static void appendToFile(String text) {
		try {
			if (isFirstWrite) {
				new File(FILE_NAME).delete(); // Delete file if it exists to start fresh
				isFirstWrite = false;
			}
			try (FileWriter writer = new FileWriter(FILE_NAME, true)) { // 'true' enables appending mode
				writer.write(text + System.lineSeparator()); // Adds the text and a newline
				System.out.println("Text appended to file successfully. " + text);
			}
		} catch (IOException e) {
			System.err.println("An error occurred while writing to the file: " + e.getMessage());
		}
	}
}
