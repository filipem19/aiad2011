package systemManagement;

import java.util.HashMap;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import products.Operation;

public class OperationList implements ListModel{

	private HashMap<String, Operation> availableOperations;

	public OperationList(HashMap<String, Operation> availableOperations) {
		this.availableOperations = availableOperations; 
	}
	
	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Operation getElementAt(int index) {
		return (Operation) availableOperations.values().toArray()[index];
	}

	@Override
	public int getSize() {
		return availableOperations.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
	}
	
}
