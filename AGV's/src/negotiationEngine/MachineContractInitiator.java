package negotiationEngine;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

public class MachineContractInitiator extends ContractNetInitiator {

	private static final long serialVersionUID = 4913533665490479921L;

	public MachineContractInitiator(Agent a, ACLMessage cfp) {
		super(a, cfp);
	}

	private HashMap<String, MachineCFP> machineResponse = new HashMap<String, MachineCFP>(),
			agvResponse = new HashMap<String, MachineCFP>();

	@Override
	protected void handleAllResponses(Vector responses, Vector acceptances) {
		System.out.println(myAgent.getLocalName() + " : handle all responses");
		Enumeration e = responses.elements();
		while (e.hasMoreElements()) {
			ACLMessage msg = (ACLMessage) e.nextElement();
			try {
				if (msg.getContentObject() != null) {
					MachineCFP content = (MachineCFP) msg.getContentObject();
					System.out.println(myAgent.getLocalName() + ": " + content);
					switch (content.getType()) {
					case Agv:
						agvResponse.put(msg.getSender().getLocalName(), content);
						break;
					case Machine:
						machineResponse.put(msg.getSender().getLocalName(), content);
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

	public boolean finnished() {
		return true;
	}

	protected int evaluate() {
		return 0;
	}
}
