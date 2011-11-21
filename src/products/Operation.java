package products;

public class Operation  {

	private String operationName;
	private int operationDuration;
	private double deltaWeight;

	public Operation(int operationDuration, double deltaWeight, String operationName) {
		this.operationName = operationName;
		this.operationDuration = operationDuration;
		this.deltaWeight = deltaWeight;
		System.out.println("\nCreated => " + toString());
	}
	
	public String getOperationName(){
		return this.operationName;
	}
	
	public int getOperationDuration() {
		return operationDuration;
	}

	public void setOperationDuration(int operationDuration) {
		this.operationDuration = operationDuration;
	}

	public double getDeltaWeight() {
		return deltaWeight;
	}

	public void setDeltaWeight(double deltaWeight) {
		this.deltaWeight = deltaWeight;
	}
	
	@Override
	public String toString() {
		return "Operation (" + operationName + "):\n\tduration = " + operationDuration + "\n\tdeltaweight = " + deltaWeight;
	}
}
