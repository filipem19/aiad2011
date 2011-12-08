package agents.machineEngine;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MachineShutdown extends CyclicBehaviour {

	private static final long serialVersionUID = 8198486440722811616L;

	public MachineShutdown(Machine machine) {
		super(machine);
	}

	@Override
	public void action() {

		ACLMessage msg = myAgent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.INFORM));
		System.out.println(myAgent.getLocalName() + ": msg > " + msg
				+ " -> class = " + myAgent.getClass());

		System.out.println(myAgent.getLocalName() + "message : "
				+ msg.getContent());
		if (msg.getContent().compareTo("TAKE_DOWN") == 0) {
			((Machine) myAgent).shutdownAgent();
			System.out.print("vou-me desligar " + myAgent.getName() + "\n");
		}
		block();
	}

}
