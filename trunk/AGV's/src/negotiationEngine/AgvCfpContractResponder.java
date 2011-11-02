package negotiationEngine;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class AgvCfpContractResponder extends ContractNetResponder{
	private static final long serialVersionUID = 3560413304270027206L;
	
	public AgvCfpContractResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException,
			FailureException, NotUnderstoodException {
		System.out.println(myAgent.getName() + ": handleCFP\n" + "CFP received: agent (" + myAgent.getAID() + "):" + cfp);
		ACLMessage reply = cfp.createReply();
		//TODO include the message content
		reply.setPerformative(ACLMessage.PROPOSE);
		return reply;
	}
	
}
