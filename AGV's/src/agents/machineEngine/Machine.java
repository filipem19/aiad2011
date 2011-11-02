package agents.machineEngine;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Enumeration;
import java.util.Vector;

import productEngine.product.Operation;
import productEngine.product.Product;

public class Machine extends Agent {
	
	private static final long serialVersionUID = -1033896316722663016L;
	
	private int locationX;
	private int locationY;
	private String machineName;
	private Product productAtWork;
	private Vector<Operation> availableOperations; //Contains all the operations that this machine is able to perform
	private DFAgentDescription[] agents = null;
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		Object[] args = getArguments();
		System.out.println("args" + args);
		if(args.length == 0){
			//TODO falta parametros
//			System.out.println("Empty parameters\nUsage: <location X> <location Y> <Machine Name> <Available operations>");
		}
		
		/* Starts the contract protocol */
		contractInitializer();
	}
	
	/* Set the defenitions to initialize the CFP Handler*/
	private void contractInitializer() {

		/* Behaviour to refresh the AGV list every 5 seconds */
		addBehaviour(new TickerBehaviour(this, 5000) {
			private static final long serialVersionUID = -4378833920060743348L;

			@Override
			protected void onTick() {
				refreshAGVList();
			}
			
		});
		
		ACLMessage message = new ACLMessage(ACLMessage.CFP);
		for(DFAgentDescription a : agents){
			message.addReceiver(a.getName());
		}
		
		message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		message.setContent("teste mensagem");
		addBehaviour(new CfpMachineContractorInitiator(this, message));
		
	}
	
	/**
	 * Search and refreshes the list of AGV's 
	 */
	private void refreshAGVList() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Transport");
		dfd.addServices(sd);
		
		try {
			agents = DFService.search(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("agents with transport service:");
		for(DFAgentDescription a : agents)
			System.out.println("\t" + a.getName());
		
	}	
	
	public boolean isOperationAvailable(Operation oper) {
		return availableOperations.contains(oper);
	}
	
	public Enumeration<Operation> getAvailableOperations(){
		return availableOperations.elements();
	}
	
	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String name) {
		this.machineName = name;
	}

	public Product getProductAtWork() {
		return productAtWork;
	}

	public void setProductAtWork(Product productAtWork) {
		this.productAtWork = productAtWork;
	}
	
}
