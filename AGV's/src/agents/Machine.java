package agents;

import jade.core.Agent;

import java.util.Enumeration;
import java.util.Vector;

import products.Operation;
import products.Product;

public class Machine extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1033896316722663016L;
	
	private int locationX;
	private int locationY;
	private String machineName;
	private Product productAtWork;
	private Vector<Operation> availableOperations; //Contains all the operations that this machine is able to perform
	
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
	}

	public boolean isOperationAvailable(Operation oper) {
		return availableOperations.contains(oper);
	}
	
	public Enumeration<Operation> getAvailableOperations(){
		return availableOperations.elements();
	}
	
	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String name) {
		this.machineName = name;
	}

	public Product getProductAtWork() {
		return productAtWork;
	}

	public void setProductAtWork(Product productAtWork) {
		this.productAtWork = productAtWork;
	}
	
}
