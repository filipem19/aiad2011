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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
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
		JTextField nomeAGVText = new JTextField();
		JLabel labelAutonomiaAGV = new JLabel("Autonomia AGV");
		JTextField autonomiaAGVText = new JTextField();
		JLabel labelCustoAGV = new JLabel("Custo AGV");
		JTextField custoAGVText = new JTextField();
		JLabel labelXAGV = new JLabel("Posição X AGV");
		JTextField xAGVText = new JTextField();
		JLabel labelYAGV = new JLabel("Posição Y AGV");
		JTextField yAGVText = new JTextField();
		JLabel labelVelocidadeAGV = new JLabel("Velocidade AGV");
		JTextField velocidadeAGVText = new JTextField();
		JLabel labelCargaMaxAGV = new JLabel("Carga Máxima AGV");
		JTextField cargaMaxAGVText = new JTextField();
		JLabel labelEstadoAGV = new JLabel("Estado AGV");
		String[] listaEstados = {"estado1", "estado2"};
		JComboBox estadosAGV = new JComboBox(listaEstados);
	
		//Adicionar Maquina
		//JButton
		JButton addMaq = new JButton();
		addMaq.setText("Adicionar Máquina");
		addMaq.setActionCommand("addMaq");
		addMaq.addActionListener(this);
		
		//Parametros Maquina - locationX locationY [availableOperations]
		JLabel labelNomeMaq = new JLabel("Nome Máquina");
		JTextField nomeMaqText = new JTextField();
		JLabel labelXMaq = new JLabel("Posição X Máquina");
		JTextField xMaqText = new JTextField();
		JLabel labelYMaq = new JLabel("Posição Y Máquina");
		JTextField yMaqText = new JTextField();
		JLabel labelOperacoes = new JLabel("Operações Máquina");
		JTextField OperacoesText = new JTextField();
		
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
		
		//accao a executar quando pressionados os botoes
		if ("removeAGV".equals(e.getActionCommand())) {
			System.out.println("Vou remover o AGV " + comboAGV.getSelectedItem().toString());
			sysManager.removeAgv(comboAGV.getSelectedItem().toString());
			System.out.println("AGV " + comboAGV.getSelectedItem().toString() + " removido");
			
		} else if ("removeMaq".equals(e.getActionCommand())) {
			System.out.println("Vou remover a Maquina " + comboMaq.getSelectedItem().toString());
			sysManager.removeMachine(comboMaq.getSelectedItem().toString());
			System.out.println("Máquina " + comboMaq.getSelectedItem().toString() + " removida");
			
		} else if ("addAGV".equals(e.getActionCommand())) {
			System.out.println("Vou adiconar o AGV ");
			//sysManager.addAgv(String agvName, String[] params);
			System.out.println("AGV adicionado");
			
		} else if ("addMaq".equals(e.getActionCommand())) {
			System.out.println("Vou adiconar a Máquina ");
			//sysManager.addMachine(String machineName, String[] params);
			System.out.println("Máquina adicionada");
			
		} 
	}
}
