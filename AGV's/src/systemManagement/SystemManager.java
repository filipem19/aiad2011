package systemManagement;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import products.Operation;
import products.Product;

public class SystemManager extends GuiAgent {

	// Constants
	public final static String TAKE_DOWN = "TAKE_DOWN";

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612421677795560441L;

	public enum ObjectType {
		Machine, Operation, Product, Agv
	};

	private HashMap<String, Operation> existingOperations = new HashMap<String, Operation>();
	private HashMap<String, Product> existingProducts = new HashMap<String, Product>();
	private HashMap<String, Location> machineMap = new HashMap<String, Location>();

	transient protected SystemManagerGUI myGui; // The gui

	@Override
	protected void setup() {
		loadProgramData("ProgramData");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendMachineMap();
		myGui = new SystemManagerGUI(this);
		testFunctions();
	}

	private void sendMachineMap() {
		ACLMessage machineMapMessage = new ACLMessage(ACLMessage.INFORM_REF);

		try {
			machineMapMessage.setContentObject(machineMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (DFAgentDescription agv : getAgentListWithService("Transport")) {
			machineMapMessage.addReceiver(agv.getName());
		}

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(machineMapMessage);
	}

	private void testFunctions() {
		// Comportamento para adicionar por um produto numa maquina
		addBehaviour(new CyclicBehaviour(this) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1232462488859989835L;

			@Override
			public void action() {
				ACLMessage msg = receive(MessageTemplate
						.MatchPerformative(ACLMessage.INFORM_IF));
				if (msg != null) {
					// mensagem com nome do agente a receber o produto e o
					// produto
					System.out.println(getLocalName() + ": content: "
							+ msg.getContent());
					String[] parts = msg.getContent().split(" ");
					if (parts.length == 2) {
						DFAgentDescription[] agents = getAgentListWithService("ProcessProduct");

						ACLMessage msgtomachine = new ACLMessage(
								ACLMessage.INFORM_IF);

						for (DFAgentDescription agent : agents) {
							if (agent.getName().getLocalName()
									.compareTo(parts[0]) == 0) {
								msgtomachine.addReceiver(agent.getName());
							}
						}
						try {
							Product p = existingProducts.get(parts[1]);
							msgtomachine.setContentObject(p);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.err
									.println("ERROR - adding product to msg content");
							e.printStackTrace();
						}

//						try {
//							System.out.println(getLocalName()
//									+ ": teste sending message: "
//									+ msgtomachine + " message contentObject: "
//									+ msgtomachine.getContentObject());
//						} catch (UnreadableException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}

						send(msgtomachine);
					}
				}
				block();
			}
		});
	}

	/**
	 * Load data from file and creates instances of objects specified in this
	 * file
	 * 
	 * @param filename
	 */
	private void loadProgramData(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			ObjectType objectType = null;
			while ((strLine = br.readLine()) != null) {
				if (strLine.startsWith("//") || strLine.startsWith("\n")) {
					// System.out.println("SKIP: " + strLine);
				} else {
					// file decoding and object creation
					// System.out.println("DECODE: " + strLine);
					objectType = decodeLine(strLine, objectType);
				}
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("erro na leitura do ficheiro:" + filename + "\t"
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param strLine
	 * @param
	 * @return
	 */
	private ObjectType decodeLine(String strLine, ObjectType objectType) {
		String[] parts = strLine.split(":");
		if (parts.length == 1) {
			objectType = identifyObjectType(parts[0]);
			if (objectType == null) {
				System.err.println("error parsing file : File corrupt");
			}
		} else {
			String objectName = parts[0], properties = parts[1];
			createObject(objectName, properties.split(" "), objectType);
		}

		return objectType;
	}

	private void createObject(String objectName, String[] properties,
			ObjectType objectType) {
		// TODO Auto-generated method stub
		switch (objectType) {
		case Machine:
			createMachineWithProperties(objectName, properties);
			break;
		case Product:
			// read details and create product
			Product p = createProductWithProperties(objectName, properties);
			if (p != null
					&& !existingProducts.keySet().contains(p.getProductName())) {
				existingProducts.put(p.getProductName(), p);
			} else {
				System.err.println("ERROR - Already existing product (p = " + p
						+ ")");
			}
			break;
		case Operation:
			// read details and create Operation
			Operation o = createOperationWithProperties(objectName, properties);
			if (o != null
					&& !existingOperations.containsKey(o.getOperationName())) {
				existingOperations.put(o.getOperationName(), o);
			} else {
				System.err.println("ERROR - Already existing operation (o = "
						+ o + ")");
			}
			break;
		case Agv:
			// read details and create AGV
			createAgvWithProperties(objectName, properties);
			break;
		default:
			break;
		}
	}

	private void createAgvWithProperties(String agvName, String[] properties) {
		if (properties.length == 7) {
			// System.out.println("creating agv");
			int autonomy = Integer.parseInt(properties[0]), cost = Integer
					.parseInt(properties[1]), locationX = Integer
					.parseInt(properties[2]), locationY = Integer
					.parseInt(properties[3]), velocity = Integer
					.parseInt(properties[4]);

			double maxLoad = Double.parseDouble(properties[5]);

			String status = properties[6];
			Object[] args = { autonomy, cost, locationX, locationY, velocity,
					maxLoad, status, agvName };

			try {
				AgentController ac = getContainerController().createNewAgent(
						agvName, "agents.agvEngine.AGV", args);
				ac.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("agv not created");
		}
	}

	private Operation createOperationWithProperties(String operationName,
			String[] properties) {
		if (properties.length != 2)
			return null;

		int operationDuration = Integer.parseInt(properties[0]);
		double deltaWeight = Double.parseDouble(properties[1]);

		return new Operation(operationDuration, deltaWeight, operationName);
	}

	private Product createProductWithProperties(String productName,
			String[] properties) {

		if (properties.length < 2)
			return null;

		LinkedList<Operation> operations = new LinkedList<Operation>();

		double currentWeight = Double.parseDouble(properties[0]);

		int i = 2;

		while (i < properties.length) {
			if (!existingOperations.containsKey(properties[i])) {
				System.err.println("unexistent operation (" + properties[i]
						+ ")");
				return null;
			}
			operations.add(existingOperations.get(properties[i]));
			i++;
		}

		return new Product(currentWeight, productName, operations);
	}

	private void createMachineWithProperties(String machineName,
			String[] properties) {

		if (properties.length >= 2) {

			int locationX = Integer.parseInt(properties[0]), locationY = Integer
					.parseInt(properties[1]);
			Vector<Operation> availableOperations = new Vector<Operation>();
			int i = 2;

			while (i < properties.length) {
				// check if the operation exists
				if (!existingOperations.containsKey(properties[i])) {
					System.err.println("unexistent operation (" + properties[i]
							+ ")");
				} else {
					availableOperations.add(existingOperations
							.get(properties[i]));
				}

				i++;
			}
			Object[] args = { locationX, locationY, availableOperations };

			try {
				AgentController ac = getContainerController().createNewAgent(
						machineName, "agents.machineEngine.Machine", args);
				ac.start();
				machineMap
						.put(ac.getName(), new Location(locationX, locationY));
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				System.err.println("Error - problem creating agent ("
						+ machineName + ")!");
				e.printStackTrace();
			}
		} else {
			System.out.println("error creating machine - bad parameters");
		}
	}

	private ObjectType identifyObjectType(String string) {

		if (string.compareTo("Machines") == 0)
			return ObjectType.Machine;

		if (string.compareTo("Products") == 0)
			return ObjectType.Product;

		if (string.compareTo("Operations") == 0)
			return ObjectType.Operation;

		if (string.compareTo("Operations") == 0)
			return ObjectType.Operation;

		if (string.compareTo("Agv") == 0)
			return ObjectType.Agv;

		return null;
	}

	public DFAgentDescription[] getAgentListWithService(String serviceName) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceName);
		dfd.addServices(sd);
		DFAgentDescription[] agents = null;
		try {
			agents = DFService.search(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("agents with " + serviceName + "service:");
		// for (DFAgentDescription a : agents)
		// System.out.println("\t" + a.getName());

		return agents;
	}

	/**
	 * 
	 * @param agvName
	 */
	public void removeAgv(String agvName) {

		AID aID = new AID(agvName, AID.ISLOCALNAME);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
		msg.setContent(TAKE_DOWN);
		msg.addReceiver(aID);
		send(msg);
	}

	/**
	 * 
	 * @param machineName
	 */
	public void removeMachine(String machineName) {

		AID aID = new AID(machineName, AID.ISLOCALNAME);
		machineMap.remove(aID.getName());
		sendMachineMap();
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
		msg.setContent(TAKE_DOWN);
		msg.addReceiver(aID);
		send(msg);

	}

	/**
	 * 
	 * @param agvName
	 * @param params
	 *            [autonomy cost locationX locationY velocity maxLoad status]
	 */
	public void addAgv(String agvName, String[] params) {
		createAgvWithProperties(agvName, params);
	}

	/**
	 * 
	 * @param machineName
	 * @param params
	 *            [locationX locationY [availableOperations]*]
	 */
	public void addMachine(String machineName, String[] params) {
		createMachineWithProperties(machineName, params);
		sendMachineMap();
	}

	public HashMap<String, Operation> getExistingOperations() {
		return existingOperations;
	}

	public HashMap<String, Product> getExistingProducts() {
		return existingProducts;
	}

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub

	}
}
