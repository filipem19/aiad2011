package agents;

import jade.core.Agent;
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
import negotiationEngine.MachineToMachineCfpContractorInitiator;
import products.Operation;
import products.Product;

public class Machine extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1033896316722663016L;

	private int locationX;
	private int locationY;
	private String machineName;
	private Product productAtWork;
	private Vector<Operation> availableOperations; // Contains all the
													// operations that this
													// machine is able to
													// perform
	protected MachineToMachineCfpContractorInitiator contractInitiator;
	private MachineToMachineCfpContractResponder mToMContractResponder;

	@Override
	protected void setup() {
		contractResponderInitialization();
		registerDfService("Machine", "BUILD");
	}

	private void contractResponderInitialization() {

		MessageTemplate template = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.MatchPerformative(ACLMessage.CFP));
		mToMContractResponder = new MachineToMachineCfpContractResponder(this,
				template);
		addBehaviour(mToMContractResponder);
	}

	private void registerDfService(String serviceName, String serviceType) {
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
			System.out.println("EXCEPTION - Failed registering df service ("
					+ service.getName() + " : " + service.getType() + ")");
			e.printStackTrace();
		}
	}

	private void machineToMachineContractInitializer() {
		DFAgentDescription[] agents = getAgentsWithService("BUILD");
		ACLMessage message = createCfpWithAgents(agents);

		System.out.println("agents with BUILD service:");
		for (DFAgentDescription a : agents) {
			System.out.println("\t" + a.getName());
		}

		contractInitiator = new MachineToMachineCfpContractorInitiator(this,
				message);
		addBehaviour(contractInitiator);
	}

	protected ACLMessage createCfpWithAgents(DFAgentDescription[] agents) {
		ACLMessage message = new ACLMessage(ACLMessage.CFP);
		message.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		for (DFAgentDescription a : agents)
			message.addReceiver(a.getName());

		return message;
	}

	protected DFAgentDescription[] getAgentsWithService(String serviceName) {
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
		return agents;
	}

	private Product executeOperation(Product product) {
		while (product.getCurrentOperation() != null && isOperationAvailable(product.getCurrentOperation())) {
			// TODO need to wait somehow the time value needed to accomplish the operation, and then proceed to next operation
			product.nextOperation();
		}
		return product;
	}

	public boolean isOperationAvailable(Operation oper) {
		return availableOperations.contains(oper);
	}

	/**
	 * 
	 * @return Enumeration with machine operations available 
	 */
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

	/**
	 * Set the product to be processed at the machine 
	 * @param productAtWork
	 */
	public void setProductAtWork(Product productAtWork) {
		if(this.productAtWork == null)
			addBehaviour(new ProcessProductBehaviour(productAtWork));
		this.productAtWork = productAtWork;
	}
	
	/**
	 * This method does the handles product that cannot longer be processed at this machine
	 * or is already finnished
	 */
	private void dispachProduct() {
		if(productAtWork.isComplete()){
			//TODO Piece finnished
		}
		else{
			//TODO negotiation with another machine
		}
		this.productAtWork = null;
	}

}
