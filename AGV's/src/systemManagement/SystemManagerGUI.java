package systemManagement;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;

import products.Operation;
import products.Product;

import agents.Machine;

public class SystemManagerGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6978147188402748454L;

	private Vector<Machine> machines;
	private Vector<Product> products;
	private Vector<Operation> operations;
	
	public enum ObjectType{Machine, Operation, Product};
	
	public SystemManagerGUI() {
		//initializeWindowPreferences();
	}
	

	private void initializeWindowPreferences(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Initial platform");
		setVisible(true);
		setSize(200, 200);
		setLocation(200, 400);
		JButton button = new JButton("Adicionar uma cena");
		add(button);
	}
}
