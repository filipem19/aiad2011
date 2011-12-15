package agents.agvEngine;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgvInformIfMessageHandler extends CyclicBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5058967696216064084L;
	private AGV agv;

	public AgvInformIfMessageHandler(AGV agv) {
		super(agv);
		this.agv = agv;
	}
	
	@Override
	public void action() {
		
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF));
		
		if(msg != null){
			if(msg.getContent().compareTo("TAKE_DOWN") == 0){
				agv.shutdownAgent();
//				System.out.print("vou-me desligar " + myAgent.getName() + "\n");
			}
			else if (msg.getContent().compareTo("STATE") == 0) {
				ACLMessage reply = msg.createReply();
				reply.setContent(agv.getState() + "");
				agv.send(reply);
			}
		}
		block();
	}
}
