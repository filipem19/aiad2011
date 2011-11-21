package products;

import java.io.Serializable;
import java.util.LinkedList;

public class Product implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4934668319850858658L;
	
	private String productName;
	private double currentWeight;
	private LinkedList<Operation> operations;

	public Product(double currentWeight, String productName, LinkedList<Operation> operations) {
		this.productName = productName;
		this.currentWeight = currentWeight;
		this.operations = operations;
//		System.out.println("\nCreated => " + toString());
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
	
	public String getProductName(){
		return this.productName;
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
	
	@Override
	public String toString() {
		return "Product(" + getProductName() + ":" +
				"\tcurrentWeight = " + currentWeight + 
				"\toperations = " + operations;
	}
}
