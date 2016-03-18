package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import complexMaths.ComplexNumber;
import complexMaths.ComplexSet;

@SuppressWarnings("serial")
public class ComplexSetViewerPanel extends JPanel implements MouseInputListener{

	private BufferedImage img;
	private ComplexSet set;
	private double realMin, realMax, imaginaryMin, imaginaryMax;
	
	private boolean dragging = false;
	private Point origin, corner;
	
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
	}
	
	@Override
	public void paintComponent(Graphics g){		
		//Updating the image while using the drag-zoom makes it run incredibly slowly, and the
		//image shouldn't be changing then anyway
		 if(!dragging){
			 //Make sure the image is the right size for the panel, in case the window was resized since
			 //the last repaint
			 img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			 updateImage();
		 }
		g.drawImage(img, 0, 0, this);	
		
		//draw the drag rectangle
		if(dragging){
			g.setColor(Color.RED);
			//This makes sure the rectangle doesn't have negative height and width values when the corner
			//is closer to (0,0) than the drag origin.
			int x = Math.min(corner.x, origin.x);
			int y = Math.min(corner.y, origin.y);
			int width = Math.abs(corner.x - origin.x);
			int height = Math.abs(corner.y - origin.y);
			g.drawRect(x, y, width, height);
		}
	}

	//Calculate the image for the visible part of the set
	private void updateImage() {		
		//The length of the visible section of the real axis
		double realWidth = realMax - realMin;
		//The length of the visible section of the imaginary axis
		double imaginaryHeight = imaginaryMax - imaginaryMin;
		
		//multiplied by the divergence depth of each point to keep the colour value <256
		double c = 255f/((double)set.getDepth());
		
		//for each column of pixels
		for(int x = 0; x < img.getWidth(); x++){
			//The amount by which x=0 (on screen) is offset from the imaginary axis
			double offsetX = (realMin * img.getWidth()) / realWidth;
			//the real value corresponding to that column
			double real = (x + offsetX) * (realWidth / (double)img.getWidth());
			//for each pixel in the column
			for(int y = 0; y < img.getHeight(); y++){
				//The amount by which y=0 (on screen) is offset from the real axis
				double offsetY = (imaginaryMin * img.getHeight()) / imaginaryHeight;
				//the imaginary value corresponding to this pixel
				double imaginary = ((y + offsetY)) * (imaginaryHeight / (double)img.getHeight());
				
				//get the number of iterations before this point diverges from the set
				int n = set.getPointDivergenceDepth(new ComplexNumber(real, imaginary));
				//get the colour value for this pixel
				int r = (int)(n*c);
				try{
					//set the pixels colour. The y value assignment is like that because 0 is
					//at the top of the screen, so the image need to be 'flipped' to render the set
					//in a way that makes sense to the user
					img.setRGB(x, (img.getHeight() - 1) - y, new Color(r,r,r).getRGB());
				}catch(ArrayIndexOutOfBoundsException e){
					//In case it tries to assign a value to a pixel not in the image
					System.err.println(e.getMessage() + " x:" + x + " y:" + y);
				}
			}
		}
	}
	
	public void updateSet(ComplexSet set) {
		this.set = set;
		repaint();
	}
	
	/* Performs the same operation as the code in updateImage.
	 * The reason updateImage doesn't call this method is that it would involve re-calculating the same real value up to 1500 times
	 * for each column of pixels, and as performance is important (especially at higher iteration depths) the code in updateImage
	 * has been changed to make it better optimised. Unfortunately, this means it can't be used to get one point at a time;
	 */
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
		
		//this bit makes sure the bounds aren't 'flipped', so the highest x value is always the max, etc.
		setImaginaryMax(Math.max(c1.getImaginary(), c2.getImaginary()));
		setImaginaryMin(Math.min(c1.getImaginary(), c2.getImaginary()));
		setRealMax(Math.max(c1.getReal(), c2.getReal()));
		setRealMin(Math.min(c1.getReal(), c2.getReal()));
	}
	
//Mouse Events -----------------------------------------------------------------------------------------------------------

	@Override
	public void mouseDragged(MouseEvent e) {
		dragging = true;
		corner = e.getPoint();
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		origin = e.getPoint();		
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
		//This check makes sure it doesn't zoom in when the user just clicks
		if(dragging){
			dragging = false;		
			zoom();
			repaint();
		}
	}
	
//Getters and Setters ------------------------------------------------------------------------------------------------------

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
	
//All of these are stubs from the mouse listener interface-------------------------------------------------------------
		
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
}
