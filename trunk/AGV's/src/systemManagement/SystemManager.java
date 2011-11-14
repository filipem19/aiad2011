package systemManagement;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import products.Operation;
import products.Product;
import agents.agvEngine.AGV;
import agents.machineEngine.Machine;

public class SystemManager extends GuiAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612421677795560441L;

	public enum ObjectType {
		Machine, Operation, Product, Agv
	};

	private HashMap<String, Operation> existingOperations = new HashMap<String, Operation>();
	private HashMap<String, Machine> existingMachines = new HashMap<String, Machine>();
	private HashMap<String, Product> existingProducts = new HashMap<String, Product>();
	private HashMap<String, AGV> existingAgvs = new HashMap<String, AGV>();

	transient protected SystemManagerGUI myGui; // The gui

	@Override
	protected void setup() {
		loadProgramData("ProgramData");
		
		registerExistingAgents();
//		myGui = new SystemManagerGUI(this);
	}

	protected void registerExistingAgents() {
		//agent registration
		for(Machine m : existingMachines.values()){
			try {
				getContainerController().acceptNewAgent(m.getMachineName(), m);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			m.initializeAgent();
		}
		
		for(AGV a : existingAgvs.values()){
			try {
				getContainerController().acceptNewAgent(a.getAgvName(), a);
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Load data from file and creates instances of objects specified in this
	 * file
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
				if(strLine.startsWith("//") || strLine.startsWith("\n")){
//					System.out.println("SKIP: " + strLine);
				}
				else{
					// file decoding and object creation
//					System.out.println("DECODE: " + strLine);
					objectType = decodeLine(strLine, objectType);
				}
			}
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("erro na leitura do ficheiro:" + filename + "\t" + e.getMessage());
			e.printStackTrace();
		}
	}

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

	private void createObject(String objectName, String[] properties, ObjectType objectType) {
		// TODO Auto-generated method stub
		switch (objectType) {
		case Machine:
			Machine m = createMachineWithProperties(objectName, properties);
			if (m != null
					&& !existingMachines.keySet().contains(m.getMachineName())) {
				existingMachines.put(m.getMachineName(), m);
			}
			else{
				System.err.println("ERROR - Already existing machine (m = " + m + ")");
			}
			break;
		case Product:
			// read details and create product
			Product p = createProductWithProperties(objectName, properties);
			if (p != null
					&& !existingProducts.keySet().contains(p.getProductName())) {
				existingProducts.put(p.getProductName(), p);
			}
			else{
				System.err.println("ERROR - Already existing product (p = " + p + ")");
			}
			break;
		case Operation:
			// read details and create Operation
			Operation o = createOperationWithProperties(objectName, properties);
			if(o != null && !existingOperations.containsKey(o.getOperationName())){
				existingOperations.put(o.getOperationName(), o);
			}
			else{
				System.err.println("ERROR - Already existing operation (o = " + o + ")");
			}
			break;
		case Agv:
			// read details and create AGV
			AGV a = createAgvWithProperties(objectName, properties);
			if(a != null && !existingAgvs.containsKey(a.getAgvName())){
				existingAgvs.put(a.getName(), a);
			}
			else{
				System.err.println("ERROR - Already existing AGV (a = " + a + ")");
			}
			break;
		default:
			break;
		}
	}

	private AGV createAgvWithProperties(String agvName, String[] properties) {
		if (properties.length < 7)
			return null;

		int autonomy = Integer.parseInt(properties[0]), cost = Integer
				.parseInt(properties[1]), locationX = Integer
				.parseInt(properties[2]), locationY = Integer
				.parseInt(properties[3]), velocity = Integer
				.parseInt(properties[4]);

		double maxLoad = Double.parseDouble(properties[5]);

		String status = properties[6];

		return new AGV(autonomy, cost, locationX, locationY, velocity, maxLoad,
				status, agvName);
	}

	private Operation createOperationWithProperties(String operationName, String[] properties) {
		if (properties.length != 2)
			return null;

		int operationDuration = Integer.parseInt(properties[0]);
		double deltaWeight = Double.parseDouble(properties[1]);
				
		return new Operation(operationDuration, deltaWeight, operationName);
	}

	private Product createProductWithProperties(String productName, String[] properties) {

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

	private Machine createMachineWithProperties(String machineName, String[] properties) {

		if (properties.length < 2)
			return null;

		int locationX = Integer.parseInt(properties[0]), locationY = Integer
				.parseInt(properties[1]);
		Vector<Operation> availableOperations = new Vector<Operation>();

		int i = 2;

		while (i < properties.length) {
			// check if the operation exists
			if (!existingOperations.containsKey(properties[i])) {
				System.err.println("unexistent operation (" + properties[i]
						+ ")");
				return null;
			}
			availableOperations.add(existingOperations.get(properties[i]));
			i++;
		}
		
		
//		AgentController ac = null;
//		
//		try {
//			ac = getContainerController().createNewAgent(machineName, "agents.machineEngine.Machine", null);
//		} catch (StaleProxyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return new Machine(locationX, locationY, machineName,
				availableOperations);
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

	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub

	}
}
