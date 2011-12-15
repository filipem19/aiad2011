package systemManagement.GUI;

import java.util.Vector;

import jade.domain.FIPAAgentManagement.DFAgentDescription;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class MachineListData implements ListModel{

	private Vector<String> machineList;
	
	public MachineListData(DFAgentDescription[] machines) {
		super();
		machineList = new Vector<String>();
		for(DFAgentDescription agent : machines){
			machineList.add(agent.getName().getLocalName());
		}
	}
	
	@Override
	public void addListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return machineList.get(index);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return machineList.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}

}
