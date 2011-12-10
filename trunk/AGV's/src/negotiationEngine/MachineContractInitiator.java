package negotiationEngine;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

public class MachineContractInitiator extends ContractNetInitiator {

	private static final long serialVersionUID = 4913533665490479921L;

	public MachineContractInitiator(Agent a, ACLMessage cfp) {
		super(a, cfp);
	}

	private HashMap<Cfp, ACLMessage> machineResponse = new HashMap<Cfp, ACLMessage>(),
			agvResponse = new HashMap<Cfp, ACLMessage>();

	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		System.out.println(myAgent.getLocalName() + " : handle all responses");
		Enumeration e = responses.elements();
		System.out.println(myAgent.getLocalName() + ": responses size = "
				+ responses.size() + " acceptances size = "
				+ acceptances.size());
		while (e.hasMoreElements()) {

			ACLMessage msg = (ACLMessage) e.nextElement();
			System.out.print(msg.getSender().getLocalName() + " ");
			if (msg.getPerformative() == ACLMessage.REFUSE)
				continue;
			if (msg.getPerformative() == ACLMessage.PROPOSE) {
				try {
					if (msg.getContentObject() != null) {
						Cfp content = (Cfp) msg
								.getContentObject();
						// System.out.println(myAgent.getLocalName()
						// + ": message content type" + content.getType());
						switch (content.getType()) {
						case Agv:
							agvResponse.put(content, msg);
							break;
						case Machine:
							machineResponse.put(content, msg);
							break;
						default:
							System.err.println("Error: bad message received");
							break;
						}
					}
				} catch (UnreadableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		evaluateProposals();
	}

	public boolean finnished() {
		return true;
	}

	protected void evaluateProposals() {
		System.out.println(myAgent.getLocalName() + " EVALUATE");
		Cfp minimal = null;
		for (Cfp machine : machineResponse.keySet()) {
			for (Cfp agv : agvResponse.keySet()) {
				if (minimal == null
						&& agv.getMachineCostMap()
								.get(machine.getDestination()) != null) {
					minimal = agv;
				} else if (agv.getMachineCostMap()
						.get(machine.getDestination()) != null
						&& agv.getMachineCostMap()
								.get(machine.getDestination()) < minimal
								.getMachineCostMap().get(
										machine.getDestination())) {
					minimal = agv;
				}
			}
		}

		if (minimal != null) {
			ACLMessage reply = agvResponse.remove(minimal);
			reply = reply.createReply();
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			try {
				reply.setContentObject(minimal);
				myAgent.send(reply);
				rejectUnwantedProposes(minimal);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			// TODO Handle no proposals available
		}
	}

	private void rejectUnwantedProposes(Cfp minimal) throws IOException {
		for(Cfp cfp: agvResponse.keySet()){
			ACLMessage reply = agvResponse.get(cfp);
			reply = reply.createReply();
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			reply.setContentObject(cfp);
			myAgent.send(reply);
		}
		
		for(Cfp cfp: machineResponse.keySet()){
			if(cfp.getDestination() != minimal.getDestination()){
				ACLMessage reply = machineResponse.get(cfp);
				reply = reply.createReply();
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				reply.setContentObject(cfp);
				myAgent.send(reply);
			}
		}
		System.out.println("agvResponse size = " + agvResponse.size()); 
		System.out.println("machineResponse size = " + machineResponse.size());
	}
}
