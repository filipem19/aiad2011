package systemManagement;

import jade.domain.FIPAAgentManagement.DFAgentDescription;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class SystemManagerGUI extends JFrame implements ActionListener,
		PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6978147188402748454L;

	private SystemManager sysManager;

	public SystemManagerGUI(SystemManager sysManager) {
		this.sysManager = sysManager;
		initializeWindowPreferences();
	}

	
	private void initializeWindowPreferences() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Initial platform");
		setVisible(true);
		setSize(300, 400);
		setLocation(0, 0);

		JPanel p = new JPanel(), p2 = new JPanel();
		p.setLayout(new GridLayout(1, 1));
		p2.setLayout(new GridLayout(1, 1));
		
		Vector<String> vec = new Vector<String>();
		
		DFAgentDescription[] agentList = sysManager.getAgentListWithService("ProcessProduct");
		for(DFAgentDescription df : agentList){
//			System.out.println(df.getName().getLocalName());
			vec.add(df.getName().getLocalName());
		}
		
		p.add(createList(vec));
		
		
		p2.add(createList(new Vector<String>(sysManager.getExistingOperations().keySet())));
		p2.add(createList(new Vector<String>(sysManager.getExistingProducts().keySet())));
		add(p);
		p.add(p2);
		
		repaint();
	}

	private JList createList (Vector<String> elements){
		JList acList = new JList();
		
		Vector<String> vec = new Vector<String>();
		
		for(String element : elements)
			vec.add(element);
		
		acList.setListData(vec);
		
		acList.setVisibleRowCount(5);
		acList.setFixedCellHeight(18);
		acList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		return acList;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("old Value: " + evt.getOldValue().toString());
		System.out.println("new Value: " + evt.getNewValue().toString());
	}
}
