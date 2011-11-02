package productEngine.product;

import jade.core.Agent;

public class Operation extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499081043636301580L;

	private int operationDuration;
	private double deltaWeight;
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
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
}
