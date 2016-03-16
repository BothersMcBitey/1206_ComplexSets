package complexMaths;

import java.io.Serializable;

public class ComplexNumber implements Serializable {

	private static final long serialVersionUID = 428336478432597116L;
	private double real;
	private double imaginary;
	
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public void add(ComplexNumber d){
		this.real += d.getReal();
		this.imaginary += d.getImaginary();
	}
	
	public void square(){
		double tempReal = real * real - imaginary * imaginary;
		imaginary = 2 * real * imaginary;
		real = tempReal;
	}
	
	public double modulusSquared(){
		return real*real + imaginary*imaginary;
	}
	
	public ComplexNumber clone(){
		return new ComplexNumber(real, imaginary);
	}
	
	public String toString(){
		String s = String.format("%.5f", real);
		s += (imaginary >= 0) ? " + " : " - "; 
		s += String.format("%.5f", Math.abs(imaginary)) + "i";
		return s;
	}

	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public double getImaginary() {
		return imaginary;
	}

	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}
}
