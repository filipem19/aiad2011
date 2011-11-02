package agents.machineEngine;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class CfpMachineContractResponder extends ContractNetResponder{
	private static final long serialVersionUID = 3560413304270027206L;
	
	public CfpMachineContractResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException,
			FailureException, NotUnderstoodException {
		System.out.println(this.myAgent.getLocalName() + ": message received -> " + cfp.getContent());
		ACLMessage msg = cfp.createReply();
		msg.setContent("cenas");
		return msg;
	}
	
}
