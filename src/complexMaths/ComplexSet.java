package complexMaths;

import java.io.Serializable;

public interface ComplexSet extends Serializable{
	
	public int getPointDivergenceDepth(ComplexNumber c);
	
	public int getDepth();
	
	public void setDepth(int depth);
}
