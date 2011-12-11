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

	private HashMap<Cfp, ACLMessage> machineResponse = new HashMap<Cfp, ACLMessage>(),
			agvResponse = new HashMap<Cfp, ACLMessage>();
	private Cfp minimal;

	public ContractInitiator(Agent a, ACLMessage cfp) {
		super(a, cfp);
	}

	@Override
	protected void handlePropose(ACLMessage propose, Vector acceptances) {
		// TODO Auto-generated method stub
		// System.out.println(myAgent.getLocalName() + ": Propose from " +
		// propose.getSender().getLocalName());
	}

	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		System.out.println(myAgent.getLocalName() + ": handle all proposes");
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
					System.out
							.println("-------------------------------------------------------"
									+ msg);
				}
			}
		}
		evaluateProposals();
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println(myAgent.getLocalName() + ": Inform from "
				+ inform.getSender().getLocalName());
	}

	public boolean finnished() {
		return true;
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		System.out.println("------------------------------------------------------failure");
	}

	protected void evaluateProposals() {

		minimal = findMinimalSolution();

		if (minimal != null) {
			ACLMessage reply = agvResponse.get(minimal);
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
			System.out.println(myAgent.getLocalName()
					+ ": after remove machine responses size = "
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
					agv.setDestination(machine.getDestination());
					minimal = agv;
				} else if (agv.getMachineCostMap()
						.get(machine.getDestination()) != null
						&& agv.getMachineCostMap()
								.get(machine.getDestination()) < minimal
								.getMachineCostMap().get(
										machine.getDestination())) {
					agv.setDestination(machine.getDestination());
					minimal = agv;
				}
			}
		}

		return minimal;
	}

	private void rejectUnwantedProposes(Cfp minimal) throws IOException {
		for (Cfp cfp : agvResponse.keySet()) {
			if (cfp != minimal) {
				ACLMessage reply = agvResponse.get(cfp);
				reply = reply.createReply();
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				reply.setContentObject(cfp);
				myAgent.send(reply);
			}
		}

		ACLMessage tmp = agvResponse.get(minimal);
		agvResponse = new HashMap<Cfp, ACLMessage>();
		agvResponse.put(minimal, tmp);

		for (Cfp cfp : machineResponse.keySet()) {
			if (cfp.getDestination() != minimal.getDestination()) {
				ACLMessage reply = machineResponse.get(cfp);
				reply = reply.createReply();
				reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
				reply.setContentObject(cfp);
				myAgent.send(reply);
			} else {
				tmp = machineResponse.get(cfp);
			}
		}
		machineResponse = new HashMap<Cfp, ACLMessage>();
		machineResponse.put(minimal, tmp);

		System.out.println("agvResponse size = " + agvResponse.size());
		System.out.println("machineResponse size = " + machineResponse.size());
	}

}
