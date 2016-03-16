package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import complexMaths.ComplexNumber;
import complexMaths.ComplexSet;

@SuppressWarnings("serial")
public class ComplexSetViewerPanel extends JPanel implements MouseInputListener{

	private BufferedImage img;
	private ComplexSet set;
	
	private boolean dragging = false;
	private Point origin;
	private Point corner;
	
	private double realMin, realMax, imaginaryMin, imaginaryMax;
	
	private ExecutorService executor;
	
	/**
	 * Calls public ComplexSetViewerPanel(ComplexSet set, double realMin, double realMax, double imaginaryMin, double imaginaryMax)
	 * using the default values of -2, 2, -1.6, 1.6 for the mins/maxes
	 * @param set
	 */
	public ComplexSetViewerPanel(ComplexSet set){
		this(set, -2f, 2f, -1.6f, 1.6f);
	}
	
	public ComplexSetViewerPanel(ComplexSet set, double realMin, double realMax, double imaginaryMin, double imaginaryMax) {
		this.set = set;
		this.realMin = realMin;
		this.realMax = realMax;
		this.imaginaryMin = imaginaryMin;
		this.imaginaryMax = imaginaryMax;		
		addMouseListener(this);
		addMouseMotionListener(this);
		executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.clearRect(0, 0, getWidth(), getHeight());		
		 if(!dragging){
			 img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			 updateImage();
		 }
		g.drawImage(img, 0, 0, this);	
		
		if(dragging){
			g.setColor(Color.RED);
			int x = Math.min(corner.x, origin.x);
			int y = Math.min(corner.y, origin.y);
			int width = Math.abs(corner.x - origin.x);
			int height = Math.abs(corner.y - origin.y);
			g.drawRect(x, y, width, height);
		}
	}

	private void updateImage() {		
		double realWidth = realMax - realMin;
		
		double c = 255f/((double)set.getDepth());
		
		Future<?> f = null;
		
		for(int x = 0; x < img.getWidth(); x++){
			double offsetX = (realMin * img.getWidth()) / realWidth;
			double real = (x + offsetX) * (realWidth / (double)img.getWidth());
			for(int y = 0; y < img.getHeight(); y++){
				f = executor.submit(new ImageTask(x, y, real, c));
			}
		}
		
		try {
			f.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}	
	}
	

	
	private class ImageTask implements Runnable{
		
		private int x, y;
		private double real, c;
		
		public ImageTask(int x, int y, double real, double c) {
			this.x = x;
			this.y = y;
			this.real = real;
			this.c = c;
		}

		@Override
		public void run() {
			double imaginaryHeight = imaginaryMax - imaginaryMin;
			
			double offsetY = (imaginaryMin * img.getHeight()) / imaginaryHeight;
			double imaginary = ((y + offsetY)) * (imaginaryHeight / (double)img.getHeight());
			
			int n = set.getPointDivergenceDepth(new ComplexNumber(real, imaginary));
			int r = (int)(n*c);
			try{
				img.setRGB(x, (img.getHeight() - 1) - y, new Color(r,r,r).getRGB());
			}catch(ArrayIndexOutOfBoundsException e){
				System.err.println(e.getMessage() + " x:" + x + " y:" + y);
			}
		}
		
	}
	
	public void updateSet(ComplexSet set) {
		this.set = set;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				repaint();
			}
		});
	}
	
	public ComplexNumber getComplexAtPoint(Point p){				
		double realWidth = realMax - realMin;
		double imaginaryHeight = imaginaryMax - imaginaryMin;
		double real = p.x * (realWidth/(double)img.getWidth()) + realMin;
		double imaginary = (((img.getHeight() - 1) - p.y) * (imaginaryHeight/(double)img.getHeight())) + imaginaryMin;
		return new ComplexNumber(real, imaginary);
	}
	
	private void zoom(){
		ComplexNumber c1 = getComplexAtPoint(origin);
		ComplexNumber c2 = getComplexAtPoint(corner);
		
		setImaginaryMax(Math.max(c1.getImaginary(), c2.getImaginary()));
		System.out.println(getImaginaryMax());
		setImaginaryMin(Math.min(c1.getImaginary(), c2.getImaginary()));
		System.out.println(getImaginaryMin());
		setRealMax(Math.max(c1.getReal(), c2.getReal()));
		System.out.println(getRealMax());
		setRealMin(Math.min(c1.getReal(), c2.getReal()));
		System.out.println(getRealMin());	
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		dragging = true;
		corner = e.getPoint();
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		origin = e.getPoint();	
		corner = new Point(e.getX() + 1, e.getY() + 1);		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
		if(dragging){
			dragging = false;		
			zoom();
			repaint();
		}
	}

	public double getRealMin() {
		return realMin;
	}

	public void setRealMin(double realMin) throws IllegalArgumentException{
		if(realMin >= realMax) throw new IllegalArgumentException("realMin must be less than realMax");
		this.realMin = realMin;
	}

	public double getRealMax() {
		return realMax;
	}

	public void setRealMax(double realMax) throws IllegalArgumentException{
		if(realMax <= realMin) throw new IllegalArgumentException("realMax must be greater than realMin");
		this.realMax = realMax;
	}

	public double getImaginaryMin() {
		return imaginaryMin;
	}

	public void setImaginaryMin(double imaginaryMin) throws IllegalArgumentException{
		if(imaginaryMin >= imaginaryMax) throw new IllegalArgumentException("imaginaryMin must be less than imaginaryMax");
		this.imaginaryMin = imaginaryMin;
	}

	public double getImaginaryMax() {
		return imaginaryMax;
	}

	public void setImaginaryMax(double imaginaryMax) throws IllegalArgumentException{
		if(imaginaryMax <= imaginaryMin) throw new IllegalArgumentException("imaginaryMax must be greater than imaginaryMin");
		this.imaginaryMax = imaginaryMax;
	}
	
	public void setAxisBounds(double realMin, double realMax, double imaginaryMin, double imaginaryMax){
		setRealMin(realMin);
		setRealMax(realMax);
		setImaginaryMin(imaginaryMin);
		setImaginaryMax(imaginaryMax);
	}
	
	public void setIterationDepth(int depth){
		set.setDepth(depth);
	}

	public ComplexSet getSet() {
		return set;
	}
		
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
