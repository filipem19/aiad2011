package products;

import java.util.LinkedList;

public class Product{

	private double currentWeight;
	private LinkedList<Operation> operations;
	
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
	
	/**
	 * Removes the completed operation and continue to the next operation 
	 * @return the operation to be realized at the product
	 */
	public Operation nextOperation(){
		if(operations.isEmpty())
			return null;
		this.operations.remove();
		return getCurrentOperation();
	}

	public boolean isComplete(){
		if(getCurrentOperation() == null){
			return true;
		}
		else{
			return false;
		}
	}
}
