import java.util.ArrayList;

public class Knoten {

	private boolean markierung;
	private ArrayList<Kante> kanten;
	private int distanz;
	private String name;
	private Knoten vorgänger;
	private double lat, lon;
	private ArrayList<Long> id;

	public Knoten(String name) {

		kanten = new ArrayList<Kante>();
		id = new ArrayList<Long>();
		this.setName(name);
		distanz = Integer.MAX_VALUE;

	}

	public ArrayList<Kante> getKanten() {
		return kanten;
	}

	public void addKanten(Kante kanten) {
		this.kanten.add(kanten);
	}
	public void setKantenListe(ArrayList<Kante> kante) {
		this.kanten = kante;
		
	}

	public int getDistanz() {
		return distanz;
	}

	public void setDistanz(int distanz) {
		this.distanz = distanz;
	}

	public boolean isMarkierung() {
		return markierung;
	}

	public void setMarkierung(boolean markierung) {
		this.markierung = markierung;
	}

	public Knoten getVorgänger() {
		return vorgänger;
	}

	public void setVorgänger(Knoten vorgänger) {
		this.vorgänger = vorgänger;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getxKord() {
		return lat;
	}

	public void setxKord(double xKord) {
		this.lat = xKord;
	}

	public double getyKord() {
		return lon;
	}

	public void setyKord(double yKord) {
		this.lon = yKord;
	}

	public ArrayList<Long> getId() {
		return id;
	}
	public void addIdListe(ArrayList<Long> k) {
		for(int i = 0; i < k.size(); i++) {
			id.add(k.get(i));
		}
	}

	public void addId(long id) {
		this.id.add(id);
	}



}
