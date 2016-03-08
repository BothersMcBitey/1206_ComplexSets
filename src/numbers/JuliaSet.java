package numbers;

public class JuliaSet implements ComplexSet {

	private int depth;
	private ComplexNumber c;
	
	public JuliaSet(int depth, ComplexNumber c) {
		this.depth = depth;
		this.c = c;
	}
	
	@Override
	public int getPointDivergenceDepth(ComplexNumber z) {
		int i = 0;
		while(z.modulusSquared() < 4 && i < depth){
			z.square();
			z.add(c);
			i++;
		}
		return i;
	}
	
	@Override
	public void setDepth(int depth) throws IllegalArgumentException{
		if(depth < 0) throw new IllegalArgumentException(depth + " is not a valid depth");
		this.depth = depth;
	}

	@Override
	public int getDepth() {
		return depth;
	}

}
