package numbers;

public class ComplexNumber {

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
		return String.format("%.2f", real) + " + " + String.format("%.2f", imaginary) + "i";
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
