package agents.agvEngine;

import agents.machineEngine.Machine;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgvShutdown extends CyclicBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5058967696216064084L;

	@Override
	public void action() {
		
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
		if(msg != null && myAgent.getClass() == AGV.class){
			System.out.print("entrei \n");
			if(msg.getContent().compareTo("TAKE_DOWN") == 0){
				((AGV)myAgent).shutdownAgent();
				System.out.print("vou-me desligar " + myAgent.getName() + "\n");
			}
		} else {
			System.out.print("else \n");
			block();
		}
	}
}
