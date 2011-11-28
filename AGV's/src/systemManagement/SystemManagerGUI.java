package systemManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;


public class SystemManagerGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6978147188402748454L;

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
		
		//Mapa Oficina
		JPanel mapaOficina = new JPanel();
		mapaOficina.setBackground(Color.ORANGE);
		mapaOficina.setSize(400, 500);
		
		//Opcoes de Adicionar e Remover
		JPanel opcoes = new JPanel(new BorderLayout ());
		JPanel adicionar = new JPanel(new BorderLayout ());
		JPanel remover = new JPanel(new BorderLayout ());
		remover.setLayout(new BoxLayout(remover, BoxLayout.PAGE_AXIS));
		adicionar.setLayout(new BoxLayout(adicionar, BoxLayout.PAGE_AXIS));
				
		//Remover AGV
		String[] listaAGV = {"agv1", "agv2"};
		//TODO: criacao lista com agv
		JComboBox listAGV = new JComboBox(listaAGV);
		listAGV.setSelectedIndex(0);
		listAGV.addActionListener(this);
		
		//Remover Maquina
		String[] listaMaq = {"maq1", "maq2"};
		//TODO: criacao lista com agv
		JComboBox listMaq = new JComboBox(listaMaq);
		listMaq.setSelectedIndex(0);
		listMaq.addActionListener(this);
		
		//Border - Caixilhos
		TitledBorder borderAdicionar, borderRemover;
		borderAdicionar = BorderFactory.createTitledBorder("Adicionar");
		adicionar.setBorder(borderAdicionar);
		borderRemover = BorderFactory.createTitledBorder("Remover");
		remover.setBorder(borderRemover);
		
		//Label
		JLabel labelAGV = new JLabel("AGV's");
		JLabel labelMaq = new JLabel("Máquinas");
		
		remover.add(labelMaq);
		remover.add(listAGV, BorderLayout.LINE_START);
		remover.add(labelAGV);
		remover.add(listMaq, BorderLayout.LINE_END);
		
		opcoes.add(remover, BorderLayout.SOUTH);
		
		//Adicionar AGV
		
		//Adicionar Maquina
		
		opcoes.add(adicionar, BorderLayout.NORTH);
				
		//Janela Principal
		JSplitPane janela = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapaOficina, opcoes);
		Dimension minimumSize = new Dimension(200, 500);
		mapaOficina.setMinimumSize(minimumSize);
		opcoes.setMinimumSize(minimumSize);
		
		//Container
		Container c = getContentPane();
		c.add(janela);
		
				
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
