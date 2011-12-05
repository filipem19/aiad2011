package negotiationEngine;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.util.Enumeration;
import java.util.Vector;

public class MachineContractInitiator extends ContractNetInitiator{

	private static final long serialVersionUID = 4913533665490479921L;

	public MachineContractInitiator(Agent a, ACLMessage cfp) {
		super(a, cfp);
	}

	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		System.out.println(myAgent.getLocalName() +" : handle all responses");
		Enumeration e = responses.elements();
//		while(e.hasMoreElements()){
//			ACLMessage msg = (ACLMessage) e.nextElement();
//			System.out.println(myAgent.getLocalName()+ "message received(conversationId:" + msg.getConversationId() + "): " + msg);	
//		}
	}
	
	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {
		// TODO Auto-generated method stub
		MachineCFP content = null;
		
		try {
			content = (MachineCFP) propose.getContentObject();
		} catch (UnreadableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(content == null)
			return;
		System.out.println(myAgent.getAID().getLocalName() + " received a propose from " + propose.getSender().getLocalName() + ": " + content);
		Enumeration e = acceptances.elements();
		while(e.hasMoreElements()){
			ACLMessage msg = (ACLMessage) e.nextElement();
			System.out.println("\t" + msg);
		}
	}
	
	public boolean finnished(){
		return true;
	}
	
	protected int evaluate(){
		return 0;
	}
}
