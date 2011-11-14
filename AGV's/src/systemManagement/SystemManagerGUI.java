package systemManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;

import products.Operation;
import products.Product;
import agents.machineEngine.Machine;


public class SystemManagerGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6978147188402748454L;

	private Vector<Machine> machines;
	private Vector<Product> products;
	private Vector<Operation> operations;
	private SystemManager sysManager;
	
	public SystemManagerGUI(SystemManager sysManager) {
		this.sysManager = sysManager;
		initializeWindowPreferences();
	}
	

	private void initializeWindowPreferences(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Initial platform");
		setVisible(true);
		setSize(200, 200);
		setLocation(200, 400);
		JButton button = new JButton("Adicionar uma cena");
		button.addActionListener(this);
		add(button);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
//		sysManager.registerExistingAgents(get);
	}
}
