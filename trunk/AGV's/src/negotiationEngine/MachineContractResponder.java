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
//		try {
//			System.out.println("\n" + myAgent.getName() + ": handleCFP\n"
//					+ "CFP received: agent (" + myAgent.getLocalName() + "):"
//					+ cfp.getContentObject().getClass());
//		} catch (UnreadableException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		ACLMessage reply = cfp.createReply();

		reply.setPerformative(ACLMessage.PROPOSE);

		// TODO include the message content
		if (myAgent.getClass() == AGV.class) {
			try {
				if (cfp.getContentObject().getClass() == MachineCFP.class)
					reply = getAgvMessageContent(reply,
							(MachineCFP) cfp.getContentObject(), (AGV) myAgent);
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;

			}
		} else if (myAgent.getClass() == Machine.class) {
			try {
				if (cfp.getContentObject().getClass() == MachineCFP.class)
					reply = getMachineMessageContent(reply,
							(MachineCFP) cfp.getContentObject());
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}

		return reply;
	}

	private ACLMessage getMachineMessageContent(ACLMessage reply,
			MachineCFP content) {
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

	private ACLMessage getAgvMessageContent(ACLMessage reply,
			MachineCFP content, AGV agent) {
		// TODO Auto-generated method stub
		try {
			reply.setContentObject(calculateCosts(content, agent));
		} catch (IOException e) {
			System.err.println(myAgent.getLocalName() + "ERROR: setting reply message content");
			e.printStackTrace();
			return null;
		}
		return reply;
	}

	private MachineCFP calculateCosts(MachineCFP content, AGV agent) {
		HashMap<AID, Integer> mapCost = new HashMap<AID, Integer>();

		for (DFAgentDescription dfAgent : agent
				.getAgentListWithService("ProcessProduct")) {
			if (dfAgent.getName().getName().compareTo(content.getOrigin().getName()) != 0) {
				content = content.clone();
				content.setDestination(dfAgent.getName());
				content.setType(ObjectType.Agv);
				mapCost.put(dfAgent.getName(),
						(int) calculateCost(content.getOrigin().getName(),content.getDestination().getName(), agent));
			}
		}

		content.setMachineCostMap(mapCost);
//		System.out.println(myAgent.getLocalName() + ": costs:");
//		for (AID aid : mapCost.keySet())
//			System.out.println("\t" + aid.getLocalName() + " = "
//					+ mapCost.get(aid));
//		System.out.println(myAgent.getLocalName() + ": ---------------------");
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
