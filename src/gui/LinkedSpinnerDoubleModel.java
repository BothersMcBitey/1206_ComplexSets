package gui;

import javax.swing.SpinnerNumberModel;

/*
 * I'm aware that it would be better to write the linking code in a super class that can handle
 * Any number, not just doubles, but this was a bit of a last minute addition.
 */
@SuppressWarnings("serial")
public class LinkedSpinnerDoubleModel extends SpinnerNumberModel {

	private LinkedSpinnerDoubleModel linkedModel = null;
	private boolean isGreater;
	
	public LinkedSpinnerDoubleModel(double value, double minimum, double maximum, double stepSize) {
		super(value, minimum, maximum, stepSize);
	}
	
	/**
	 * Sets the spinnerModel this one should be linked to, and whether or not this is greater than it.
	 * The values of the two models will also never be equal.
	 * When this model is called on one model it automatically calls it on the linkedModel to ensure 
	 * both are always linked properly.
	 * 
	 * @param linkedModel
	 * @param isGreater - if true, this model should always be greater than linkedModel. If false, the opposite is true
	 */
	public void setLinkedModel(LinkedSpinnerDoubleModel linkedModel, boolean isGreater){
		this.linkedModel = linkedModel;
		this.isGreater = isGreater;
		linkedModel.setOtherLinkedModel(this, !isGreater);
	}
	
	/**
	 * This method acts almost the same as setLinkedModel, but doesn't make another method call. This is
	 * to prevent an infinite loop of recursive calls.
	 * @param linkedModel
	 * @param isGreater
	 */
	private void setOtherLinkedModel(LinkedSpinnerDoubleModel linkedModel, boolean isGreater){
		this.linkedModel = linkedModel;
		this.isGreater = isGreater;
	}
	
	
	@Override
	public void setValue(Object value) throws IllegalArgumentException{
		if(value == null || !(value instanceof Number)) throw new IllegalArgumentException();
		
		double doubleVal = (double) value;
		double linkedVal = (double) linkedModel.getValue();
		
		if(isGreater){
			if(doubleVal <= linkedVal) throw new IllegalArgumentException();
		} else {
			if(doubleVal >= linkedVal) throw new IllegalArgumentException();
		}
		
		super.setValue(value);
	}
}
