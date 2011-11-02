package negotiationEngine;

import products.Product;
import agents.Machine;

public class AgvCFP {

	private Machine origin, destination;
	private Product product;
	
	public AgvCFP(Machine origin, Machine destination, Product product) {
		this.origin = origin;
		this.destination = destination;
		this.product = product;
	}
	
	public Machine getOrigin() {
		return origin;
	}
	
	public Machine getDestination() {
		return destination;
	}
	
	public Product getProduct() {
		return product;
	}
	
	
}
