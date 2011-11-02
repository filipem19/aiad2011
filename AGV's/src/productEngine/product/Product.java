package productEngine.product;

import jade.core.Agent;

import java.util.LinkedList;


public class Product extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -858911517993953638L;

	private double currentWeight;
	private LinkedList<Operation> operations;

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
	}

	public double getCurrentWeight() {
		return currentWeight;
	}

	public void setCurrentWeight(double currentWeight) {
		this.currentWeight = currentWeight;
	}

	public Operation getCurrentOperation() {
		if(operations.isEmpty())
			return null;
		return operations.element();
	}
	
	//removes the completed operation and continue to the next operation 
	public Operation nextOperation(){
		if(operations.isEmpty())
			return null;
		this.operations.remove();
		return getCurrentOperation();
	}

}
