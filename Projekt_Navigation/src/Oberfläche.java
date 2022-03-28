import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class Oberfl�che extends JFrame {
	private Oberfl�chePanel panel;

	public Oberfl�che(ArrayList<Knoten> k, ArrayList<Knoten> alle, Navigation navi) {
		// Erzeugen des JPanels, auf dem sp�ter gemalt werden soll
		panel = new Oberfl�chePanel(k, alle, navi);
		add(panel);

		setSize(1400, 1000); // 1000, 1000
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	}

}
