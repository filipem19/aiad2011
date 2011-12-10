package negotiationEngine;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;

import java.io.IOException;
import java.util.HashMap;

import systemManagement.Location;
import systemManagement.SystemManager.ObjectType;
import agents.agvEngine.AGV;
import agents.machineEngine.Machine;

public class MachineContractResponder extends ContractNetResponder {
	private static final long serialVersionUID = 3560413304270027206L;

	public MachineContractResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException,
			FailureException, NotUnderstoodException {

		ACLMessage reply = cfp.createReply();

		reply.setPerformative(ACLMessage.PROPOSE);

		if (myAgent.getClass() == AGV.class) {
			try {
				if (cfp.getContentObject().getClass() == Cfp.class)
					reply = getAgvMessageContent(reply,
							(Cfp) cfp.getContentObject(), (AGV) myAgent);
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;

			}
		} else if (myAgent.getClass() == Machine.class) {
			try {
				reply = getMachineMessageContent(reply,
						(Cfp) cfp.getContentObject());
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return reply;
	}

	private ACLMessage getMachineMessageContent(ACLMessage reply,
			Cfp content) {
		Machine machine = (Machine) myAgent;
		if (machine.isOperationAvailable(content.getProduct()
				.getCurrentOperation())) {
			content.setDestination(myAgent.getAID());
			content.setType(ObjectType.Machine);
			try {
				reply.setContentObject(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent(null);
		}
		return reply;
	}

	private ACLMessage getAgvMessageContent(ACLMessage reply,
			Cfp content, AGV agent) {
		try {
			reply.setContentObject(calculateCosts(content, agent));
		} catch (IOException e) {
			System.err.println(myAgent.getLocalName()
					+ "ERROR: setting reply message content");
			e.printStackTrace();
			return null;
		}
		return reply;
	}

	private Cfp calculateCosts(Cfp content, AGV agent) {
		HashMap<AID, Integer> mapCost = new HashMap<AID, Integer>();
		//TODO improve cost calculation according products in the agv 
		for (DFAgentDescription dfAgent : agent
				.getAgentListWithService("ProcessProduct")) {
			if (dfAgent.getName().getName()
					.compareTo(content.getOrigin().getName()) != 0) {
				content = content.clone();
				content.setDestination(dfAgent.getName());
				content.setType(ObjectType.Agv);
				content.setAgv(myAgent.getAID());
				mapCost.put(
						dfAgent.getName(),
						(int) calculateCost(content.getOrigin().getName(),
								content.getDestination().getName(), agent));
			}
		}

		content.setMachineCostMap(mapCost);
		return content;
	}

	private double calculateCost(String originMachine,
			String destinationMachine, AGV agent) {
		HashMap<String, Location> map = agent.getMachineLocation();
		double cost = (agent.getLocation().distanceTo(map.get(originMachine)) + Location
				.distanceTo(map.get(originMachine), map.get(destinationMachine)))
				* agent.getCost();
		return cost;
	}

	@Override
	protected ACLMessage handleAcceptProposal(ACLMessage cfp,
			ACLMessage propose, ACLMessage accept) throws FailureException {
		// TODO Handle the accept
		System.out.println("proposta aceite:" + propose.getConversationId());
		ACLMessage reply = cfp.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}

	@Override
	protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose,
			ACLMessage reject) {
		//TODO handle reject
		System.out.println(myAgent.getLocalName() + ": Proposta rejeitada: "
				+ propose.getConversationId());
	}
}
