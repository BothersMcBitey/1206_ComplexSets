
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import gui.ComplexSetViewerPanel;
import numbers.MandlebrotSet;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Complex Set Viewer");
		frame.setSize(800, 850);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(null);
		frame.setContentPane(mainPanel);
		
		ComplexSetViewerPanel mandlebrotViewer = new ComplexSetViewerPanel(new MandlebrotSet(100), 800, 800);
		mandlebrotViewer.setLocation(0, 0);
		mainPanel.add(mandlebrotViewer);
		
		mainPanel.repaint();		
		frame.setVisible(true);
	}

}
