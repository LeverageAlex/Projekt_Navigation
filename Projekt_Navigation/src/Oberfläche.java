import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Oberfläche extends JFrame {
	private OberflächePanel panel;

	public Oberfläche(ArrayList<Knoten> k, ArrayList<Knoten> alle, Navigation navi) {
		// Erzeugen des JPanels, auf dem später gemalt werden soll
		panel = new OberflächePanel(k, alle, navi);
		add(panel);

		setSize(1400, 1000); // 1000, 1000
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

}
