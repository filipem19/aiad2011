package systemManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
		setSize(500, 500);
		setLocation(100, 100);
		
		//---Pagina Principal---
		
		//Buttons
		JButton button1 = new JButton("Adicionar Máquina");
		button1.addActionListener(this);
		button1.setActionCommand("AddMaq");
		button1.setSize(100, 50);
		
		JButton button2 = new JButton("Adicionar AGV");
		button2.addActionListener(this);
		button2.setActionCommand("AddAGV");		
		button2.setSize(100, 50);
		
		JButton button3 = new JButton("Remover Máquina");
		button3.addActionListener(this);
		button3.setActionCommand("DelMaq");		
		button3.setSize(100, 50);
		
		JButton button4 = new JButton("Remover AGV");
		button4.addActionListener(this);
		button4.setActionCommand("DelAGV");		
		button4.setSize(100, 50);
				
		//JPanels
		JPanel mapaOficina = new JPanel();
		mapaOficina.setBackground(Color.ORANGE);
		mapaOficina.setSize(400, 500);
		
		//Box
		Box botoes = Box.createVerticalBox();
		botoes.add(button1);
		botoes.add(button2);
		botoes.add(button3);
		botoes.add(button4);
		botoes.setSize(100, 500);
		
		//Container
		Container c = getContentPane();
		c.add(botoes, BorderLayout.EAST);
		c.add(mapaOficina, BorderLayout.WEST);  
	    		
		this.add(c);
		//---   ---
				
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//sysManager.registerExistingAgents(get);
		
		//accao a executar quando pressionados os botoes
		if ("AddMaq".equals(e.getActionCommand())) {
			System.out.println("AddMaq");
			
		} else if ("AddAGV".equals(e.getActionCommand())) {
			System.out.println("AddAGV");
			
		} else if ("DelMaq".equals(e.getActionCommand())) {
			System.out.println("DelMaq");
			
		} else if ("DelAGV".equals(e.getActionCommand())) {
			System.out.println("DelAGV");
			
		}
	}
}
