package numbers;

public class MandelbrotSet implements ComplexSet {
	
	private int depth;
	
	public MandelbrotSet(int depth){
		setDepth(depth);
	}

	@Override
	public int getPointDivergenceDepth(ComplexNumber c) {
		ComplexNumber z = c.clone();
		int i = 0;
		float m = z.modulusSquared();
		while(m < 4 && i < depth){
			z.square();
			z.add(c);
			m = z.modulusSquared();
			i++;
		}
		return i;
	}

	public void setDepth(int depth) throws IllegalArgumentException{
		if(depth < 0) throw new IllegalArgumentException(depth + " is not a valid depth");
		this.depth = depth;
	}
	
	public int getDepth(){
		return depth;
	}
}