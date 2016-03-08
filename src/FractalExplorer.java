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
		
		JLabel realMinLbl = new JLabel("Real min");
		controlPanel.add(realMinLbl);
		JSpinner realMin = new JSpinner(new SpinnerNumberModel(-2, -2, 1, 0.25));
		realMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setRealMin(((Double)realMin.getValue()).floatValue());
			}
		});
		controlPanel.add(realMin);
		
		JLabel realMaxLbl = new JLabel("Real max");
		controlPanel.add(realMaxLbl);
		JSpinner realMax = new JSpinner(new SpinnerNumberModel(1, -2, 1, 0.25));
		realMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setRealMax(((Double)realMax.getValue()).floatValue());
			}
		});
		controlPanel.add(realMax);
		
		JLabel imaginaryMinLbl = new JLabel("Imaginary min");
		controlPanel.add(imaginaryMinLbl);
		JSpinner imaginaryMin = new JSpinner(new SpinnerNumberModel(-1.6, -1.6, 1.6, 0.2));
		imaginaryMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setImaginaryMin(((Double)imaginaryMin.getValue()).floatValue());
			}
		});
		controlPanel.add(imaginaryMin);
		
		JLabel imaginaryMaxLbl = new JLabel("Imaginary max");
		controlPanel.add(imaginaryMaxLbl);
		JSpinner imaginaryMax = new JSpinner(new SpinnerNumberModel(1.6, -1.6, 1.6, 0.2));
		imaginaryMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setImaginaryMax(((Double)imaginaryMax.getValue()).floatValue());
			}
		});
		controlPanel.add(imaginaryMax);
		
		JLabel iterationsLbl = new JLabel("No. Iterations");
		controlPanel.add(iterationsLbl);
		JSpinner iterations = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 20));
		iterations.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setIterationDepth((int)iterations.getValue());
			}
		});
		controlPanel.add(iterations);
		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		controlPanel.add(exit);
		
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
