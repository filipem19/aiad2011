package systemManagement.GUI;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import systemManagement.SystemManager;

public class SystemManagerControlsGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3421461888810613018L;

	private SystemManager sysManager;
	private JList machineList, productList;
	private JTextField message = new JTextField();
	
	public SystemManagerControlsGUI(SystemManager sysManager) {
		super("Controls");
		setVisible(true);
		this.sysManager = sysManager;
		setLayout(new GridBagLayout());
		initializeInterfaceComponents();
		this.setSize(new Dimension(350, 350));
	}
	
	private void initializeInterfaceComponents() {
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.gridx = 0;
		constraint.gridy = 0;
		getContentPane().add(machineListComponent(), constraint);
		constraint.gridx = 1;
		constraint.gridy = 0;
		getContentPane().add(productListComponent(), constraint);
		constraint.gridy = 3;
		JButton button = new JButton("Adicionar Produto a máquina");
		button.addActionListener(this);
		button.setActionCommand("addProductToMachine");
		getContentPane().add(button, constraint);
		constraint.gridy = 4;
		message.setEditable(false);
		message.setText("Mensagem:");
		getContentPane().add(message, constraint);
	}

	private Component productListComponent() {
		JPanel productListPanel = new JPanel();
		productList = new JList(new ProductList(sysManager.getExistingProducts()));
		productListPanel.add(productList);
		return productListPanel;
	}

	private Component machineListComponent() {
		JPanel machineListPanel = new JPanel();
		machineList = new JList(new MachineListData(sysManager.getAgentListWithService("ProcessProduct")));
		machineListPanel.add(machineList);
		return machineListPanel;
	}

	public void setMessageFieldContent (String content){
		message.setText(content);
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("addProductToMachine")){
			DFAgentDescription[] agents = sysManager.getAgentListWithService("ProcessProduct");
			AID receiver = null;
			for(DFAgentDescription agent : agents){
				if(((String)machineList.getSelectedValue()).compareTo(agent.getName().getLocalName()) == 0){
					receiver = agent.getName();
				}
			}
			
			if(receiver != null && productList.getSelectedValue() != null){
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
				msg.setContent(receiver.getLocalName() + " " + (String)productList.getSelectedValue().toString());
				msg.addReceiver(sysManager.getAID());
				sysManager.send(msg);
			}
		}
	}

}
