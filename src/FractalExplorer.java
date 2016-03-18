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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("MandelbrotSet.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.invokeAndWait(new Runnable(){ public void run(){ new FractalExplorer().init();}});
		} catch (InvocationTargetException | InterruptedException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public FractalExplorer() {
		super("Fractal Explorer");
	}
	
	public void init(){
		//makes sure the program is 'fullscreened' and components are the correct size
		setSize(Toolkit.getDefaultToolkit().getScreenSize());	
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		//TODO: replace null layout with gridbag?
		mainPanel = new JPanel(null);
		setContentPane(mainPanel);
		
		setsInit();	
		controlsInit();
			
		setVisible(true);
		//Because of the way the rendering is done, resizing can cause serious issues
		setResizable(false);
	}
	
	private void setsInit(){
		//Makes sure there is enough room for the controls beneath the set panels
		int h = getHeight() - ((getHeight()/10 < cpHeight) ? cpHeight : getHeight()/10);
		//creates mandlebrot set viewer with slightly non-standard sizing, as having realMAx at 1 looks better
		mandelbrotViewer = new ComplexSetViewerPanel(new MandelbrotSet(100), -2, 1, -1.6, 1.6);
		mandelbrotViewer.setBounds(0, 0, getWidth()/2, h);
		
		//This listener updates the julia set viewer as the mouse moves, just switch 'mouseMoved' for 'mouseClicked'
		//and change the method to 'addMouseListener' to make it work on click instead
		mandelbrotViewer.addMouseMotionListener(new MouseAdapter() {
			//TODO:make this run in a different thread
			public void mouseMoved(MouseEvent e){
				//update the current point
				userSelectedPoint = mandelbrotViewer.getComplexAtPoint(e.getPoint());
				userPointLbl.setText(userSelectedPoint.toString());
				//update the julia set
				juliaViewer.updateSet(new JuliaSet(200, userSelectedPoint));
			}
		});	
		mainPanel.add(mandelbrotViewer);
		
		//Set initial values for selected point and julia set
		userSelectedPoint = new ComplexNumber(0, 0);
		juliaViewer = new ComplexSetViewerPanel(new JuliaSet(100, userSelectedPoint));
		juliaViewer.setBounds(getWidth()/2, 0, getWidth()/2, mandelbrotViewer.getHeight());
		mainPanel.add(juliaViewer);
	}
	
	private void controlsInit(){
		//make sure controls take up the right amount of space
		cpHeight = (getHeight()/10 < cpHeight) ? cpHeight : getHeight()/10;
		
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		controlPanel.setBounds(0, mandelbrotViewer.getHeight(), getWidth(), cpHeight);
		
		//add the Mandelbrot set bounds controls
		controlPanel.add(boundsInit());
		
		//add separator to make UI easier to understand
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(1, cpHeight - 20));
		controlPanel.add(sep);
		
		//add Julia set storage controls
		controlPanel.add(storageInit());
		
		//another separator
		JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
		sep1.setPreferredSize(new Dimension(1, cpHeight - 20));
		controlPanel.add(sep1);
		
		//add exit button
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
		
		//constraints made with 5px insets so components are nicely spaced
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);
		
		//add user selected point labels
		JLabel userPointCaptionLbl = new JLabel("Point selected: ");
		boundsPanel.add(userPointCaptionLbl, c);
		userPointLbl = new JLabel("0 + 0i");
		c.gridx = 1;
		boundsPanel.add(userPointLbl, c);
		
		//add iteration controls
		JLabel iterationsLbl = new JLabel("No. Iterations");
		c.gridy = 1;
		c.gridx = 0;
		boundsPanel.add(iterationsLbl, c);
		JSpinner iterations = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 20));
		c.gridx = 1;
		boundsPanel.add(iterations, c);
		
		//ADD MANDLEBROT MIN/MAX CONTROLS----------------------------------------------------------		
		//RMin label
		JLabel realMinLbl = new JLabel("Real min");
		c.gridy = 0;
		c.gridx = 2;
		boundsPanel.add(realMinLbl, c);
		//RMin spinner
		LinkedSpinnerDoubleModel rMinModel = new LinkedSpinnerDoubleModel(-2, -2, 1, 0.25);
		realMin = new JSpinner(rMinModel);
		c.gridx = 3;
		boundsPanel.add(realMin, c);
		
		//RMax label
		JLabel realMaxLbl = new JLabel("Real max");
		c.gridx = 2;
		c.gridy = 1;
		boundsPanel.add(realMaxLbl, c);
		//RMax spinner
		LinkedSpinnerDoubleModel rMaxModel = new LinkedSpinnerDoubleModel(1, -2, 1, 0.25);
		rMaxModel.setLinkedModel(rMinModel, true);
		realMax = new JSpinner(rMaxModel);
		c.gridx = 3;
		boundsPanel.add(realMax, c);
		
		//IMin label
		JLabel imaginaryMinLbl = new JLabel("Imaginary min");
		c.gridx = 4;
		c.gridy = 0;
		boundsPanel.add(imaginaryMinLbl, c);
		//IMin spinner
		LinkedSpinnerDoubleModel iMinModel = new LinkedSpinnerDoubleModel(-1.6, -1.6, 1.6, 0.2);
		imaginaryMin = new JSpinner(iMinModel);
		c.gridx = 5;
		boundsPanel.add(imaginaryMin, c);
		
		//IMax label
		JLabel imaginaryMaxLbl = new JLabel("Imaginary max");
		c.gridx = 4;
		c.gridy = 1;
		boundsPanel.add(imaginaryMaxLbl, c);
		//IMax spinner
		LinkedSpinnerDoubleModel iMaxModel = new LinkedSpinnerDoubleModel(1.6, -1.6, 1.6, 0.2);
		iMaxModel.setLinkedModel(iMinModel, true);
		imaginaryMax = new JSpinner(iMaxModel);
		c.gridx = 5;
		boundsPanel.add(imaginaryMax, c);
		
		//Mandelbrot update button
		JButton updateMandelbrot = new JButton("Update Mandelbrot Set");
		updateMandelbrot.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				mandelbrotViewer.setIterationDepth((int)iterations.getValue()); 
				mandelbrotViewer.setAxisBounds((double)realMin.getValue(), (double)realMax.getValue(), (double)imaginaryMin.getValue(), (double)imaginaryMax.getValue());
				mandelbrotViewer.repaint();
			}
		});
		c.gridx = 6;
		c.gridy = 0;
		c.gridheight = 2;
		boundsPanel.add(updateMandelbrot, c);

		//Mandelbrot reset button
		JButton resetMandelbrot = new JButton("Reset Mandelbrot Zoom");
		resetMandelbrot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mandelbrotViewer.setAxisBounds(-2, 1, -1.6, 1.6);
				mandelbrotViewer.repaint();
			}
		});
		c.gridx = 7;
		c.gridy = 0;
		c.gridheight = 1;
		boundsPanel.add(resetMandelbrot, c);
		
		//Julia reset button
		JButton resetJulia = new JButton("Reset Julia Zoom");
		resetJulia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				juliaViewer.setAxisBounds(-2, 2, -1.6, 1.6);
				juliaViewer.repaint();
			}
		});
		c.gridy = 1;
		boundsPanel.add(resetJulia, c);
		
		return boundsPanel;
	}
	
	private JPanel storageInit(){
		storagePanel = new SetStoragePanel(cpHeight, juliaViewer);
		return storagePanel;
	}
}
