package agents.machineEngine;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MachineShutdown extends CyclicBehaviour{

<<<<<<< .mine
	private static final long serialVersionUID = 8198486440722811616L;
=======
		/**
	 * 
	 */
	private static final long serialVersionUID = 8198486440722811616L;
>>>>>>> .r30

<<<<<<< .mine
	public MachineShutdown(Machine machine) {
		super(machine);
	}
	
=======

>>>>>>> .r30
		@Override
		public void action() {
<<<<<<< .mine
			
=======
					
>>>>>>> .r30
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		
			if(msg != null && myAgent.getClass() == Machine.class){
				
				if(msg.getContent().compareTo("TAKE_DOWN") == 0){
					((Machine)myAgent).shutdownAgent();
					System.out.print("vou-me desligar " + myAgent.getName() + "\n");
				}
			}
			block();
		}
}
