package agents.agvEngine;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.Serializable;
import java.util.HashMap;

import systemManagement.Location;


public class AgvInformRefHandler extends CyclicBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6172272160087477596L;
	private AGV agv;

	public AgvInformRefHandler(AGV agv) {
		super(agv);
		this.agv = agv;
//		System.out.println(myAgent.getLocalName() + "added behaviour inform ref handler");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF));
//		System.out.println(myAgent.getLocalName() + ": " + msg);
		if(msg != null){
			try {
				Serializable content = msg.getContentObject();
				if(content != null && content.getClass() == HashMap.class){
					HashMap<String, Location> machineLocation = (HashMap<String, Location>) content;
					agv.setMachineMap(machineLocation);
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		block();
	}

}
