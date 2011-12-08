package systemManagement;
      
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

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
	
	private String[] listaAGV;
	private JComboBox comboAGV;
	private String[] listaMaq;
	private JComboBox comboMaq;
	
	private String[] listaEstados = {"Good"}; //--estados possiveis
	
	private JTextField nomeAGVText = new JTextField();
	private JTextField autonomiaAGVText = new JTextField();
	private JTextField custoAGVText = new JTextField();
	private JTextField xAGVText = new JTextField();
	private JTextField yAGVText = new JTextField();
	private JTextField velocidadeAGVText = new JTextField();
	private JTextField cargaMaxAGVText = new JTextField();
	private JComboBox estadosAGV = new JComboBox(listaEstados);
	
	JTextField nomeMaqText = new JTextField();
	JTextField xMaqText = new JTextField();
	JTextField yMaqText = new JTextField();
	JTextField OperacoesText = new JTextField();
	
	public SystemManagerGUI(SystemManager sysManager) {
		this.sysManager = sysManager;
		initializeWindowPreferences();
	}
	
	private void initializeWindowPreferences(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Initial platform");
		setVisible(true);
		setSize(500, 800);
		setLocation(900, 50);
		
		//---Pagina Principal---
		
		//Mapa Oficina
		JPanel mapaOficina = new JPanel();
		mapaOficina.setBackground(Color.ORANGE);
		mapaOficina.setSize(400, 500);
		
		//Opcoes de Adicionar e Remover
		JPanel opcoes = new JPanel(new BorderLayout());
		JPanel adicionar = new JPanel(new BorderLayout ());
		JPanel remover = new JPanel(new BorderLayout ());
		remover.setLayout(new BoxLayout(remover, BoxLayout.PAGE_AXIS));
		adicionar.setLayout(new BoxLayout(adicionar, BoxLayout.PAGE_AXIS));
					
		//Remover AGV
		DFAgentDescription[] agvs = sysManager.getAgentListWithService("Transport");
		listaAGV = new String[agvs.length];
		for(int i=0; i < agvs.length; i++){
			listaAGV[i] = agvs[i].getName().getLocalName();
		}
		comboAGV = new JComboBox(listaAGV);
		comboAGV.addActionListener(this);
				
		//Remover Maquina
		DFAgentDescription[] maquinas = sysManager.getAgentListWithService("ProcessProduct");
		listaMaq = new String[maquinas.length];
		for(int i=0; i < maquinas.length; i++){
			listaMaq[i] = maquinas[i].getName().getLocalName();
		}
		comboMaq = new JComboBox(listaMaq);
		comboMaq.addActionListener(this);
		
		//Border - Criar Caixilhos
		TitledBorder borderAdicionar, borderRemover;
		borderAdicionar = BorderFactory.createTitledBorder("Adicionar");
		adicionar.setBorder(borderAdicionar);
		borderRemover = BorderFactory.createTitledBorder("Remover");
		remover.setBorder(borderRemover);
		
		//Label
		JLabel labelAGV = new JLabel("AGV's");
		JLabel labelMaq = new JLabel("Máquinas");
		
		//JButton
		JButton removeAGV = new JButton();
		removeAGV.setText("Remover AGV");
		removeAGV.setActionCommand("removeAGV");
		removeAGV.addActionListener(this);
		removeAGV.setToolTipText("Escolha o AGV que pretende remover da lista acima.");

		JButton removeMaq = new JButton();
		removeMaq.setText("Remover Máquina");
		removeMaq.setActionCommand("removeMaq");
		removeMaq.addActionListener(this);
		removeMaq.setToolTipText("Escolha a Maquina que pretende remover da lista acima.");
		
		//Caixilho Remover
		remover.add(labelAGV);
		remover.add(comboAGV);
		remover.add(removeAGV);
		remover.add(labelMaq);
		remover.add(comboMaq);
		remover.add(removeMaq);
		
		opcoes.add(remover, BorderLayout.SOUTH);
		
		//Adicionar AGV
		//JButton
		JButton addAGV = new JButton();
		addAGV.setText("Adicionar AGV");
		addAGV.setActionCommand("addAGV");
		addAGV.addActionListener(this);
		
		//Parametros AGV
		JLabel labelNomeAGV = new JLabel("Nome AGV");
		JLabel labelAutonomiaAGV = new JLabel("Autonomia AGV");
		JLabel labelCustoAGV = new JLabel("Custo AGV");
		JLabel labelXAGV = new JLabel("Posição X AGV");
		JLabel labelYAGV = new JLabel("Posição Y AGV");
		JLabel labelVelocidadeAGV = new JLabel("Velocidade AGV");
		JLabel labelCargaMaxAGV = new JLabel("Carga Máxima AGV");
		JLabel labelEstadoAGV = new JLabel("Estado AGV");
	
		//Adicionar Maquina
		//JButton
		JButton addMaq = new JButton();
		addMaq.setText("Adicionar Máquina");
		addMaq.setActionCommand("addMaq");
		addMaq.addActionListener(this);
		
		//Parametros Maquina - locationX locationY [availableOperations]
		JLabel labelNomeMaq = new JLabel("Nome Máquina");
		JLabel labelXMaq = new JLabel("Posição X Máquina");
		JLabel labelYMaq = new JLabel("Posição Y Máquina");
		JLabel labelOperacoes = new JLabel("Operações Máquina");
			
		//Caixilho Adicionar
		adicionar.add(labelNomeAGV);
		adicionar.add(nomeAGVText);
		adicionar.add(labelAutonomiaAGV);
		adicionar.add(autonomiaAGVText);
		adicionar.add(labelCustoAGV);
		adicionar.add(custoAGVText);
		adicionar.add(labelXAGV);
		adicionar.add(xAGVText);
		adicionar.add(labelYAGV);
		adicionar.add(yAGVText);
		adicionar.add(labelVelocidadeAGV);
		adicionar.add(velocidadeAGVText);
		adicionar.add(labelCargaMaxAGV);
		adicionar.add(cargaMaxAGVText);
		adicionar.add(labelEstadoAGV);
		adicionar.add(estadosAGV);
		adicionar.add(addAGV);
		
		adicionar.add(labelNomeMaq);
		adicionar.add(nomeMaqText);
		adicionar.add(labelXMaq);
		adicionar.add(xMaqText);
		adicionar.add(labelYMaq);
		adicionar.add(yMaqText);
		adicionar.add(labelOperacoes);
		adicionar.add(OperacoesText);
		adicionar.add(addMaq);
		
		opcoes.add(adicionar, BorderLayout.NORTH);
				
		//Janela Principal
		JSplitPane janela = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapaOficina, opcoes);
		Dimension minimumSize = new Dimension(200, 500);
		mapaOficina.setMinimumSize(minimumSize);
		opcoes.setMinimumSize(minimumSize);
		
		//Container
		Container c = getContentPane();
		c.add(janela);
	    		
		//---   ---
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if ("removeAGV".equals(e.getActionCommand())) {
			System.out.println("Vou remover o AGV " + comboAGV.getSelectedItem().toString());
			sysManager.removeAgv(comboAGV.getSelectedItem().toString());
			System.out.println("AGV " + comboAGV.getSelectedItem().toString() + " removido");
			

			comboAGV.removeItem(comboAGV.getSelectedItem());
			this.repaint();
			

			//Refresh
			comboAGV.removeItem(comboAGV.getSelectedItem());
			this.repaint();
			

		} else if ("removeMaq".equals(e.getActionCommand())) {
			System.out.println("Vou remover a Maquina " + comboMaq.getSelectedItem().toString());
			sysManager.removeMachine(comboMaq.getSelectedItem().toString());
			System.out.println("Máquina " + comboMaq.getSelectedItem().toString() + " removida");
			

			comboMaq.removeItem(comboMaq.getSelectedItem());
			this.repaint();

			//Refresh
			comboMaq.removeItem(comboMaq.getSelectedItem());
			this.repaint();
			

		} else if ("addAGV".equals(e.getActionCommand())) {
			System.out.println("Vou adiconar o AGV " + nomeAGVText.getText());
			String[] params = {autonomiaAGVText.getText(), custoAGVText.getText(), xAGVText.getText(), yAGVText.getText(), velocidadeAGVText.getText(), cargaMaxAGVText.getText(), estadosAGV.getSelectedItem().toString()};
			sysManager.addAgv(nomeAGVText.getText(), params);
			System.out.println("AGV " + nomeAGVText.getText() + " adicionado");
								
			//Refresh
			comboAGV.addItem(nomeAGVText.getText()); //para aparecer na lista de remover
			
			//Colocar caixas de texto vazias para nova adicao
			nomeAGVText.setText("");
			autonomiaAGVText.setText("");
			custoAGVText.setText("");
			xAGVText.setText("");
			yAGVText.setText("");
			velocidadeAGVText.setText("");
			cargaMaxAGVText.setText("");
			
			this.repaint();
			
		} else if ("addMaq".equals(e.getActionCommand())) {
			System.out.println("Vou adiconar a Máquina " + nomeMaqText.getText());
			String[] params = {xMaqText.getText(), yMaqText.getText(), OperacoesText.getText()};
			sysManager.addMachine(nomeMaqText.getText(), params);
			System.out.println("Máquina " + nomeMaqText.getText() + " adicionada");
			
			//Refresh
			comboMaq.addItem(nomeMaqText.getText());
			
			//Colocar caixas de texto vazias para nova adicao
			nomeMaqText.setText("");
			xMaqText.setText("");
			yMaqText.setText("");
			OperacoesText.setText("");
			
			this.repaint();
		} 
	}
}
