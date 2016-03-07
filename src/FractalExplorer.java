
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import gui.ComplexSetViewerPanel;
import numbers.ComplexNumber;
import numbers.MandlebrotSet;

@SuppressWarnings("serial")
public class FractalExplorer extends JFrame{
	
	private ComplexNumber userSelectedPoint;
	
	private JLabel userPointLbl;
	private JPanel mainPanel;

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {new FractalExplorer().init();}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public FractalExplorer() {
		super("Fractal Explorer");
	}
	
	public void init(){
		setSize(Toolkit.getDefaultToolkit().getScreenSize());	
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel(null);
		setContentPane(mainPanel);
		
		mandelbrotInit();		
		controlsInit();
			
		setVisible(true);
		setResizable(false);
	}
	
	private void mandelbrotInit(){
		ComplexSetViewerPanel mandlebrotViewer = new ComplexSetViewerPanel(new MandlebrotSet(100));
		mandlebrotViewer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				userSelectedPoint = mandlebrotViewer.getComplexAtPoint(e.getPoint());
				userPointLbl.setText("Point selected: " + userSelectedPoint.toString());
				revalidate();
			}
		});
		int x = (getWidth()/5 < 500) ? 500 : getWidth()/5;
		int w = getWidth() - x;
		mandlebrotViewer.setBounds(x,0,w,getHeight());
		mainPanel.add(mandlebrotViewer);
	}
	
	private void controlsInit(){
		JPanel controlPanel = new JPanel();
		controlPanel.setBounds(0, 0, (getWidth()/5 < 500) ? 500 : getWidth()/5, getHeight());
		
		userPointLbl = new JLabel("Point selected: ");
		controlPanel.add(userPointLbl);
		
		mainPanel.add(controlPanel);
	}
}
