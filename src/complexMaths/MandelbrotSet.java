package complexMaths;

public class MandelbrotSet extends ComplexSet {
		
	private static final long serialVersionUID = 1847224292970379633L;

	public MandelbrotSet(int depth){
		super(depth, "mandelbrotSet");
	}

	@Override
	public int getPointDivergenceDepth(ComplexNumber c) {
		ComplexNumber z = c.clone();
		int i = 0;
		while(z.modulusSquared() < 4 && i < getDepth()){
			z.square();
			z.add(c);
			i++;
		}
		return i;
	}
}
