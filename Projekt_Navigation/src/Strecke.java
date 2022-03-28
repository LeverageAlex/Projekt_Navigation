import java.util.ArrayList;

public class Strecke {

	private ArrayList<String> linien;
	private ArrayList<Integer> distanzen;

	public Strecke(ArrayList<String> linien, ArrayList<Integer> distanzen) {
		this.linien = linien;
		this.distanzen = distanzen;
	}

	public ArrayList<String> getLinien() {
		return linien;
	}

	public void setLinien(ArrayList<String> linien) {
		this.linien = linien;
	}

	public ArrayList<Integer> getDistanzen() {
		return distanzen;
	}

	public void setDistanzen(ArrayList<Integer> distanzen) {
		this.distanzen = distanzen;
	}

}
