package negotiationEngine;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import agents.Machine;

public class MachineToMachineCfpContractResponder extends ContractNetResponder{
	private static final long serialVersionUID = 3560413304270027206L;
	
	private Machine agent;
	
	public MachineToMachineCfpContractResponder(Machine a, MessageTemplate mt) {
		super(a, mt);
		// TODO Auto-generated constructor stub
		this.agent = a;
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException,
			FailureException, NotUnderstoodException {
		System.out.println(myAgent.getName() + ": handleCFP\n" + "CFP received: agent (" + myAgent.getAID() + "):" + cfp);
		ACLMessage reply;
		try {
			reply = decodeMessage(cfp);
		} catch (UnreadableException e) {
			return null;
		}
		//TODO to be continued
		reply.setPerformative(ACLMessage.PROPOSE);
		return reply;
	}

	private ACLMessage decodeMessage(ACLMessage cfp) throws UnreadableException {
		ACLMessage reply = null;
		
		/*
		 * Como fazer a verificação da mensagem?
		 * */
		if(cfp.getContentObject() != null && cfp.getContentObject().getClass() == MachineCFP.class){
			MachineCFP cfpContent = (MachineCFP) cfp.getContentObject();
			
			if(agent.isOperationAvailable(cfpContent.getProduct().getCurrentOperation())){
				reply = cfp.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				//TODO properties if of possible negotiations between machies
			}
		}
		
		return reply;
	}
	
}
