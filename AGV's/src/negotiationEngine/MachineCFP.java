package negotiationEngine;

import java.io.Serializable;

import products.Product;

public class MachineCFP implements Serializable{

	private static final long serialVersionUID = -2312982920303681093L;

	private Product product;
	
	public MachineCFP(Product product) {
		// TODO Auto-generated constructor stub
		this.product = product;
	}
	
	public Product getProduct() {
		return product;
	}

}
