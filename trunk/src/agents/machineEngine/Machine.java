package agents.machineEngine;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Enumeration;
import java.util.Vector;

import negotiationEngine.MachineToMachineCfpContractResponder;
import products.Operation;
import products.Product;

public class Machine extends Agent {

	private static final long serialVersionUID = -1033896316722663016L;

	
	/* Agent properties */
	private int locationX;
	private int locationY;
	private String machineName;
	private Product productAtWork;
	private Vector<Operation> availableOperations; // Contains all the
													// operations that this
													// machine is able to
													// perform

	/* ------ */

	public Machine(int locationX, int locationY, String machineName,
			Vector<Operation> availableOperations) {
		this.locationX = locationX;
		this.locationY = locationY;
		this.machineName = machineName;
		this.availableOperations = availableOperations;
		System.out.println("\nCreated => " + toString());
	}

	@Override
	protected void setup() {
		super.setup();
	}

	public void initializeAgent(){
		setup();
		System.out.println("intializing agent: " + getMachineName());
		
		registerAgentAtDF("ProcessProduct:" + getMachineName(),
				"ProcessProduct");
		/* Starting FIPA contract Responder Protocols */
//		machineToMachineContractResponder();
		addBehaviour(new CyclicBehaviour(this) 
        {
             /**
			 * 
			 */
			private static final long serialVersionUID = -8611591964691930759L;

			public void action() 
             {
				System.out.println("behaviour");
                ACLMessage msg= receive();
                if (msg!=null)
                    System.out.println( " - " +
                       myAgent.getLocalName() + " <- " +
                       msg.getContent() );
                System.out.println("block");
                block();
             }
        });
	}
	/* test for contract behaviour */
	/*-----------*/

	/**
	 * Register Machine agent on DF with given parameters
	 * 
	 * @param serviceName
	 * @param serviceType
	 */
	private void registerAgentAtDF(String serviceName, String serviceType) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription service = new ServiceDescription();
		service.setName(serviceName);
		service.setType(serviceType);
		dfd.addServices(service);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initiates the ContractResponder to other machines CFP's (Call for
	 * proposal to process a product)
	 */
	private void machineToMachineContractResponder() {

		MessageTemplate template = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.MatchPerformative(ACLMessage.CFP));
		addBehaviour(new MachineToMachineCfpContractResponder(this, template));
	}

	/**
	 * Search and refreshes the list of AGV's
	 */
	private DFAgentDescription[] getAGVList(String serviceName) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Transport");
		dfd.addServices(sd);
		DFAgentDescription[] agents = null;
		try {
			agents = DFService.search(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("agents with transport service:");
		for (DFAgentDescription a : agents)
			System.out.println("\t" + a.getName());

		return agents;
	}

	public boolean isOperationAvailable(Operation oper) {
		return availableOperations.contains(oper);
	}

	public Enumeration<Operation> getAvailableOperations() {
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
		addBehaviour(new ProcessProductBehaviour(this.productAtWork));
	}

	@Override
	public String toString() {
		return "Machine (" + getMachineName() + "):\n\tlocationX = "
				+ locationX + "\tlocationY = " + locationY
				+ "\tavailableOperations = " + availableOperations;
	}
}
