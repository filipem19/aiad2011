package agents.machineEngine;

import java.io.IOException;

import systemManagement.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MachineQueryHandler extends CyclicBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6172272160087477596L;
	private Machine machine;

	public MachineQueryHandler(Machine machine) {
		super(machine);
		this.machine = machine;
	}
	
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF));
		if(msg != null && msg.getContent().compareTo("Location") == 0){
			ACLMessage reply = msg.createReply();
			try {
				reply.setContentObject(new Location(machine.getLocationX(), machine.getLocationY()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAgent.send(reply);
		}
		
	}

}
