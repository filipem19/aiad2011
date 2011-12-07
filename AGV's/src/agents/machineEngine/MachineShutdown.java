package agents.machineEngine;

import agents.agvEngine.AGV;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MachineShutdown extends CyclicBehaviour{

		/**
	 * 
	 */
	private static final long serialVersionUID = 8198486440722811616L;


		@Override
		public void action() {
					
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
			if(msg != null && myAgent.getClass() == Machine.class){
				
				if(msg.getContent().compareTo("TAKE_DOWN") == 0){
					((Machine)myAgent).shutdownAgent();
					System.out.print("vou-me desligar " + myAgent.getName() + "\n");
				}
			} else {
				block();
			}
		}
}
