package negotiationEngine;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

public class ContractInitiator extends ContractNetInitiator {

	private static final long serialVersionUID = 4913533665490479921L;

	public ContractInitiator(Agent a, ACLMessage cfp) {
		super(a, cfp);
		myAgent.doWait(cfp.getReplyByDate().getTime()
				- System.currentTimeMillis());
	}

	private HashMap<Cfp, ACLMessage> machineResponse = new HashMap<Cfp, ACLMessage>(),
			agvResponse = new HashMap<Cfp, ACLMessage>();

	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {
		// TODO Auto-generated method stub
		// System.out.println(myAgent.getLocalName() + ": Propose from " +
		// propose.getSender().getLocalName());
	}

	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		Enumeration e = responses.elements();
		while (e.hasMoreElements()) {

			ACLMessage msg = (ACLMessage) e.nextElement();
			if (msg.getPerformative() == ACLMessage.REFUSE) {
				continue;
			} else {
				if (msg.getPerformative() == ACLMessage.PROPOSE) {
					try {
						if (msg.getContentObject() != null) {
							Cfp content = (Cfp) msg.getContentObject();
							switch (content.getType()) {
							case Agv:
								agvResponse.put(content, msg);
								break;
							case Machine:
								machineResponse.put(content, msg);
								break;
							default:
								System.err
										.println("Error: bad message received");
								break;
							}
						}
					} catch (UnreadableException e1) {
						System.err.println(myAgent.getLocalName()
								+ ": ERROR - Can't load propose content");
						e1.printStackTrace();
					}
				} else {

				}
			}
		}
		evaluateProposals();
	}
	
	public boolean finnished() {
		return true;
	}

	protected void evaluateProposals() {

		Cfp minimal = findMinimalSolution();

		if (minimal != null) {
			ACLMessage reply = agvResponse.remove(minimal);
			reply = reply.createReply();
			reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			try {
				System.out.println(myAgent.getLocalName()
						+ "minimal -> Origin = "
						+ minimal.getOrigin().getLocalName()
						+ "; Destination =  "
						+ minimal.getDestination().getLocalName() + "; AGV = "
						+ minimal.getAgv().getLocalName());
				rejectUnwantedProposes(minimal);
				// TODO send Acceptances to proposals
				reply.setContentObject(minimal);
				reply.addReceiver(minimal.getDestination());
				myAgent.send(reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			System.out.println(myAgent.getLocalName() + ": machine responses size = "
					+ machineResponse.size() + " agv responses size = "
					+ agvResponse.size());
		} else {
			// TODO Handle no proposals available
		}
	}

	private Cfp findMinimalSolution() {

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

		return minimal;
	}

	private void rejectUnwantedProposes(Cfp minimal) throws IOException {
		for (Cfp cfp : agvResponse.keySet()) {
			System.out.println(myAgent.getLocalName() + ": agv " + cfp.getAgv().getLocalName());
			ACLMessage reply = agvResponse.get(cfp);
			reply = reply.createReply();
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			reply.setContentObject(cfp);
			myAgent.send(reply);
		}

		for (Cfp cfp : machineResponse.keySet()) {
			if (cfp.getDestination() != minimal.getDestination()) {
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
