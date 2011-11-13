package negotiationEngine;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class MachineToAgvCfpContractResponder extends ContractNetResponder{
	private static final long serialVersionUID = 3560413304270027206L;
	
	public MachineToAgvCfpContractResponder(Agent a, MessageTemplate mt) {
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
		reply.setContent("teste reply");		
		return reply;
	}

	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) throws FailureException {
		// TODO Auto-generated method stub
		System.out.println("proposta aceite:" + propose.toString());
		ACLMessage reply = cfp.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}
	
	@Override
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose,
			ACLMessage reject) {
		System.out.println("Proposta rejeitada: " + propose.toString());
	}
}
