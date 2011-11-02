package agents.machineEngine;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Enumeration;
import java.util.Vector;

public class CfpMachineContractorInitiator extends ContractNetInitiator{

	private static final long serialVersionUID = 4913533665490479921L;

	public CfpMachineContractorInitiator(Agent a, ACLMessage cfp) {
		super(a, cfp);
	}
	
	@Override
	protected Vector prepareCfps(ACLMessage cfp) {
		// TODO Auto-generated method stub
		return super.prepareCfps(cfp);
	}
	
	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		Enumeration<?> e = responses.elements();
		while(e.hasMoreElements()){
			ACLMessage msg = (ACLMessage) e.nextElement();
			System.out.println("message received: " + msg.getContent());	
		}
	}
	
	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {
		// TODO Auto-generated method stub
		super.handlePropose(propose, acceptances);
	}
		
}
