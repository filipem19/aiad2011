package systemManagement;
      
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import products.Operation;


public class SystemManagerGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6978147188402748454L;
	
	private static final int WINDOW_X_SIZE = 800;
	private static final int WINDOW_Y_SIZE = 800;
	private static final int WINDOW_X_POS = 500;
	private static final int WINDOW_Y_POS = 50;
	
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
	JList operationList;
	

	public SystemManagerGUI(SystemManager sysManager) {
		this.sysManager = sysManager;
		initializeWindowPreferences();
	}
	
	private void initializeWindowPreferences(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Initial platform");
		setVisible(true);
		setSize(WINDOW_X_SIZE, WINDOW_Y_SIZE);
		setLocation(WINDOW_X_POS, WINDOW_Y_POS);
		
		//---Pagina Principal---
		
		// Simulação de lista de localizações de AGVs e de Máquinas.
		int[][] AgvLoc = new int[10][2];
		int[][] MachineLoc = new int[10][2];
		
		AgvLoc[0][0] = 10;
		AgvLoc[0][1] = 10;
		AgvLoc[1][0] = 70;
		AgvLoc[1][1] = 90;
		AgvLoc[2][0] = 60;
		AgvLoc[2][1] = 40;
		AgvLoc[3][0] = 15;
		AgvLoc[3][1] = 50;
		AgvLoc[4][0] = 25;
		AgvLoc[4][1] = 100;
		
		MachineLoc[0][0] = 25;
		MachineLoc[0][1] = 25;
		MachineLoc[1][0] = 35;
		MachineLoc[1][1] = 90;
		MachineLoc[2][0] = 60;
		MachineLoc[2][1] = 60;
		MachineLoc[3][0] = 85;
		MachineLoc[3][1] = 80;
		MachineLoc[4][0] = 100;
		MachineLoc[4][1] = 100;
		
		//Mapa Oficina
		mapa mapaOficina = new mapa(AgvLoc, MachineLoc);
		mapaOficina.setBackground(Color.ORANGE);
		mapaOficina.setSize(WINDOW_X_SIZE / 2, WINDOW_Y_SIZE);
		
		//Opcoes de Adicionar e Remover
		GridBagLayout opcoesLayout = new GridBagLayout();
		JPanel opcoes = new JPanel(opcoesLayout);
		
		JPanel adicionar = new JPanel(new BorderLayout ());
		JPanel remover = new JPanel(new BorderLayout ());
		
		JPanel agv = new JPanel(new BorderLayout ());
		JPanel maq = new JPanel(new BorderLayout ());
		
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
		
//		opcoes.add(remover, BorderLayout.EAST);
		
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
//		adicionar.add(OperacoesText);
		this.operationList = createList(sysManager.getExistingOperations());
		this.operationList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		adicionar.add(operationList);
		adicionar.add(addMaq);
		
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.fill = GridBagConstraints.HORIZONTAL;
		constraint.weighty = 10;
		opcoes.add(adicionar,constraint);
		constraint.weighty=200;
		opcoes.add(remover,constraint);
		
		opcoes.add(addtestbutton(),constraint);
		
		//Janela Principal
		JSplitPane janela = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapaOficina, opcoes);
		Dimension minimumSize = new Dimension(WINDOW_X_POS / 3, WINDOW_Y_POS / 4);
		mapaOficina.setMinimumSize(minimumSize);
		opcoes.setMinimumSize(minimumSize);
		
		//Container
		Container c = getContentPane();
		c.add(janela);
	    		
		//---   ---
	}

	private Component addtestbutton() {
		JButton sendmessage = new JButton("m10 p1");
		sendmessage.addActionListener(this);
		sendmessage.setActionCommand("teste");
		return sendmessage;
	}

	private JList createList(HashMap<String, Operation> existingOperations) {
		JList list = new JList(new OperationList(existingOperations));
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if ("removeAGV".equals(e.getActionCommand())) {
			if(comboAGV.getSelectedItem() == null)
				return;
			System.out.println("Vou remover o AGV " + comboAGV.getSelectedItem().toString());
			sysManager.removeAgv(comboAGV.getSelectedItem().toString());
			System.out.println("AGV " + comboAGV.getSelectedItem().toString() + " removido");
			
			//Refresh
			comboAGV.removeItem(comboAGV.getSelectedItem());
			
		} else if ("removeMaq".equals(e.getActionCommand())) {
			if(comboMaq.getSelectedItem() == null)
				return;
			
			System.out.println("Vou remover a Maquina " + comboMaq.getSelectedItem().toString());
			sysManager.removeMachine(comboMaq.getSelectedItem().toString());
			System.out.println("Máquina " + comboMaq.getSelectedItem().toString() + " removida");
			
			//Refresh
			comboMaq.removeItem(comboMaq.getSelectedItem());
			
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
			
			
		} else if ("addMaq".equals(e.getActionCommand())) {
			System.out.println("Vou adiconar a Máquina " + nomeMaqText.getText());
			Vector<String> selectedOperations = new Vector<String>();
			for(Object o: operationList.getSelectedValues()){
				Operation oper = (Operation)o;
				selectedOperations.add(oper.getOperationName()); 
			}
			
			String[] params = new String[selectedOperations.size() + 2];
			params[0] = xMaqText.getText();
			params[1] = yMaqText.getText();
			for(int i = 0; i< selectedOperations.size();i++){
				System.out.println(i);
				params[i+2] = selectedOperations.get(i);
			}
			sysManager.addMachine(nomeMaqText.getText(), params);
			System.out.println("Máquina " + nomeMaqText.getText() + " adicionada");
			
			//Refresh
			comboMaq.addItem(nomeMaqText.getText()); //para aparecer na lista de remover
			
			//Colocar caixas de texto vazias para nova adicao
			nomeMaqText.setText("");
			xMaqText.setText("");
			yMaqText.setText("");
						
		} else if ("teste".equals(e.getActionCommand())) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
			msg.setContent("m10 p1");
			msg.addReceiver(sysManager.getAID());
			sysManager.send(msg);
		}
		this.repaint();
	}
}
