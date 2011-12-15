package agents.machineEngine;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;

import java.util.Enumeration;
import java.util.Vector;

import negotiationEngine.ContractResponder;
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
	
	private ProcessProductBehaviour processBehaviour;

	@Override
	protected void setup() {
		Object[] args = getArguments();
		if (args.length < 3) {
//			System.out.println("takedown");
			takeDown();
			return;
		}
		
		setMachineProperties((Integer) args[0], (Integer) args[1],
				this.getName(), (Vector) args[2]);
		initializeAgent();
		

	}

	protected void shutdownAgent() {
		
		this.doDelete();
	}

	public void setMachineProperties(int locationX, int locationY,
			String machineName, Vector<Operation> availableOperations) {
		this.locationX = locationX;
		this.locationY = locationY;
		this.machineName = machineName;
		this.availableOperations = availableOperations;
		// System.out.println("\nCreated => " + toString());
	}

	public void initializeAgent() {

		addBehaviour(new MachineInformIfMessageHandler(this));

		registerAgentAtDF("ProcessProduct:" + getMachineName(),
				"ProcessProduct");

		/* Starting FIPA contract Responder Protocols */
		machineContractResponder();
		
	}

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
	private void machineContractResponder() {

		MessageTemplate template = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.MatchPerformative(ACLMessage.CFP));
		addBehaviour(new ContractResponder(this, template));
	}

	/**
	 * Search and refreshes the list of AGV's
	 */

	public boolean isOperationAvailable(Operation oper) {
		for (Operation operation : availableOperations) {
			if (operation.getOperationName().compareTo(oper.getOperationName()) == 0) {
				return true;
			}
		}

		return false;
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
//		System.out.println(getLocalName() + ": setProductAtWork:"
//				+ productAtWork);
		this.productAtWork = productAtWork;
		if(processBehaviour != null){
			removeBehaviour(processBehaviour);
		}
		
		if(productAtWork != null){
			processBehaviour = new ProcessProductBehaviour(
					this.productAtWork, this);
			addBehaviour(processBehaviour);
		}
	}

	@Override
	public String toString() {
		return "Machine (" + getMachineName() + "):\n\tlocationX = "
				+ locationX + "\tlocationY = " + locationY
				+ "\tavailableOperations = " + availableOperations;
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
}
