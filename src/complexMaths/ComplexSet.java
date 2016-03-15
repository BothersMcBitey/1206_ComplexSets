package complexMaths;

import java.io.Serializable;

public abstract class ComplexSet implements Serializable{
	
	private static final long serialVersionUID = 4109157370589382533L;
	private int depth;
	private String name;
	
	public ComplexSet(int depth, String name) {
		setDepth(depth);
		setName("set");
	}
	
	public abstract int getPointDivergenceDepth(ComplexNumber c);
	
	public void setDepth(int depth) throws IllegalArgumentException{
		if(depth < 0) throw new IllegalArgumentException(depth + " is not a valid depth");
		this.depth = depth;
	}
	
	public int getDepth(){
		return depth;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public String toString(){		
		return name;
	}
}
