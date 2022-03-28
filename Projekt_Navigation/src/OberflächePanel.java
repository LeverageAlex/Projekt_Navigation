import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class OberflächePanel extends JPanel {
	private ArrayList<Knoten> knoten, weg;
	private Navigation navi;
	private double zoom;
	private JButton findR;
//	private JTextField start, ziel;
	private Strecke v;
	private Color[] farbenListe;
	JComboBox<String> startBox, zielBox;

	public OberflächePanel(ArrayList<Knoten> wege, ArrayList<Knoten> k, Navigation navi) {
		// WindowsDesign wird standardmäßig gewählt
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		farbenListe = new Color[] { new Color(0, 191, 255), Color.blue, Color.GREEN, Color.PINK, Color.ORANGE,
				Color.magenta };
		knoten = k;
		this.navi = navi;
		this.weg = wege;
		zoom = Zoom();
		System.out.println(zoom);
		findR = new JButton("Finde kürzeste Route");
		this.setLayout(null);
		findR.setBounds(1125, 300, 200, 50);
		this.add(findR);
		// Umwandeln der Dijkstra-Liste in einen Array für die JComboBox
		ArrayList<String> autocompleteList = new ArrayList<String>();

		for (int i = 0; i < navi.gibAlleKnoten().size(); i++) {
			if(navi.BreitengradzuX(navi.gibAlleKnoten().get(i)) > 300 && navi.BreitengradzuX(navi.gibAlleKnoten().get(i)) < 900 && navi.LängengradzuY(navi.gibAlleKnoten().get(i)) > 700 && navi.LängengradzuY(navi.gibAlleKnoten().get(i)) < 1800) {
			autocompleteList.add(navi.gibAlleKnoten().get(i).getName());
			}
		}
		String[] autocomplete = autocompleteList.toArray(new String[0]);
		Arrays.sort(autocomplete);
		/* start = new JTextField("Allgäuer Straße"); */ startBox = /* new AutoComplete */ new JComboBox<String>(
				autocomplete);
		// Autovervollständigung
		AutoCompleteDecorator.decorate(this.startBox);

		// startBox.getEditor().getItem();
		zielBox = new JComboBox<String>(autocomplete);
		AutoCompleteDecorator.decorate(this.zielBox);
//		ziel = new JTextField("friedhof");
		// start.setBounds(1125, 100, 200, 30);
		JLabel start_label = new JLabel("Startpunkt:");
		JLabel ziel_label = new JLabel("Zielpunkt:");

		startBox.setBounds(1125, 100, 200, 30);
		zielBox.setBounds(1125, 200, 200, 30);
		start_label.setBounds(1125, 70, 200, 30);
		ziel_label.setBounds(1125, 170, 200, 30);
		startBox.setEditable(true);
		zielBox.setEditable(true);
		this.add(ziel_label);
		this.add(start_label);
//		this.add(ziel);
		// this.add(start);
		this.add(startBox);
		this.add(zielBox);
		
		JLabel laufstrecke2 = new JLabel("Laufstreckenlänge");
		
		JRadioButton kurz = new JRadioButton("Kurz");
		JRadioButton mittel = new JRadioButton("Mittel");
		JRadioButton lang = new JRadioButton("Lang");
		
        ButtonGroup gruppe = new ButtonGroup();
		gruppe.add(kurz);
		gruppe.add(mittel);
		gruppe.add(lang);
		
		kurz.setBounds(1125, 820, 200, 50);
		mittel.setBounds(1125, 855, 200, 50);
		lang.setBounds(1125, 890, 200, 50);
		laufstrecke2.setBounds(1125, 770, 200, 50);
		laufstrecke2.setFont(new Font("TimesRoman", Font.PLAIN,20));
		this.add(laufstrecke2);
		this.add(kurz);
		this.add(mittel);
		this.add(lang);

		// Finde kürzeste Route-Knopf wird Funktion bei Klick zugewiesen
		findR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Knoten starts = navi.namezuKnoten(/* start.getText()); */(String) startBox.getEditor().getItem());
				Knoten ziels = navi.namezuKnoten(/* ziel.getText()); */ (String) zielBox.getEditor().getItem());
				// Berechnung des Dijkstras vom Startpunkt aus
				navi.Dijkstra(starts);
				// Knoten in einer Liste zurückgeben
				weg = navi.kuerzesterWeg(ziels);
				if (weg.size() <= 1) {
					return;
				}
				// Berechnung des Zoom-Koeffizienten
				zoom = Zoom();
				// Dijkstra-Liste wird umgedreht (richtig herum)
				ArrayList<Knoten> invertiert = new ArrayList<Knoten>();
				for (int z = 0; z < weg.size(); z++) {
					invertiert.add(weg.get(weg.size() - z - 1));

				}
				weg = invertiert;
				v = navi.gibBesteLinien(invertiert);
				System.out.println("nochvorhanden");

				System.out.println(v.getLinien());
				System.out.println(v.getDistanzen());
				repaint();

			}
		});

		
		kurz.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				navi.setLaufstrecke(4.0);
				navi.alleKantenLöschen();
				navi.xmlEinlesenLinien();
				navi.naheKnotenKantenEinfuegen(navi.gibAlleKnoten());
			}
		});
		mittel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				navi.setLaufstrecke(6.0);
				navi.alleKantenLöschen();
				navi.xmlEinlesenLinien();
				navi.naheKnotenKantenEinfuegen(navi.gibAlleKnoten());
				
			}
		});
		lang.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				navi.setLaufstrecke(8.0);
				navi.alleKantenLöschen();
				navi.xmlEinlesenLinien();
				navi.naheKnotenKantenEinfuegen(navi.gibAlleKnoten());
				
			}
		});
	}

	@Override
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		// Graphics wird zu Graphics2D gecastet, um mehr Geometrische Formen zu erlangen
		Graphics2D g = (Graphics2D) g2;

		// Kleinste X und Y Werte werden für den Zoom benötigt (ausschneiden)
		int kleinstesX = kleinstesX();
		int kleinstesY = kleinstesY();
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		for (int i = 0; i < knoten.size(); i++) {
			g.setColor(new Color(92, 82, 82, 70));
			g.fillOval((int) ((navi.BreitengradzuX(knoten.get(i)) - kleinstesX) * zoom),
					(int) ((navi.LängengradzuY(knoten.get(i)) - kleinstesY) * zoom), 16, 16);
			g.drawString(knoten.get(i).getName(), (int) ((navi.BreitengradzuX(knoten.get(i)) - kleinstesX) * zoom),
					(int) ((navi.LängengradzuY(knoten.get(i)) - kleinstesY) * zoom));

		}

		int index = 0;
		// Zeichnen von den Haltestellen, inklusive deren Verbindungen
		for (int i = 0; i < weg.size(); i++) {
			if (i + 1 < weg.size()) {

				int aktuelleDistanz = i;

				if (v != null) {

					if (aktuelleDistanz >= v.getDistanzen().get(index)) {

						index++;
					}

				}
				if (index > farbenListe.length - 1) {
					index = 0;
				}
				g.setColor(farbenListe[index]);

				g.setStroke(new BasicStroke(3));
				g.drawLine((int) ((navi.BreitengradzuX(weg.get(i)) - kleinstesX) * zoom) + 8,
						(int) ((navi.LängengradzuY(weg.get(i)) - kleinstesY) * zoom) + 8,
						(int) ((navi.BreitengradzuX(weg.get(i + 1)) - kleinstesX) * zoom) + 8,
						(int) ((navi.LängengradzuY(weg.get(i + 1)) - kleinstesY) * zoom) + 8);

			}
			g.setColor(Color.red);
			g.fillOval((int) ((navi.BreitengradzuX(weg.get(i)) - kleinstesX) * zoom),
					(int) ((navi.LängengradzuY(weg.get(i)) - kleinstesY) * zoom), 16, 16);

		}
		// g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(Color.black);
		for (int i = 0; i < weg.size(); i++) {
			g.drawString(weg.get(i).getName(), (int) ((navi.BreitengradzuX(weg.get(i)) - kleinstesX) * zoom),
					(int) ((navi.LängengradzuY(weg.get(i)) - kleinstesY) * zoom));

		}

		// rechte GUI
		g.setColor(Color.gray);
		g.fillRect(1050, 0, 1400, 1000);
		g.setColor(new Color(185, 215, 220));
		g.fillRect(1125, 380, 200, 40);
		g.setColor(Color.black);
		g.drawString("Befahrene Linien:", 1150, 405);
		if (v != null) {
			g.setColor(new Color(185, 215, 220));
			g.fillRect(1125, 410, 200, 20 + 30 * v.getLinien().size());
			for (int i = 0; i < v.getLinien().size(); i++) {

				g.setColor(Color.black);
				g.drawOval(1150, 433 + 30 * i, 2, 2);
				g.setColor(farbenListe[i]);
				g.drawString(v.getLinien().get(i), 1170, 440 + 30 * i);

			}
		}

	}

	// Gibt einen Zoomkoeffizienten zurück, welcher benutzt wird, damit die
	// ausgewählte Route in Vollbild dargestellt werden kann
	public double Zoom() {

		int x = groesstesX() - kleinstesX();
		int y = groesstesY() - kleinstesY();
		double faktor = 1.0;

		while (x < 960 && y < 960) {
			x = (int) ((groesstesX() - kleinstesX()) * faktor);
			y = (int) ((groesstesY() - kleinstesY()) * faktor);
			faktor += 0.05;

		}
		faktor -= 0.05;
		System.out.println((groesstesX() - kleinstesX()) * faktor);
		System.out.println((groesstesY() - kleinstesY()) * faktor);
		return faktor;
	}

	public int kleinstesX() {
		int kleinstesX = Integer.MAX_VALUE;
		for (int i = 0; i < weg.size(); i++) {
			if (navi.BreitengradzuX(weg.get(i)) < kleinstesX) {
				kleinstesX = (int) navi.BreitengradzuX(weg.get(i));
			}

		}

		return kleinstesX - 20;
	}

	public int kleinstesY() {

		int kleinstesY = Integer.MAX_VALUE;
		for (int i = 0; i < weg.size(); i++) {
			if (navi.LängengradzuY(weg.get(i)) < kleinstesY) {
				kleinstesY = (int) navi.LängengradzuY(weg.get(i));
			}

		}

		return kleinstesY - 20;

	}

	public int groesstesX() {
		int gX = 0;
		for (int i = 0; i < weg.size(); i++) {
			if (navi.BreitengradzuX(weg.get(i)) > gX) {
				gX = (int) navi.BreitengradzuX(weg.get(i));
			}

		}

		return gX + 20;
	}

	public int groesstesY() {

		int groesstesY = 0;
		for (int i = 0; i < weg.size(); i++) {
			if (navi.LängengradzuY(weg.get(i)) > groesstesY) {
				groesstesY = (int) navi.LängengradzuY(weg.get(i));
			}

		}

		return groesstesY + 20;

	}

}
