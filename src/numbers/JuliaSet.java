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
		float m = z.modulusSquared();
		while(m < 4 && i < depth){
			z.square();
			z.add(c);
			m = z.modulusSquared();
			i++;
		}
		return i;
	}

	@Override
	public int getDepth() {
		return depth;
	}

}
