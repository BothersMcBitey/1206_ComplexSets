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
		real = real * real - imaginary * imaginary;
		imaginary = 2 * (real * imaginary);
	}
	
	public float modulusSquared(){
		return real*real + imaginary*imaginary;
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
