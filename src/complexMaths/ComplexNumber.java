package complexMaths;

import java.io.Serializable;

public class ComplexNumber implements Serializable {

	private float real;
	private float imaginary;
	
	public ComplexNumber(float real, float imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public void add(ComplexNumber d){
		this.real += d.getReal();
		this.imaginary += d.getImaginary();
	}
	
	public void square(){
		float tempReal = real * real - imaginary * imaginary;
		imaginary = 2 * real * imaginary;
		real = tempReal;
	}
	
	public float modulusSquared(){
		return real*real + imaginary*imaginary;
	}
	
	public ComplexNumber clone(){
		return new ComplexNumber(real, imaginary);
	}
	
	public String toString(){
		String s = String.format("%.2f", real);
		s += (imaginary >= 0) ? " + " : " - "; 
		s += String.format("%.2f", Math.abs(imaginary)) + "i";
		return s;
	}

	public float getReal() {
		return real;
	}

	public void setReal(float real) {
		this.real = real;
	}

	public float getImaginary() {
		return imaginary;
	}

	public void setImaginary(float imaginary) {
		this.imaginary = imaginary;
	}
}
