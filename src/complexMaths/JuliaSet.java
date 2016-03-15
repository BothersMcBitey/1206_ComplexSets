package complexMaths;

public class JuliaSet extends ComplexSet{

	private static final long serialVersionUID = 8053747924166912758L;
	private ComplexNumber c;
	
	public JuliaSet(int depth, ComplexNumber c) {
		super(depth, "juliaSet");
		this.c = c;		
	}
	
	@Override
	public int getPointDivergenceDepth(ComplexNumber z) {
		int i = 0;
		while(z.modulusSquared() < 4 && i < getDepth()){
			z.square();
			z.add(c);
			i++;
		}
		return i;
	}

}
