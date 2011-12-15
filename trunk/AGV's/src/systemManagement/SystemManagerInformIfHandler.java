package systemManagement;

import java.io.IOException;
import java.io.Serializable;

import products.Product;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class SystemManagerInformIfHandler extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6104164352894958372L;

	private SystemManager mySysManager;

	public SystemManagerInformIfHandler(SystemManager mySysManager) {
		super(mySysManager);
		this.mySysManager = mySysManager;
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate
				.MatchPerformative(ACLMessage.INFORM_IF));
		if (msg != null) {
			Serializable content = null;
			try {
				content = msg.getContentObject();
			} catch (UnreadableException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}

			if (content != null) {
				try {
					if (msg.getContentObject().getClass() == Location.class) {
						Location location = null;
						location = (Location) msg.getContentObject();
						mySysManager.sendAgvLocation(msg.getSender(), location);
					}

				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (content == null) {
				// mensagem com nome do agente a receber o produto e o
				// produto
//				System.out.println(myAgent.getLocalName() + ": content: "
//						+ msg.getContent());
				String[] parts = msg.getContent().split(" ");
				if(msg.getContent()!= null && parts[0].compareTo("RefreshMessage") == 0){
					mySysManager.getControlGUI().setMessageFieldContent(msg.getContent().split("RefreshMessage")[1]);
				}
				else if (parts.length == 2) {
					setMachineProduct(parts);
				}

			}

		}
		block();
	}

	private void setMachineProduct(String[] parts) {
		DFAgentDescription[] agents = mySysManager
				.getAgentListWithService("ProcessProduct");

		ACLMessage msgtomachine = new ACLMessage(ACLMessage.INFORM_IF);

		for (DFAgentDescription agent : agents) {
			if (agent.getName().getLocalName().compareTo(parts[0]) == 0) {
				msgtomachine.addReceiver(agent.getName());
			}
		}
		try {
			Product p = mySysManager.getExistingProducts().get(parts[1]);
			msgtomachine.setContentObject(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("ERROR - adding product to msg content");
			e.printStackTrace();
		}
		myAgent.send(msgtomachine);

	}

}
