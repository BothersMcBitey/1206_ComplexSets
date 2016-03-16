import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import javax.swing.JSeparator;
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
	private SetStoragePanel storagePanel;
	
	private JLabel userPointLbl;
	private JPanel mainPanel;
	
	private JSpinner realMin;
	private JSpinner realMax;
	private JSpinner imaginaryMin;
	private JSpinner imaginaryMax;
	
	private int cpHeight = 60; 

	public static void main(String[] args) {
		try {;
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
		mandelbrotViewer = new ComplexSetViewerPanel(new MandelbrotSet(100), -2, 1, -1.6, 1.6);
		mandelbrotViewer.setBounds(0, 0, getWidth()/2, h);
		mandelbrotViewer.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e){
				userSelectedPoint = mandelbrotViewer.getComplexAtPoint(e.getPoint());
				userPointLbl.setText(userSelectedPoint.toString());
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
		
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		controlPanel.setBounds(0, mandelbrotViewer.getHeight(), getWidth(), cpHeight);
		
		controlPanel.add(boundsInit());
		
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(1, cpHeight - 20));
		controlPanel.add(sep);
		
		controlPanel.add(storageInit());
		
		JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
		sep1.setPreferredSize(new Dimension(1, cpHeight - 20));
		controlPanel.add(sep1);
		
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
		JPanel boundsPanel = new JPanel(new GridBagLayout());
		boundsPanel.setBounds(0, mandelbrotViewer.getHeight(), getWidth(), cpHeight);
		
		GridBagConstraints g = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		
		JLabel userPointCaptionLbl = new JLabel("Point selected: ");
		boundsPanel.add(userPointCaptionLbl, g);
		userPointLbl = new JLabel("0 + 0i");
		g.gridx = 1;
		boundsPanel.add(userPointLbl, g);
		
		JLabel iterationsLbl = new JLabel("No. Iterations");
		g.gridy = 1;
		g.gridx = 0;
		boundsPanel.add(iterationsLbl, g);
		JSpinner iterations = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 20));
		iterations.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setIterationDepth((int)iterations.getValue());
				mandelbrotViewer.repaint();
			}
		});
		g.gridx = 1;
		boundsPanel.add(iterations, g);
		
		JLabel realMinLbl = new JLabel("Real min");
		g.gridy = 0;
		g.gridx = 2;
		boundsPanel.add(realMinLbl, g);
		realMin = new JSpinner(new SpinnerNumberModel(-2, -2, 1, 0.25));
		realMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setRealMin((double) realMin.getValue());
				mandelbrotViewer.repaint();
			}
		});
		g.gridx = 3;
		boundsPanel.add(realMin, g);
		
		JLabel realMaxLbl = new JLabel("Real max");
		g.gridx = 2;
		g.gridy = 1;
		boundsPanel.add(realMaxLbl, g);	
		realMax = new JSpinner(new SpinnerNumberModel(1, -2, 1, 0.25));
		realMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setRealMax((double)realMax.getValue());
				mandelbrotViewer.repaint();
			}
		});
		g.gridx = 3;
		boundsPanel.add(realMax, g);
		
		JLabel imaginaryMinLbl = new JLabel("Imaginary min");
		g.gridx = 4;
		g.gridy = 0;
		boundsPanel.add(imaginaryMinLbl, g);
		imaginaryMin = new JSpinner(new SpinnerNumberModel(-1.6, -1.6, 1.6, 0.2));
		imaginaryMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setImaginaryMin((double)imaginaryMin.getValue());
				mandelbrotViewer.repaint();
			}
		});
		g.gridx = 5;
		boundsPanel.add(imaginaryMin, g);
		
		JLabel imaginaryMaxLbl = new JLabel("Imaginary max");
		g.gridx = 4;
		g.gridy = 1;
		boundsPanel.add(imaginaryMaxLbl, g);
		imaginaryMax = new JSpinner(new SpinnerNumberModel(1.6, -1.6, 1.6, 0.2));
		imaginaryMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mandelbrotViewer.setImaginaryMax((double)imaginaryMax.getValue());
				mandelbrotViewer.repaint();
			}
		});
		g.gridx = 5;
		boundsPanel.add(imaginaryMax, g);
		

		JButton resetMandlebrot = new JButton("Reset Mandlebrot Zoom");
		resetMandlebrot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mandelbrotViewer.setAxisBounds(-2, 1, -1.6, 1.6);
				mandelbrotViewer.repaint();
			}
		});
		g.gridx = 6;
		g.gridy = 0;
		boundsPanel.add(resetMandlebrot, g);
		
		JButton resetJulia = new JButton("Reset Julia Zoom");
		resetJulia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				juliaViewer.setAxisBounds(-2, 2, -1.6, 1.6);
				juliaViewer.repaint();
			}
		});
		g.gridy = 1;
		boundsPanel.add(resetJulia, g);
		
		return boundsPanel;
	}
	
	private JPanel storageInit(){
		storagePanel = new SetStoragePanel(cpHeight, juliaViewer);
		return storagePanel;
	}
}
