package agents.machineEngine;

import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Date;

import negotiationEngine.Cfp;
import negotiationEngine.ContractInitiator;
import products.Operation;
import products.Product;

public class ProcessProductBehaviour extends SimpleBehaviour {

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
	}

	@Override
	public void action() {
		Operation op = product.getCurrentOperation();
//		System.out.println(product + " " + op + " " + machine.isOperationAvailable(op));
		while (op != null && machine.isOperationAvailable(op)) {
			System.out.println(myAgent.getLocalName() + ": Processing product (" + product.getProductName() + ") operation = " + op.getOperationName() + " time = " + op.getOperationDuration() + "s");
			myAgent.doWait(op.getOperationDuration()*1000);
			op = product.nextOperation();
			//TODO changes at piece
		}
		finnished = true;

		if(product.getCurrentOperation() == null){
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
			msg.setContent("RefreshMessage " + product.toString() + " Concluído");
			msg.addReceiver(this.machine.getAgentListWithService("SystemManagement") [0].getName());
			myAgent.send(msg);
			myAgent.removeBehaviour(this);
			return;
		}
		
		// M2MCFP
		
		DFAgentDescription[] agvs = machine
				.getAgentListWithService("Transport"), machines = machine.getAgentListWithService("ProcessProduct");

		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

		Cfp cfpContent = new Cfp(product, myAgent.getAID());
		
		try {
			cfp.setContentObject(cfpContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cfp = addReceivers(cfp, machines);
		cfp = addReceivers(cfp, agvs);
		
		cfp.removeReceiver(myAgent.getAID());
		
		cfp.setReplyByDate(new Date(System.currentTimeMillis() + 2000));
		myAgent.addBehaviour(new ContractInitiator(myAgent, cfp));
		
	}

	private ACLMessage addReceivers(ACLMessage cfp, DFAgentDescription[] list){
		for(DFAgentDescription agent : list)
			cfp.addReceiver(agent.getName());
		return cfp;
	}
	
	@Override
	public boolean done() {
		return finnished;
	}

}
