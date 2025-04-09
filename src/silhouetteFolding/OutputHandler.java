package silhouetteFolding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputHandler {
	private static final String FILE_NAME = "faltmusterDreieck.cp";
	private static boolean isFirstWrite = true;
//	System.out.println("x= " + p3.get_x() + " y= " + p3.get_y());
//	System.out.println("p2 " + p2Winkel + "p3 " + p3Winkel);	
//	System.out.println("FINAL Min heigt " + w);

	// Methode zur Erstellung des finalen Outputs
	public static void createCreasepatern(List<Help_structure> a) {

		double actionPoint = 0; // das ist unsere "SweepLine"
		double old_actionPoint = actionPoint;
		double feinheit = 1;
		double w = maxStreifenBreite(a) / (2.0 * feinheit); // Streifenbreite
		double u = 0;
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

			m = ((h - w) / (segNr - 1.0));

			// if h!=w dann normalfall
			// erstmal simpel: Winkelbefuellen:
			double p3Winkel = Calculator.calculation_of_angle(p2, p3, p1);

			if (i == 0) { // Streifeninitiirung und add Startsegmemt
				OutputHandler.add(1, 0, 0, 0, w); // add border left side start
				if (Math.toDegrees(Calculator.calculation_of_angle(p2, p3, p1)) > 90
						|| Math.toDegrees(Calculator.calculation_of_angle(p3, p2, p1)) > 90) {
					// stumpfes Dreieck
					var = fillDreieck2(p1, p2, p3, actionPoint, null, segNr, obenUnten, w, m);
				} else { // spitzes Dreieck
					actionPoint = Calculator.seitenlaenge(w, m, p3Winkel, i);
					OutputHandler.add(1, actionPoint, 0, old_actionPoint, 0);
					OutputHandler.add(1, actionPoint, w, old_actionPoint, w);
					OutputHandler.add(2, old_actionPoint, 0, actionPoint, w);
					old_actionPoint = actionPoint;
					var = fillDreieck(p1, p2, p3, actionPoint, null, segNr, obenUnten, w, m);

				}

			} else {
				var = fillDreieck2(p1, p2, p3, actionPoint, a.get(i - 1).get_goal(), segNr, var.obenUnten, w, m);
			}
//			System.out.println("m= " + m + " h= " + h + " w ist " + w + " mod ist: " + (h / segNr));

			// hier Uerbegang
			if (i < (a.size() - 1)) {
				// am Reflexwinkel zu enden bedeutet wir muessen noch ein Stueck einfuegen um
				// zum Punkt P3 zu kommen... um genau zu sein abziehen, ich fuege es aber nach
				// einer Wendung hinzu.
				if (Math.toDegrees(Calculator.calculation_of_angle(p1, p3, p2)) > 90) {
					// rechne etwas aus, was nach dem 180Grad Fold noch dazu kommt
					u = m / Math.abs(Calculator.myTan(Calculator.calculation_of_angle(p1, p3, p2))); // u ist ein add
																										// auf der
																										// Rüchseite.
				}
				actionPoint = uebergang(var.obenUnten, var.actionPoint, a.get(i), a.get(i + 1), u, w);
				u = 0; // reset u, fuer naechsten Durchlauf
			}
		}

		OutputHandler.add(1, var.actionPoint, 0, var.actionPoint, w); // das letze Endstück hinzufügen

	}

	// berechne Uebergang mit: startpunkt und Zielkanten. und fuege die Faltung
	// richtig orientiert ein.
	// u = Korrektur bei wenn P3 ein Reflexwinkel ist.
	private static double uebergang(boolean obenUnten, double actionPoint, Help_structure a1, Help_structure a2,
			double u, double w) {
		// berechne Winkel von Turnaround
		double in;
		double actionPoint1 = actionPoint;
		// suche Punkt der auf beiden Zielkanten ist. und bestimme seinen Innenwinkel
		if (!a1.get_goal().get_p1().equals(a2.get_start())) { // vergleiche a2.P1 mit a1.zielKante zK
			// wenn keine Gleichheit -> p1 von a1.zK ist auf beiden a1.zK und a2.zK
			// teste diesen Punkt mit a2.zK um p3 zu bestimmen
			if (a1.get_goal().get_p1().equals(a2.get_goal().get_p1()))
				in = Calculator.calculation_of_angle(a2.get_start(), a1.get_goal().get_p1(), a2.get_goal().get_p2());
			else
				in = Calculator.calculation_of_angle(a2.get_start(), a1.get_goal().get_p1(), a2.get_goal().get_p1());
		} else {
			if (a1.get_goal().get_p2().equals(a2.get_goal().get_p1()))
				in = Calculator.calculation_of_angle(a2.get_start(), a1.get_goal().get_p2(), a2.get_goal().get_p2());
			else
				in = Calculator.calculation_of_angle(a2.get_start(), a1.get_goal().get_p2(), a2.get_goal().get_p1());
		}
		if (Math.toDegrees(in) <= 90) {
			// crease turn around
			OutputHandler.add(2, actionPoint1, 0, actionPoint1, w);
			// fuege den Zusatz ein, wenn vorheriges Dreieck im Reflexwinkel endet
			OutputHandler.add(1, actionPoint1, w, actionPoint1 + u, w);
			OutputHandler.add(1, actionPoint1, 0, actionPoint1 + u, 0);
			actionPoint1 = actionPoint1 + u;
			double a = w / Calculator.myTan(in);
			double b = Math.sqrt(w * w + a * a);
			if (obenUnten) {
				// borders
				OutputHandler.add(1, actionPoint1, 0, actionPoint1 + b, 0); // oben
				OutputHandler.add(1, actionPoint1 + b, 0, actionPoint1 + b + a, 0);
				OutputHandler.add(1, actionPoint1, w, actionPoint1 + a, w); // unten
				OutputHandler.add(1, actionPoint1 + a, w, actionPoint1 + a + b, w);
				// crease
				OutputHandler.add(3, actionPoint1 + b, 0, actionPoint1 + a, w);
				actionPoint1 = actionPoint1 + a + b;
			} else { // das selbe gespiegelt
						// borders
				OutputHandler.add(1, actionPoint1, w, actionPoint1 + b, w); // unten
				OutputHandler.add(1, actionPoint1 + b, w, actionPoint1 + b + a, w);
				OutputHandler.add(1, actionPoint1, 0, actionPoint1 + a, 0); // oben
				OutputHandler.add(1, actionPoint1 + a, 0, actionPoint1 + a + b, 0);
				// crease
				OutputHandler.add(3, actionPoint1 + b, w, actionPoint1 + a, 0);
				actionPoint1 = actionPoint1 + a + b;
			}
		} else { // Wenn der Winkel vom Turnaround groeser als 90* ist
					// berechne Zusatz der vom Winkel abhaengt
			double zusatz = w / Calculator.myTan(Math.PI - in);
			OutputHandler.add(1, actionPoint1, w, actionPoint1 + zusatz, w);
			OutputHandler.add(1, actionPoint1, 0, actionPoint1 + zusatz, 0);
			actionPoint1 = actionPoint1 + zusatz;
			// setze Faltung fuer Streifenausrichtung
			OutputHandler.add(2, actionPoint1, 0, actionPoint1, w);
			// addiere den Zusatz durch Reflexwinkel
			OutputHandler.add(1, actionPoint1, w, actionPoint1 + u, w);
			OutputHandler.add(1, actionPoint1, 0, actionPoint1 + u, 0);
			actionPoint1 = actionPoint1 + u;

			double a = w / Calculator.myTan((Math.PI - in) / 2);
			if (obenUnten) {
				OutputHandler.add(1, actionPoint1, 0, actionPoint1 + a, 0); // oben
				OutputHandler.add(1, actionPoint1, w, actionPoint1 + a, w); // unten
				// crease
				OutputHandler.add(3, actionPoint1, w, actionPoint1 + a, 0);
				actionPoint1 = actionPoint1 + a;
			} else {
				OutputHandler.add(1, actionPoint1, 0, actionPoint1 + a, 0); // oben
				OutputHandler.add(1, actionPoint1, w, actionPoint1 + a, w); // unten
				// crease
				OutputHandler.add(3, actionPoint1, 0, actionPoint1 + a, w);
				actionPoint1 = actionPoint1 + a;
			}
		}
		// berechne laenge von Overhangzusatz, der sich aus Winkel <90* ergibt
		// berechne laenge bis naechstem Startpunkt
		// fuege Faltmuster hinzu
		// altenate turn
		return actionPoint1;

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

	public static boolean addX(boolean obenUnten, double actionPoint, double w, double m) {
		if (obenUnten) {
			double x = (w + m) / 2;

			OutputHandler.add(1, actionPoint, 0, (actionPoint + w + m), 0); // unterer Teil
			OutputHandler.add(1, actionPoint, w, (actionPoint + m), w);// oberer Teil1
			OutputHandler.add(1, (actionPoint + m), w, (actionPoint + w), w); // oberer Teil2
			OutputHandler.add(1, actionPoint + w, w, (actionPoint + w + m), w); // oberer Teil3
			OutputHandler.add(3, actionPoint, 0, (actionPoint + x), x);// Creases
			OutputHandler.add(2, (actionPoint + x), x, (actionPoint + w + m), 0); //
			OutputHandler.add(3, (actionPoint + m), w, (actionPoint + x), x);//
			OutputHandler.add(3, (actionPoint + x), x, (actionPoint + w), w); //
		} else {
			double x = (w + m) / 2;
			double y = x;
			x = w - x;
			OutputHandler.add(1, actionPoint, w, (actionPoint + w + m), w); // oberer Teil
			OutputHandler.add(1, actionPoint, 0, (actionPoint + m), 0);// unterer Teil1
			OutputHandler.add(1, (actionPoint + m), 0, (actionPoint + w), 0); // unterer Teil2
			OutputHandler.add(1, actionPoint + w, 0, (actionPoint + w + m), 0); // unterer Teil3
			OutputHandler.add(3, actionPoint, w, (actionPoint + y), x);// Creases
			OutputHandler.add(2, (actionPoint + y), x, (actionPoint + w + m), w); //
			OutputHandler.add(3, (actionPoint + m), 0, (actionPoint + y), x);//
			OutputHandler.add(3, (actionPoint + y), x, (actionPoint + w), 0); //
		}
		return !obenUnten;
	}

	// starte filling immer ab Hoehenlinie, sodass an AlternatingTurnGadget
	// angeschlossen
	public static Streifenvar fillDreieck(Coordinates p1, Coordinates p2, Coordinates p3, double actionPoint,
			GeometricEdge vorherigeZielkante, double segNr, boolean obenUnten, double w, double m) {
		double winkel1, winkel2;
		double old_actionPoint = actionPoint;
		double actionPoint1 = actionPoint;
		boolean obenUnten1 = obenUnten;
		double vorherigeLaenge = 0;

		// Fall Dreieckwinkel an Zielkante stumpf
		if (Math.toDegrees(Calculator.calculation_of_angle(p2, p3, p1)) > 90
				|| Math.toDegrees(Calculator.calculation_of_angle(p3, p2, p1)) > 90) {
			System.out.println("nich implementiert! ein Winkel am ziel groesser als 90Grad");
			boolean fall1 = false; // haben wir das unterste Stuek vom Alternating Turnaround bereits?
			// bestimme Winkel von dem wir in das Dreieck hineingehen um zu schauen ob wir
			// den ersten Teil bereits berechnet haben.
			// Winkel1 ist immer der winkel > 90 Grad
			if (vorherigeZielkante == null) {
				if (Math.toDegrees(Calculator.calculation_of_angle(p3, p2, p1)) > 90) {
					winkel1 = Calculator.calculation_of_angle(p3, p2, p1);
					winkel2 = Calculator.calculation_of_angle(p2, p3, p1);
				} else {
					winkel1 = Calculator.calculation_of_angle(p2, p3, p1);
					winkel2 = Calculator.calculation_of_angle(p3, p2, p1);
				}
			} else if (Calculator.istPunktAufKante(vorherigeZielkante, p2)) { // bestimme Winkel der Eintrittskante
				winkel1 = Calculator.calculation_of_angle(p3, p2, p1);
				winkel2 = Calculator.calculation_of_angle(p2, p3, p1);
			} else {
				winkel1 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel2 = Calculator.calculation_of_angle(p3, p2, p1);
			}

			if (Math.toDegrees(winkel1) > 90) // teste ob der Winkel, der Eintrittskante Stumpf ist
				fall1 = true; // wenn er es ist, so ist das gesamte Dreieck noch zu fuellen.

			// wir starten auserhalb oder innerhalb des Dreiecks.
			// fall1=winkel Stumpf -> berechne fuellung, ab p1, in Dreieck hinein. Fall2
			// heist wir ueberspringen das einfach, da wir es im Alternating Turnaround
			// berechnet haben.
			boolean alternating = true;
			if (fall1)
				for (int k = 0; k < (segNr); k++) {
					if (k == 0 && fall1) {
						vorherigeLaenge = (w + m) / Calculator.myTan(winkel2); // berechne Laenge
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						// w kante bleibt
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w);
						// crease
						OutputHandler.add(2, old_actionPoint, w,
								old_actionPoint + Math.abs(w / Calculator.myTan(winkel1)), 0);
						OutputHandler.add(3, old_actionPoint, w,
								old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0);
						// 0 kante
						OutputHandler.add(1, old_actionPoint, 0,
								old_actionPoint + Math.abs(w / Calculator.myTan(winkel1)), 0);
						OutputHandler.add(1, old_actionPoint + Math.abs(w / Calculator.myTan(winkel1)), 0,
								old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0);
						OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0,
								actionPoint1, 0);// fuege in output hinzu
						old_actionPoint = actionPoint1; // update old_actionPoint
					}

					else if (k == 1) { // NOTE: Tan(winkel1) => negativ
						vorherigeLaenge = (w + m) / Calculator.myTan(winkel2) + (w) / Calculator.myTan(winkel1);
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						// w kante
						OutputHandler.add(1, old_actionPoint, w, actionPoint1 + w / Calculator.myTan(winkel1), w);
						OutputHandler.add(1, actionPoint1 + w / Calculator.myTan(winkel1), w, actionPoint1, w);
						// crease
						OutputHandler.add(3, old_actionPoint, w,
								old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0);
						OutputHandler.add(2, actionPoint1 + w / Calculator.myTan(winkel1), w, actionPoint1, 0);
						// 0 kante
						OutputHandler.add(1, old_actionPoint, 0,
								old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0);
						OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0,
								actionPoint1, 0);// fuege in output hinzu
						old_actionPoint = actionPoint1;
					} else {
						if (alternating) {
							vorherigeLaenge = vorherigeLaenge + (2.0 * m) / Calculator.myTan(winkel2); // berechne
																										// Laenge
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							// w kante
							OutputHandler.add(1, old_actionPoint, w,
									old_actionPoint - Calculator.seitenlaenge((m), 0, winkel1, 0), w);
							OutputHandler.add(1, old_actionPoint - Calculator.seitenlaenge((m), 0, winkel1, 0), w,
									actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel2, 0), w);
							OutputHandler.add(1, actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel2, 0), w,
									actionPoint1, w);
							// crease
							OutputHandler.add(3, old_actionPoint - Calculator.seitenlaenge((m), 0, winkel1, 0), w,
									old_actionPoint - Calculator.seitenlaenge((w + m), 0, winkel1, 0), 0);
							OutputHandler.add(3, actionPoint1 - Calculator.seitenlaenge((m), 0, winkel2, 0), 0,
									actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel2, 0), w);
							// 0 kante
							OutputHandler.add(1, old_actionPoint, 0,
									old_actionPoint - Calculator.seitenlaenge((w + m), 0, winkel1, 0), 0);
							OutputHandler.add(1, old_actionPoint - Calculator.seitenlaenge((w + m), 0, winkel1, 0), 0,
									actionPoint1 - Calculator.seitenlaenge((m), 0, winkel2, 0), 0);
							OutputHandler.add(1, old_actionPoint - Calculator.seitenlaenge((w + m), 0, winkel1, 0), 0,
									actionPoint1, 0); // fuege in output hinzu
							old_actionPoint = actionPoint1;
							alternating = false;
						} else {
							vorherigeLaenge = vorherigeLaenge + (2.0 * m) / Calculator.myTan(winkel1); // berechne
																										// Laenge
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							// w kante
							OutputHandler.add(1, old_actionPoint, w,
									actionPoint1 + Calculator.seitenlaenge((w), 0, winkel1, 0), w);
							OutputHandler.add(1, actionPoint1 + Calculator.seitenlaenge((w), 0, winkel1, 0), w,
									actionPoint1, w);
							// crease
							OutputHandler.add(3, old_actionPoint, w,
									old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0);
							OutputHandler.add(2, actionPoint1 + Calculator.seitenlaenge((w), 0, winkel1, 0), w,
									actionPoint1, 0);
							// 0 kante
							OutputHandler.add(1, old_actionPoint, 0,
									old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0);
							OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0,
									actionPoint1, 0);
							old_actionPoint = actionPoint1;
							alternating = true;
						}
					}
					// hier Add turnArround? JA! w==m volle Streifenbreite genutzt.
					if (k < (segNr - 1)) {
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
				}
			else // fall2: start bei 1, da das erste Segment vom Turnaround berechnet ist.
				for (int k = 1; k < (segNr); k++) {
					if (k == 1) {
						vorherigeLaenge = (w + 2.0 * m) / Calculator.myTan(winkel2);
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
						old_actionPoint = actionPoint1;
					}
					if (k == 2) {
						vorherigeLaenge = vorherigeLaenge + (w + m) / Calculator.myTan(winkel1);
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
						old_actionPoint = actionPoint1;
					} else {
						if (alternating) {
							vorherigeLaenge = (2.0 * m) / Calculator.myTan(winkel2);
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
							OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
							old_actionPoint = actionPoint1;
							alternating = false;
						} else {
							vorherigeLaenge = (2.0 * m) / Calculator.myTan(winkel1);
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
							OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
							old_actionPoint = actionPoint1;
							alternating = true;
						}
					}
					if (k < (segNr - 1)) {
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
				}

			return new Streifenvar(obenUnten1, actionPoint1);

		} else

		{ // normales Dreieck
			// setze Winkel richtig, so dass das Startsegment als "bereits berechnet"
			// Benennung; winkel1 ist wo der Papierstreifen das Dreiek betritt, von
			// Zielkante aus
			// und winkel2 ist der Winkel wo wir ggf. unseren "Turn around" haben(von
			// Zielkante aus).
			if (vorherigeZielkante == null || Calculator.istPunktAufKante(vorherigeZielkante, p3)) { // Error:
																										// Dreiecknr1?
				winkel1 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel2 = Calculator.calculation_of_angle(p3, p2, p1);
			} else {
				winkel2 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel1 = Calculator.calculation_of_angle(p3, p2, p1);
			}
			boolean LR = true;
			for (int k = 0; k < (segNr - 1); k++) {
				if (k == 0) { // erster schritt bereits berechnet nur ein Teil das fehlt
					actionPoint1 = actionPoint1 + Calculator.seitenlaenge(w, m, winkel2, (k + 1));
					// add hiding Gadget Mountain
					OutputHandler.add(3, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0,
							old_actionPoint, w);
					// geteilt
					OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0,
							old_actionPoint, 0);
					OutputHandler.add(1, actionPoint1, 0, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0),
							0);
					// bleibt
					OutputHandler.add(1, actionPoint1, w, old_actionPoint, w);
					old_actionPoint = actionPoint1;
				}
				if (k > 0) {
					if (LR) {
						actionPoint1 = actionPoint1 + Calculator.seitenlaenge(w, m, winkel2, k)
								+ Calculator.seitenlaenge(w, m, winkel1, (k + 1));
						// Creases
						OutputHandler.add(3, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0,
								old_actionPoint, w);
						OutputHandler.add(3, actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel1, 0), 0,
								actionPoint1 - Calculator.seitenlaenge((m), 0, winkel1, 0), w);
						// 0 Seite
						OutputHandler.add(1, old_actionPoint, 0,
								old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0);
						OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0,
								actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel1, 0), 0);
						OutputHandler.add(1, actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel1, 0), 0,
								actionPoint1, 0);
						// w Seite
						OutputHandler.add(1, old_actionPoint, w,
								actionPoint1 - Calculator.seitenlaenge((m), 0, winkel1, 0), w);
						OutputHandler.add(1, actionPoint1 - Calculator.seitenlaenge((m), 0, winkel1, 0), w,
								actionPoint1, w);
						LR = false;
						old_actionPoint = actionPoint1;
					} else {
						actionPoint1 = actionPoint1 + Calculator.seitenlaenge(w, m, winkel1, k)
								+ Calculator.seitenlaenge(w, m, winkel2, (k + 1));
						// Creases
						OutputHandler.add(3, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel1, 0), w,
								old_actionPoint, 0);
						OutputHandler.add(3, actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel2, 0), w,
								actionPoint1 - Calculator.seitenlaenge((m), 0, winkel2, 0), 0);
						// w Seite
						OutputHandler.add(1, old_actionPoint, w,
								old_actionPoint + Calculator.seitenlaenge((w), 0, winkel1, 0), w);
						OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel1, 0), w,
								actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel2, 0), w);
						OutputHandler.add(1, actionPoint1 - Calculator.seitenlaenge((w + m), 0, winkel2, 0), w,
								actionPoint1, w);
						// 0 Seite
						OutputHandler.add(1, old_actionPoint, 0,
								actionPoint1 - Calculator.seitenlaenge((m), 0, winkel2, 0), 0);
						OutputHandler.add(1, actionPoint1 - Calculator.seitenlaenge((m), 0, winkel2, 0), 0,
								actionPoint1, 0);
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
			actionPoint1 = actionPoint1 + Calculator.distance(p3, p2);
			if (LR) {
				// Creases
				OutputHandler.add(3, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0, old_actionPoint,
						w);
//				OutputHandler.add(3, actionPoint1-Calculator.seitenlaenge((w), 0, winkel1,0), 0, actionPoint1, w);
				// 0 Seite
				OutputHandler.add(1, old_actionPoint, 0, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0),
						0);
				OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel2, 0), 0, actionPoint1, 0);
//				OutputHandler.add(1,actionPoint1-Calculator.seitenlaenge((w), 0, winkel1,0), 0, actionPoint1, 0);
				// w Seite
				OutputHandler.add(1, old_actionPoint, w, actionPoint1, w);

				old_actionPoint = actionPoint1;
			} else {
				// Creases
				OutputHandler.add(3, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel1, 0), w, old_actionPoint,
						0);
//				OutputHandler.add(3, actionPoint1-Calculator.seitenlaenge((w), 0, winkel2,0), w, actionPoint1, 0);
				// 0 Seite
				OutputHandler.add(1, old_actionPoint, w, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel1, 0),
						w);
				OutputHandler.add(1, old_actionPoint + Calculator.seitenlaenge((w), 0, winkel1, 0), w, actionPoint1, w);
//				OutputHandler.add(1,actionPoint1-Calculator.seitenlaenge((w), 0, winkel2,0), w, actionPoint1, w);
				// w Seite
				OutputHandler.add(1, old_actionPoint, 0, actionPoint1, 0);
				old_actionPoint = actionPoint1;
			}

			return new Streifenvar(obenUnten1, actionPoint1);

		}

	}

	// a = B,M,V Rest sind Koordinaten.
	public static void add(int a, double x1, double y1, double x2, double y2) {
//		if (Math.abs(y1) < 1E-10) { // Treat as zero if small enough
//		    y1 = 0.0;
//		}
		if (x1 != x2 || y1 != y2)
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

	public static Streifenvar fillDreieck2(Coordinates p1, Coordinates p2, Coordinates p3, double actionPoint,
			GeometricEdge vorherigeZielkante, double segNr, boolean obenUnten, double w, double m) {
		double winkel1, winkel2;
		double old_actionPoint = actionPoint;
		double actionPoint1 = actionPoint;
		boolean obenUnten1 = obenUnten;
		double vorherigeLaenge = 0;

		// Fall Dreieckwinkel an Zielkante stumpf
		if (Math.toDegrees(Calculator.calculation_of_angle(p2, p3, p1)) > 90
				|| Math.toDegrees(Calculator.calculation_of_angle(p3, p2, p1)) > 90) {
			System.out.println("nich implementiert! ein Winkel am ziel groesser als 90Grad");
			boolean fall1 = false; // haben wir das unterste Stuek vom Alternating Turnaround bereits?
			// bestimme Winkel von dem wir in das Dreieck hineingehen um zu schauen ob wir
			// den ersten Teil bereits berechnet haben.
			// Winkel1 ist immer der winkel > 90 Grad
			if (vorherigeZielkante == null) {
				if (Math.toDegrees(Calculator.calculation_of_angle(p3, p2, p1)) > 90) {
					winkel1 = Calculator.calculation_of_angle(p3, p2, p1);
					winkel2 = Calculator.calculation_of_angle(p2, p3, p1);
				} else {
					winkel1 = Calculator.calculation_of_angle(p2, p3, p1);
					winkel2 = Calculator.calculation_of_angle(p3, p2, p1);
				}
			} else if (Calculator.istPunktAufKante(vorherigeZielkante, p2)) { // bestimme Winkel der Eintrittskante
				winkel1 = Calculator.calculation_of_angle(p3, p2, p1);
				winkel2 = Calculator.calculation_of_angle(p2, p3, p1);
			} else {
				winkel1 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel2 = Calculator.calculation_of_angle(p3, p2, p1);
			}

			if (Math.toDegrees(winkel1) > 90) // teste ob der Winkel, der Eintrittskante Stumpf ist
				fall1 = true; // wenn er es ist, so ist das gesamte Dreieck noch zu fuellen.

			// wir starten auserhalb oder innerhalb des Dreiecks.
			// fall1=winkel Stumpf -> berechne fuellung, ab p1, in Dreieck hinein. Fall2
			// heist wir ueberspringen das einfach, da wir es im Alternating Turnaround
			// berechnet haben.
			boolean alternating = false;
			if (fall1)
				for (int k = 0; k < (segNr); k++) {
					if (k == 0 && fall1) {
						vorherigeLaenge = (w + m) / Calculator.myTan(winkel2); // berechne Laenge
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
						old_actionPoint = actionPoint1; // update old_actionPoint
					}

					else if (k == 1) {
						vorherigeLaenge = (w + m) / Calculator.myTan(winkel2) + (w) / Calculator.myTan(winkel1);
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
						old_actionPoint = actionPoint1;
					} else {
						if (alternating) {
							vorherigeLaenge = vorherigeLaenge + (2.0 * m) / Calculator.myTan(winkel1); // berechne
																										// Laenge
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
							OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
							old_actionPoint = actionPoint1;
							alternating = false;
						} else {
							vorherigeLaenge = vorherigeLaenge + (2.0 * m) / Calculator.myTan(winkel2); // berechne
																										// Laenge
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
							OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
							old_actionPoint = actionPoint1;
							alternating = true;
						}
					}
					// hier Add turnArround? JA! w==m volle Streifenbreite genutzt.
					if (k < (segNr - 1)) {
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
				}
			else // fall2: start bei 1, da das erste Segment vom Turnaround berechnet ist.
				for (int k = 1; k < (segNr); k++) {
					if (k == 1) {
						vorherigeLaenge = (w + 2.0 * m) / Calculator.myTan(winkel2);
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
						old_actionPoint = actionPoint1;
					}
					if (k == 2) {
						vorherigeLaenge = vorherigeLaenge + (w + m) / Calculator.myTan(winkel1);
						actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
						OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
						OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
						old_actionPoint = actionPoint1;
					} else {
						if (alternating) {
							vorherigeLaenge = (2.0 * m) / Calculator.myTan(winkel2);
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
							OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
							old_actionPoint = actionPoint1;
							alternating = false;
						} else {
							vorherigeLaenge = (2.0 * m) / Calculator.myTan(winkel1);
							actionPoint1 = actionPoint1 + vorherigeLaenge; // update actionPoint
							OutputHandler.add(1, actionPoint1, 0, old_actionPoint, 0);
							OutputHandler.add(1, actionPoint1, w, old_actionPoint, w); // fuege in output hinzu
							old_actionPoint = actionPoint1;
							alternating = true;
						}
					}
					if (k < (segNr - 1)) {
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
				}

			return new Streifenvar(obenUnten1, actionPoint1);

		} else

		{ // normales Dreieck
			// setze Winkel richtig, so dass das Startsegment als "bereits berechnet"
			// Benennung; winkel1 ist wo der Papierstreifen das Dreiek betritt,
			// und winkel2 ist der Winkel wo wir ggf. unseren "Turn around" haben.
			if (vorherigeZielkante == null || Calculator.istPunktAufKante(vorherigeZielkante, p3)) { // Error:
																										// Dreiecknr1?
				winkel1 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel2 = Calculator.calculation_of_angle(p3, p2, p1);
			} else {
				winkel2 = Calculator.calculation_of_angle(p2, p3, p1);
				winkel1 = Calculator.calculation_of_angle(p3, p2, p1);
			}
			boolean LR = true;
			for (int k = 0; k < (segNr - 1); k++) {
				if (k == 0) { // erster schritt bereits berechnet nur ein Teil das fehlt
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

	}
}
