package agents.machineEngine;

import java.io.IOException;

import negotiationEngine.MachineCFP;
import negotiationEngine.MachineToMachineCfpContractorInitiator;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import products.Operation;
import products.Product;

public class ProcessProductBehaviour extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8171703463270555600L;
	
	private Product product;
	private Machine machine;
	private boolean finnished = false;
	
	public ProcessProductBehaviour(Product product, Machine machine) {
		super(machine);
		this.product = product;
		this.machine = machine;
		action();
	}
	
	@Override
	public void action() {
		Operation op = product.getCurrentOperation(); 
		System.out.println(myAgent.getLocalName() + ": operation: " + op + "\nMachine: " + machine);
		while(op != null && machine.isOperationAvailable(op)){
			block(op.getOperationDuration());
			op = product.nextOperation();
			System.out.println(myAgent.getLocalName() + ": Processing Product (" + op.getOperationDuration()*1000 + ")");
			block(op.getOperationDuration()*1000);
		}
		finnished = true;
		
		//M2MCFP
		System.out.println(myAgent.getLocalName() + ": Starting cfpContract Initiator");
		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		
		DFAgentDescription[] agents = machine.getAgentListWithService("ProcessProduct");
		for(DFAgentDescription agent : agents){
			cfp.addReceiver(agent.getName());
		}
		
		MachineCFP cfpContent = new MachineCFP(product);
		try {
			cfp.setContentObject(cfpContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		machine.addBehaviour(new MachineToMachineCfpContractorInitiator(myAgent, cfp));
	}

	@Override
	public boolean done() {
		return finnished;
	}
	
}
