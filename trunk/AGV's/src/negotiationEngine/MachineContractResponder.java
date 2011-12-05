package negotiationEngine;

import java.io.IOException;

import systemManagement.SystemManager.ObjectType;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import agents.agvEngine.AGV;
import agents.machineEngine.Machine;

public class MachineContractResponder extends ContractNetResponder{
	private static final long serialVersionUID = 3560413304270027206L;
	
	public MachineContractResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException,
			FailureException, NotUnderstoodException {
		try {
			System.out.println("\n" + myAgent.getName() + ": handleCFP\n" + "CFP received: agent (" + myAgent.getLocalName() + "):" + cfp.getContentObject().getClass());
		} catch (UnreadableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ACLMessage reply = cfp.createReply();
		
		reply.setPerformative(ACLMessage.PROPOSE);
		
		//TODO include the message content
		if(myAgent.getClass() == AGV.class){
			try {
				if(cfp.getContentObject().getClass() == MachineCFP.class)
					reply = getAgvMessageContent(reply, (MachineCFP)cfp.getContentObject());
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;

			}
		}
		else if(myAgent.getClass() == Machine.class){
			try {
				if(cfp.getContentObject().getClass() == MachineCFP.class)
					reply = getMachineMessageContent(reply, (MachineCFP) cfp.getContentObject());
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		else{
			return null;
		}
		
		return reply;
	}
	
	private ACLMessage getMachineMessageContent(ACLMessage reply, MachineCFP content) {
		// TODO Auto-generated method stub
		content.setDestination(myAgent.getAID());
		content.setType(ObjectType.Machine);
		try {
			reply.setContentObject(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reply;
	}

	private ACLMessage getAgvMessageContent(ACLMessage reply, MachineCFP content) {
		// TODO Auto-generated method stub
		content = calculateCosts(content);
		return reply;
	}

	private MachineCFP calculateCosts(MachineCFP content) {
		for(DFAgentDescription agent : ((AGV)myAgent).getAgentListWithService("ProcessProduct")){
			//falta posição das máquinas
		}
		return null;
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
