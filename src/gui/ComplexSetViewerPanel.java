package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import numbers.ComplexNumber;
import numbers.ComplexSet;

@SuppressWarnings("serial")
public class ComplexSetViewerPanel extends JPanel {

	private BufferedImage img;
	private ComplexSet set;
	
	private float realMin, realMax, imaginaryMin, imaginaryMax;
	
	/**
	 * Calls public ComplexSetViewerPanel(ComplexSet set, float realMin, float realMax, float imaginaryMin, float imaginaryMax)
	 * using the default values of -2, 2, -1.6, 1.6 for the mins/maxes
	 * @param set
	 */
	public ComplexSetViewerPanel(ComplexSet set){
		this(set, -2f, 2f, -1.6f, 1.6f);
	}
	
	public ComplexSetViewerPanel(ComplexSet set, float realMin, float realMax, float imaginaryMin, float imaginaryMax) {
		this.set = set;
		this.realMin = realMin;
		this.realMax = realMax;
		this.imaginaryMin = imaginaryMin;
		this.imaginaryMax = imaginaryMax;		
	}
	
	@Override
	public void paintComponent(Graphics g){
		img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		updateImage();
		g.drawImage(img, 0, 0, this);
	}

	private void updateImage() {
		float realWidth = realMax - realMin;
		float imaginaryHeight = imaginaryMax - imaginaryMin;
		
		double c = 255f/((double)set.getDepth());
		
		for(int x = 0; x < img.getWidth(); x++){
			float offsetX = Math.abs((realMin * img.getWidth()) / realWidth);
			float real = (x - offsetX) * (realWidth / (float)img.getWidth());
			for(int y = 0; y < img.getHeight(); y++){
				float offsetY = Math.abs((imaginaryMin * img.getHeight()) / imaginaryHeight);
				float imaginary = (-(y - offsetY)) * (imaginaryHeight / (float)img.getHeight());
				
				int n = set.getPointDivergenceDepth(new ComplexNumber(real, imaginary));
				int rgb =0;
				int r = (int)(n*c);
				try{					
					rgb = new Color(r,r,r).getRGB();
				}
				catch (IllegalArgumentException e) {
					System.err.println(r);
				}
				try{
					img.setRGB(x, y, rgb);
				}catch(ArrayIndexOutOfBoundsException e){
					System.err.println(e.getMessage() + " x:" + x + " y:" + y);
				}				
			}
		}
	}
	
	public ComplexNumber getComplexAtPoint(Point p){				
		float realWidth = realMax - realMin;
		float imaginaryHeight = imaginaryMax - imaginaryMin;
		float real = p.x * (realWidth/(float)getWidth()) + realMin;
		float imaginary = -(p.y * (imaginaryHeight/(float)getHeight()) + imaginaryMin);
		return new ComplexNumber(real, imaginary);
	}

	public float getRealMin() {
		return realMin;
	}

	public void setRealMin(float realMin) throws IllegalArgumentException{
		if(realMin >= realMax) throw new IllegalArgumentException("realMin must be less than realMax");
		this.realMin = realMin;
		repaint();
	}

	public float getRealMax() {
		return realMax;
	}

	public void setRealMax(float realMax) throws IllegalArgumentException{
		if(realMax <= realMin) throw new IllegalArgumentException("realMax must be greater than realMin");
		this.realMax = realMax;
		repaint();
	}

	public float getImaginaryMin() {
		return imaginaryMin;
	}

	public void setImaginaryMin(float imaginaryMin) throws IllegalArgumentException{
		if(imaginaryMin >= imaginaryMax) throw new IllegalArgumentException("imaginaryMin must be less than imaginaryMax");
		this.imaginaryMin = imaginaryMin;
		repaint();
	}

	public float getImaginaryMax() {
		return imaginaryMax;
	}

	public void setImaginaryMax(float imaginaryMax) throws IllegalArgumentException{
		if(imaginaryMax <= imaginaryMin) throw new IllegalArgumentException("imaginaryMax must be greater than imaginaryMin");
		this.imaginaryMax = imaginaryMax;
		repaint();
	}
	
	public void setIterationDepth(int depth){
		set.setDepth(depth);
		repaint();
	}
	
}
