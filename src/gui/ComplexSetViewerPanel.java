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
		float realStep = realWidth/((float) img.getWidth());
		float imaginaryHeight = imaginaryMax - imaginaryMin;
		float imaginaryStep = imaginaryHeight/((float) img.getWidth());		
		for(float i = realMin; i < realMax; i += realStep){
			for(float j = imaginaryMin; j < imaginaryMax; j+= imaginaryStep){
				int n = set.getDepth() - set.getPointDivergenceDepth(new ComplexNumber(i, j));
				int x = (int) Math.floor((i - realMin) * (((float)img.getWidth()) / realWidth));				
				int y = (int) Math.floor((j - imaginaryMin) * (((float)img.getHeight()) / imaginaryHeight));
				int c = 255/set.getDepth();
				int rgb = new Color((set.getDepth()-n)*c,(set.getDepth()-n)*c,(set.getDepth()-n)*c).getRGB();
				try{
				img.setRGB(x, y, rgb);
				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println(e.getMessage() + " x:" + x + " y:" + y);
				}				
			}
		}
	}
	
	public ComplexNumber getComplexAtPoint(Point p){				
		float realWidth = realMax - realMin;
		float imaginaryHeight = imaginaryMax - imaginaryMin;
		float real = p.x * (realWidth/(float)getWidth()) + realMin;
		float imaginary = p.y * (imaginaryHeight/(float)getHeight()) + imaginaryMin;
		return new ComplexNumber(real, imaginary);
	}

	public float getRealMin() {
		return realMin;
	}

	public void setRealMin(float realMin) throws IllegalArgumentException{
		if(realMin >= realMax) throw new IllegalArgumentException("realMin must be less than realMax");
		this.realMin = realMin;
	}

	public float getRealMax() {
		return realMax;
	}

	public void setRealMax(float realMax) throws IllegalArgumentException{
		if(realMax <= realMin) throw new IllegalArgumentException("realMax must be greater than realMin");
		this.realMax = realMax;
	}

	public float getImaginaryMin() {
		return imaginaryMin;
	}

	public void setImaginaryMin(float imaginaryMin) throws IllegalArgumentException{
		if(imaginaryMin >= imaginaryMax) throw new IllegalArgumentException("imaginaryMin must be less than imaginaryMax");
		this.imaginaryMin = imaginaryMin;
	}

	public float getImaginaryMax() {
		return imaginaryMax;
	}

	public void setImaginaryMax(float imaginaryMax) throws IllegalArgumentException{
		if(imaginaryMax <= imaginaryMin) throw new IllegalArgumentException("imaginaryMax must be greater than imaginaryMin");
		this.imaginaryMax = imaginaryMax;
	}
	
}
