package agents;

import jade.core.behaviours.SimpleBehaviour;
import products.Operation;
import products.Product;

public class ProcessProductBehaviour extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8171703463270555600L;
	
	private Product product;
	private Machine machine;
	private boolean finnished = false;
	
	public ProcessProductBehaviour(Product product) {
		this.product = product;
		this.machine = (Machine) myAgent;
	}
	
	@Override
	public void action() {
		Operation op = product.getCurrentOperation(); 
		while(machine.isOperationAvailable(product.getCurrentOperation())){
			block(op.getOperationDuration());
			op = product.nextOperation();
		}
		machine.setProductAtWork(product);
		finnished = true;
	}

	@Override
	public boolean done() {
		return finnished;
	}
	
}
