import java.util.ArrayList;

public class Kante {

	private Knoten ziel;
	private ArrayList<String> linie;

	public Kante(Knoten ziel) {
		this.ziel = ziel;
		linie = new ArrayList<String>();

	}

	// Linie wird der Liste hinzugefügt
	public void addLinie(String linie) {
		this.linie.add(linie);
	}

	public ArrayList<String> getLinie() {
		return linie;

	}

	public Knoten getZiel() {
		return ziel;
	}

	public void setZiel(Knoten ziel) {
		this.ziel = ziel;
	}

}
