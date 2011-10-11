package products;

import java.util.LinkedList;

import jade.core.Agent;
import jade.lang.acl.*;;

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
		ACLMessage m = new ACLMessage();
		m.setPerformative(ACLMessage.)
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
