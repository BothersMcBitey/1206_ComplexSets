import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import complexMaths.*;
import gui.*;

@SuppressWarnings("serial")
public class FractalExplorer extends JFrame{
	
	private ComplexNumber userSelectedPoint;
	
	private ComplexSetViewerPanel mandelbrotViewer;
	private ComplexSetViewerPanel juliaViewer;
	
	private JLabel userPointLbl;
	private JPanel mainPanel;
	
	private int cpHeight = 60; 

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {new FractalExplorer().init();}
			});
		} catch (InvocationTargetException | InterruptedException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
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
		
		setsInit();	
		controlsInit();
			
		setVisible(true);
		setResizable(false);
	}
	
	private void setsInit(){
		int h = getHeight() - ((getHeight()/10 < cpHeight) ? cpHeight : getHeight()/10);
		mandelbrotViewer = new ComplexSetViewerPanel(new MandelbrotSet(100), -2f, 0.8f, -1.6f, 1.6f);
		mandelbrotViewer.setBounds(0, 0, getWidth()/2, h);
		mandelbrotViewer.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				userSelectedPoint = mandelbrotViewer.getComplexAtPoint(e.getPoint());
				userPointLbl.setText("Point selected: " + userSelectedPoint.toString());
				juliaViewer.updateSet(new JuliaSet(200, userSelectedPoint));
			}
		});	
		mainPanel.add(mandelbrotViewer);
		
		userSelectedPoint = new ComplexNumber(0, 0);
		juliaViewer = new ComplexSetViewerPanel(new JuliaSet(100, userSelectedPoint));
		juliaViewer.setBounds(getWidth()/2, 0, getWidth()/2, mandelbrotViewer.getHeight());
		mainPanel.add(juliaViewer);
	}
	
	private void controlsInit(){
		cpHeight = (getHeight()/10 < cpHeight) ? cpHeight : getHeight()/10;
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBounds(0, mandelbrotViewer.getHeight(), getWidth(), cpHeight);
		
		controlPanel.add(boundsInit());
		
		controlPanel.add(storageInit());
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		controlPanel.add(exit);
		
		mainPanel.add(controlPanel);
	}
	
	private JPanel boundsInit(){
		JPanel boundsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		boundsPanel.setBounds(0, mandelbrotViewer.getHeight(), getWidth(), cpHeight);
		
		userPointLbl = new JLabel("Point selected: 0 + 0i");
		boundsPanel.add(userPointLbl);
		
		JLabel realMinLbl = new JLabel("Real min");
		boundsPanel.add(realMinLbl);
		JSpinner realMin = new JSpinner(new SpinnerNumberModel(-2, -2, 1, 0.25));
		realMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setRealMin(((Double)realMin.getValue()).floatValue());
			}
		});
		boundsPanel.add(realMin);
		
		JLabel realMaxLbl = new JLabel("Real max");
		boundsPanel.add(realMaxLbl);
		JSpinner realMax = new JSpinner(new SpinnerNumberModel(1, -2, 1, 0.25));
		realMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setRealMax(((Double)realMax.getValue()).floatValue());
			}
		});
		boundsPanel.add(realMax);
		
		JLabel imaginaryMinLbl = new JLabel("Imaginary min");
		boundsPanel.add(imaginaryMinLbl);
		JSpinner imaginaryMin = new JSpinner(new SpinnerNumberModel(-1.6, -1.6, 1.6, 0.2));
		imaginaryMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setImaginaryMin(((Double)imaginaryMin.getValue()).floatValue());
			}
		});
		boundsPanel.add(imaginaryMin);
		
		JLabel imaginaryMaxLbl = new JLabel("Imaginary max");
		boundsPanel.add(imaginaryMaxLbl);
		JSpinner imaginaryMax = new JSpinner(new SpinnerNumberModel(1.6, -1.6, 1.6, 0.2));
		imaginaryMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setImaginaryMax(((Double)imaginaryMax.getValue()).floatValue());
			}
		});
		boundsPanel.add(imaginaryMax);
		
		JLabel iterationsLbl = new JLabel("No. Iterations");
		boundsPanel.add(iterationsLbl);
		JSpinner iterations = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 20));
		iterations.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setIterationDepth((int)iterations.getValue());
			}
		});
		boundsPanel.add(iterations);
		
		return boundsPanel;
	}
	
	private JPanel storageInit(){
		SetStoragePanel storagePanel = new SetStoragePanel(cpHeight-20, juliaViewer);
//		storagePanel
		return storagePanel;
	}
}
