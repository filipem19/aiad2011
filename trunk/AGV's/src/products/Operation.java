package products;

import java.io.Serializable;

public class Operation implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6249120443607970589L;
	private String name;
	private int duration;
	private double deltaWeight;

	public Operation(int operationDuration, double deltaWeight, String operationName) {
		this.name = operationName;
		this.duration = operationDuration;
		this.deltaWeight = deltaWeight;
//		System.out.println("\nCreated => " + toString());
	}
	
	public String getOperationName(){
		return this.name;
	}
	
	public int getOperationDuration() {
		return duration;
	}

	public void setOperationDuration(int operationDuration) {
		this.duration = operationDuration;
	}

	public double getDeltaWeight() {
		return deltaWeight;
	}

	public void setDeltaWeight(double deltaWeight) {
		this.deltaWeight = deltaWeight;
	}
	
	@Override
	public String toString() {
		return "Operation (" + name + "):\n\tduration = " + duration + "\n\tdeltaweight = " + deltaWeight;
	}
}
