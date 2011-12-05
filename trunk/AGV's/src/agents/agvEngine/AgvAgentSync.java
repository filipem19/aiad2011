package agents.agvEngine;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.HashMap;

import systemManagement.Location;

public class AgvAgentSync extends CyclicBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4540615536850950225L;
	
	private AGV agv;

	public AgvAgentSync(AGV agv) {
		this.agv = agv;
	}
	
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
		HashMap<AID, Location> object = null;
		if(msg != null){
			try {
				object = (HashMap<AID, Location>) msg.getContentObject();
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			agv.setMachineLocation(object);
		}
		else{
			block();
		}
	}

}
