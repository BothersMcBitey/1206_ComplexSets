import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gui.ComplexSetViewerPanel;
import numbers.ComplexNumber;
import numbers.JuliaSet;
import numbers.MandelbrotSet;

@SuppressWarnings("serial")
public class FractalExplorer extends JFrame{
	
	private ComplexNumber userSelectedPoint;
	
	private ComplexSetViewerPanel mandelbrotViewer;
	private ComplexSetViewerPanel juliaViewer;
	
	private JLabel userPointLbl;
	private JPanel mainPanel;
	
	private int cpHeight = 100; 

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
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel(null);
		setContentPane(mainPanel);
		
		mandelbrotInit();	
		controlsInit();
			
		setVisible(true);
		setResizable(false);
	}
	
	private void mandelbrotInit(){
		int h = getHeight() - ((getHeight()/10 < cpHeight) ? cpHeight : getHeight()/10);
		mandelbrotViewer = new ComplexSetViewerPanel(new MandelbrotSet(100), -2f, 0.8f, -1.6f, 1.6f);
		mandelbrotViewer.setBounds(0, 0, getWidth(), h);
		mandelbrotViewer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				userSelectedPoint = mandelbrotViewer.getComplexAtPoint(e.getPoint());
				userPointLbl.setText("Point selected: " + userSelectedPoint.toString());
				displayJuliaSet();
			}
		});	
		mainPanel.add(mandelbrotViewer);
	}
	
	private void controlsInit(){
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		controlPanel.setBounds(0, mandelbrotViewer.getHeight(), getWidth(), (getHeight()/10 < cpHeight) ? cpHeight : getHeight()/10);
		
		userPointLbl = new JLabel("Point selected: 0 + 0i");
		controlPanel.add(userPointLbl);
		
		JLabel realZoomLbl = new JLabel("Zoom (R)");
		controlPanel.add(realZoomLbl);
		JSpinner realZoom = new JSpinner(new SpinnerNumberModel(1, 1, 20, 0.5));
		realZoom.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setSize((int) (mandelbrotViewer.getWidth()*(Double)realZoom.getValue()), mandelbrotViewer.getHeight());
				revalidate();
			}
		});
		controlPanel.add(realZoom);
		
		JLabel imaginaryZoomLbl = new JLabel("Zoom (I)");
		controlPanel.add(imaginaryZoomLbl);
		JSpinner imaginaryZoom = new JSpinner(new SpinnerNumberModel(1, 1, 20, 0.5));
		imaginaryZoom.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setSize(mandelbrotViewer.getWidth(), (int) (mandelbrotViewer.getHeight()*(Double)imaginaryZoom.getValue()));
				revalidate();
			}
		});
		controlPanel.add(imaginaryZoom);
		
		mainPanel.add(controlPanel);
	}
	
	private void displayJuliaSet(){			
		if(juliaViewer == null){
			mandelbrotViewer.setBounds(0, 0, getWidth()/2, mandelbrotViewer.getHeight());
		} else {
			mainPanel.remove(juliaViewer);
		}
		
		juliaViewer = new ComplexSetViewerPanel(new JuliaSet(100, userSelectedPoint));
		juliaViewer.setBounds(getWidth()/2, 0, getWidth()/2, mandelbrotViewer.getHeight());
		mainPanel.add(juliaViewer);
		revalidate();
		repaint();
	}
}
