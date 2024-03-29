package agents.machineEngine;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import products.Product;

public class MachineInformIfMessageHandler extends CyclicBehaviour {

	private static final long serialVersionUID = 8198486440722811616L;

	private Machine machine;

	public MachineInformIfMessageHandler(Machine machine) {
		super(machine);
		this.machine = machine;
	}

	@Override
	public void action() {

		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF));
		if (msg != null) {
//			System.out.println(myAgent.getLocalName() + ": msg > " + msg
//					+ " -> class = " + myAgent.getClass());
//			System.out.println(myAgent.getLocalName() + ": message content = " + msg.getContent());
			if (msg.getContent() != null && msg.getContent().compareTo("TAKE_DOWN") == 0) {
//				System.out.println(myAgent.getLocalName() + "message (" + msg.getConversationId() + "): "
//						+ msg.getContent());
				((Machine) myAgent).shutdownAgent();
//				System.out.print("vou-me desligar " + myAgent.getName() + "\n");
			} else {
				Product p = null;
				try {
//					System.out.println(myAgent.getLocalName() + ": contenttype " + msg.getContentObject()); 
					p = (Product) msg.getContentObject();
//					System.out.println(myAgent.getLocalName()
//							+ ": messageObjectContent() = " + p);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(myAgent.getLocalName() + ": setting product to " + p);

				machine.setProductAtWork(p);
			}
		}
		block();
	}

}
