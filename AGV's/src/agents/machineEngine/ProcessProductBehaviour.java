package agents.machineEngine;

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
		action();
	}
	
	@Override
	public void action() {
		System.out.println("action process product behaviour");
		Operation op = product.getCurrentOperation(); 
		
		while(machine.isOperationAvailable(product.getCurrentOperation())){
			block(op.getOperationDuration());
			op = product.nextOperation();
			//TODO delay for operation time and product modification
		}
		machine.setProductAtWork(product);
		finnished = true;
		
		//M2MCFP
	}

	@Override
	public boolean done() {
		return finnished;
	}
	
}
