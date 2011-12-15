package systemManagement.GUI;

import java.util.HashMap;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import products.Operation;
import products.Product;

public class ProductList implements ListModel{

	private HashMap<String, Product> availableProducts;

	public ProductList(HashMap<String, Product> availableOperations) {
		this.availableProducts = availableOperations; 
	}
	
	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Product getElementAt(int index) {
		return (Product) availableProducts.values().toArray()[index];
	}

	@Override
	public int getSize() {
		return availableProducts.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
	}
	
}
