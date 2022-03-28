import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class Navigation {

	private ArrayList<Knoten> gefunden, alleKnoten;
	private Document doc;
	private double laufstrecke;

	public Navigation() {

		alleKnoten = new ArrayList<Knoten>();
		gefunden = new ArrayList<Knoten>();

		xmlEinlesenKnoten();
		xmlEinlesenLinien();
		long knot = 6263471330l;
		long ziel = 6263286469l;

		Knoten start = idzuKnoten(knot);
		// System.out.println(start.getName());

		Dijkstra(start);

		Knoten refer = idzuKnoten(ziel);
		// kuerzester Weg
		// System.out.println(refer.getKanten());

		ArrayList<Knoten> kurz = kuerzesterWeg(refer);
		System.out.println("Breite: " + BreitengradzuX(idzuKnoten(1874800654l)));
		System.out.println("Länge: " + LängengradzuY(idzuKnoten(1874800654l)));

		// Erzeugen der Grafischen Oberfläche
		naheKnotenKantenEinfuegen(alleKnoten);
		new Oberfläche(kurz, alleKnoten, this);

	}

	// Der bekannte Dijkstra Algorithmus - Erkärung dazu kann im Internet
	// nachgelesen werden
	public void Dijkstra(Knoten startKnoten) {
		
		// Alle existierende Knoten werden zurückgesetzt auf Standardwerte
		for (int i = 0; i < alleKnoten.size(); i++) {
			alleKnoten.get(i).setDistanz(Integer.MAX_VALUE);
			alleKnoten.get(i).setMarkierung(false);
			alleKnoten.get(i).setVorgänger(null);
		}

		startKnoten.setDistanz(0);
		startKnoten.setMarkierung(true);
		gefunden.add(startKnoten);
		while (gefunden.size() > 0) {
			Knoten aktuellerKnoten = gefunden.remove(0);
//		System.out.println(aktuellerKnoten.getName() + " - " + aktuellerKnoten.getDistanz());
			aktuellerKnoten.setMarkierung(true);
			// System.out.println(aktuellerKnoten.getName());
			for (int i = 0; i < aktuellerKnoten.getKanten().size(); i++) {
				
				if (istEnthalten(aktuellerKnoten.getKanten().get(i).getZiel()) && aktuellerKnoten.getDistanz()
						+ 1 < aktuellerKnoten.getKanten().get(i).getZiel().getDistanz()) {
					aktuellerKnoten.getKanten().get(i).getZiel().setDistanz(aktuellerKnoten.getDistanz() + 1);
					aktuellerKnoten.getKanten().get(i).getZiel().setVorgänger(aktuellerKnoten);
					Collections.sort(gefunden, new Sortierungskriterium());

				} else if (!aktuellerKnoten.getKanten().get(i).getZiel().isMarkierung()) {
					gefunden.add(aktuellerKnoten.getKanten().get(i).getZiel());

					aktuellerKnoten.getKanten().get(i).getZiel().setDistanz(aktuellerKnoten.getDistanz() + 1);
					aktuellerKnoten.getKanten().get(i).getZiel().setVorgänger(aktuellerKnoten);
					Collections.sort(gefunden, new Sortierungskriterium());

				}

			}

		}
	}

	// Hiermit werden alle Knoten eingelesen
	public void xmlEinlesenKnoten() {
		try {
			doc = new SAXBuilder().build("Daten XML.xml");
		} catch (JDOMException e1) {

			e1.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		Element fahrplan = doc.getRootElement();

		List<Element> haltestelle = fahrplan.getChildren("node");

		for (int i = 0; i < haltestelle.size(); i++) {
			List<Element> tags = haltestelle.get(i).getChildren("tag");
			for (int z = 0; z < tags.size(); z++) {
				if (tags.get(z).getAttributeValue("k").equals("name")) {
					String name = tags.get(z).getAttributeValue("v");
					long id = Long.parseLong(haltestelle.get(i).getAttributeValue("id"));
					double lat = Double.parseDouble(haltestelle.get(i).getAttributeValue("lat"));
					double lon = Double.parseDouble(haltestelle.get(i).getAttributeValue("lon"));
					Knoten erg = namezuKnoten(name);
					if (erg == null) {
						Knoten k = new Knoten(name);
						k.addId(id);
						k.setxKord(lat);
						k.setyKord(lon);
						alleKnoten.add(k);
					} else {
						erg.addId(id);
					}
					break;
				}

			}

		}

		/**
		 * Ab hier wird die 2 DB (Haltestellen.txt) eingelesen
		 */

		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("db/Haltestellen.txt"), "UTF-8"));
			// System.out.println(br.readLine());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		String p = null;
		try {
			p = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (p != null) {
			// erhalten der ID

			int pos = findeLeerZeichen(0, p);

			long id = Long.parseLong(p.substring(0, pos));

			int pos2 = findeLeerZeichen(pos + 1, p);
			// System.out.println(p.substring(pos+1, pos2));
			double lat = Double.parseDouble(p.substring(pos + 1, pos2));

			pos = findeLeerZeichen(pos2 + 1, p);
			double lon = Double.parseDouble(p.substring(pos2, pos));

			String name = p.substring(pos + 1);
			// System.out.println(name);
			if (namezuKnoten(name) == null) {
				Knoten k = new Knoten(name);
				k.addId(id);
				k.setxKord(lat);
				k.setyKord(lon);
				alleKnoten.add(k);
				// System.out.println(p);

			} else {
				// System.out.println(idzuKnoten(id).getName());
			}

			try {
				p = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// Einlesen von Linien zwischen den Knoten
	public void xmlEinlesenLinien() {
		try {
			doc = new SAXBuilder().build("Daten XML.xml");
		} catch (JDOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Element fahrplan = doc.getRootElement();

		List<Element> Buslinien = fahrplan.getChildren("relation");

		for (int z = 0; z < Buslinien.size(); z++) {
			List<Element> members = Buslinien.get(z).getChildren("member");

			for (int i = 0; i < members.size(); i++) {
				if (members.get(i).getAttributeValue("type").equals("node")
						&& (members.get(i).getAttributeValue("role").equals("stop")
								|| members.get(i).getAttributeValue("role").equals(""))) {
					Knoten startKnoten = idzuKnoten(Long.parseLong(members.get(i).getAttributeValue("ref")));
					if (startKnoten == null)
						continue;
//					System.out.println("Referenz: " + Long.parseLong(members.get(i).getAttributeValue("ref")));
					for (int zweiter = i + 1; zweiter < members.size(); zweiter++) {
						if (members.get(zweiter).getAttributeValue("type").equals("node")
								&& (members.get(zweiter).getAttributeValue("role").equals("stop")
										|| members.get(zweiter).getAttributeValue("role").equals(""))) {
							Knoten endKnoten = idzuKnoten(
									Long.parseLong(members.get(zweiter).getAttributeValue("ref")));
							if (endKnoten == null)
								continue;
							String liniennummer = null;
							List<Element> tags = Buslinien.get(z).getChildren("tag");
							for (int u = 0; u < tags.size(); u++) {
								if (tags.get(u).getAttributeValue("k").equals("ref")) {
									liniennummer = tags.get(u).getAttributeValue("v");
									break;
								}
							}

							Kante k = isKanteVorhanden(startKnoten, endKnoten);
							if (k == null) {
								k = new Kante(endKnoten);
								startKnoten.addKanten(k);
							}
							k.addLinie(liniennummer);
							break;
						}

					}
				}
			}
		}
	}

	public Kante isKanteVorhanden(Knoten start, Knoten ziel) {
//System.out.println("startknoten" + start);
		for (int u = 0; u < start.getKanten().size(); u++) {
			// System.out.println("Durchlauf: " + start.getKanten().get(u).getZiel());
			if (start.getKanten().get(u).getZiel().equals(ziel)) {
				return start.getKanten().get(u);
			}
		}

		return null;
	}

	public Knoten namezuKnoten(String name) {
		for (int i = 0; i < alleKnoten.size(); i++) {
			if (alleKnoten.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
				return alleKnoten.get(i);
			}
		}

		return null;
	}

	public Knoten idzuKnoten(long id) {
		for (int i = 0; i < alleKnoten.size(); i++) {
			for (int z = 0; z < alleKnoten.get(i).getId().size(); z++) {
				if (alleKnoten.get(i).getId().get(z) == id) {
					return alleKnoten.get(i);
				}
			}
		}
		// System.out.println("Error @ idzuKnoten");
		return null;

	}

	public boolean istEnthalten(Knoten k) {
		for (int i = 0; i < gefunden.size(); i++) {
			if (k.equals(gefunden.get(i))) {
				return true;
			}

		}
		return false;
	}

	public ArrayList<Knoten> kuerzesterWeg(Knoten k) {
		ArrayList<Knoten> list = new ArrayList<Knoten>();
		Knoten aktuell = k;
		list.add(aktuell);
		while (aktuell.getVorgänger() != null) {

			list.add(aktuell.getVorgänger());
			aktuell = aktuell.getVorgänger();
		}
		if(list.size() < 2) {
			JOptionPane.showMessageDialog(null, "Es existiert keine Busstrecke zu zwischen diesen Haltestellen", "Fehler!", JOptionPane.ERROR_MESSAGE); 
		}
		return list;
	}

	public double BreitengradzuX(Knoten k) {

		double x = k.getxKord() - 48.32;
		double kordX = 7500.0 * x;
		// double kordX = 10000.0*x;
		// System.out.println(kordX);
		return kordX;
	}

	public int findeLeerZeichen(int startIndex, String p) {

		for (int z = startIndex; z < p.length(); z++) {
			char c = p.charAt(z);
			if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
				// System.out.println(z);

				return z;
			}
		}
		return -1;

	}

	public double LängengradzuY(Knoten k) {

		double y = k.getyKord() - 9.8;
		double kordY = 7500.0 * y;
		// System.out.println(kordY);
		return kordY;
	}

	public boolean linieVorhandenCheck(ArrayList<String> liste, String Linie) {

		for (int i = 0; i < liste.size(); i++) {
			if (liste.get(i).equals(Linie)) {
				return true;
			}
		}

		return false;
	}

	// Gibt beste Linien aus einer Knoten-Liste, welche befahren werden sollen,
	// zurück
	public Strecke gibBesteLinien(ArrayList<Knoten> weg) {
		ArrayList<String> speichernvonLinien = new ArrayList<String>();
		ArrayList<Integer> speichernvonLinien_distanz = new ArrayList<Integer>();
		ArrayList<String> aktuelleLinien = null;
		ArrayList<String> aktuelleLinien_Knoten = null;
		for (int i = 0; i < weg.size() - 1; i++) {
			// System.out.println("Weg 1: " + weg.get(i).getName() + " Weg 2: " +
			// weg.get(i+1).getName());
			Kante aktuelleKante = isKanteVorhanden(weg.get(i), weg.get(i + 1));
			// System.out.println(aktuelleKante.getZiel().getName());
			// System.out.println(aktuelleKante.getLinie().get(0));
			aktuelleLinien_Knoten = aktuelleKante.getLinie();
			for (int j = 0; j < aktuelleLinien_Knoten.size(); j++) {
				if (aktuelleLinien == null) {
					aktuelleLinien = new ArrayList<String>();
					// Alle Linien hinzufügen in startListe

					for (int x = 0; x < aktuelleLinien_Knoten.size(); x++) {
						aktuelleLinien.add(aktuelleLinien_Knoten.get(x));
					}

				} else {
					// Schauen ob in Liste bereits ähnliche Linie ist
					for (int x = 0; x < aktuelleLinien.size(); x++) {
						if (aktuelleLinien.size() > 1) {
							if (!linieVorhandenCheck(aktuelleLinien_Knoten, aktuelleLinien.get(x))) {
								aktuelleLinien.remove(x);
								x--;

							}
						} else {
							if (!linieVorhandenCheck(aktuelleLinien_Knoten,
									aktuelleLinien.get(0))/* && i < weg.size()-2 */) {
								speichernvonLinien.add(aktuelleLinien.get(0));
								speichernvonLinien_distanz.add(i);
								// System.out.println("Distanz " + i);
								// System.out.println("Folge Linie wurde gespeichert: " +
								// aktuelleLinien.get(0));
								aktuelleLinien = null;
								break;
							}
						}
					}

				}

			}

		}
		// Abschluss Bedingung
		if (aktuelleLinien != null) {
			speichernvonLinien.add(aktuelleLinien.get(0));
			speichernvonLinien_distanz.add(weg.size() - 1);
		} else {
			speichernvonLinien.add(aktuelleLinien_Knoten.get(0));
			speichernvonLinien_distanz.add(weg.size() - 1);
		}
		// speichernvonLinien.add(aktuelleLinien.get(0));
//		Object[] obj = { speichernvonLinien, speichernvonLinien_distanz };
		return new Strecke(speichernvonLinien, speichernvonLinien_distanz);


	}

	public ArrayList<Knoten> gibAlleKnoten() {
		return alleKnoten;
	}
	
	public double abstandZuKnoten(Knoten k1, Knoten k2) {
		double abstandX = BreitengradzuX(k2) - BreitengradzuX(k1);
		double abstandY = LängengradzuY(k2) - LängengradzuY(k1);
		//System.out.println("Sqrt: " + Math.sqrt(abstandX*abstandX + abstandY*abstandY));
		return Math.sqrt(abstandX*abstandX + abstandY*abstandY);
	}
	
	public void naheKnotenKantenEinfuegen(ArrayList<Knoten> weg) {
		int anzahl = 0;
		for(int i = 0; i < weg.size(); i++) {
			for(int z = 0; z < weg.size(); z++) {
				if(i != z) {
					if(abstandZuKnoten(weg.get(i), weg.get(z)) < laufstrecke ) {
						Kante neueKante = new Kante(weg.get(z));
						neueKante.addLinie("laufen");
						weg.get(i).getKanten().add(neueKante);
						anzahl++;
					}
				}
			}
		}
		System.out.println("Anzahl " + anzahl);
	}
	
	public void setLaufstrecke(double laufstrecke) {
		this.laufstrecke = laufstrecke;
		
	}
	
	public void alleKantenLöschen() {
		for(int i = 0; i < alleKnoten.size(); i++) {
			alleKnoten.get(i).getKanten().clear();
			
		}
		
	}
	

	// Startmethode
	public static void main(String[] args) {
		new Navigation();
	}

}
