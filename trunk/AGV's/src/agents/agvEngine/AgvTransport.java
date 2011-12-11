package agents.agvEngine;

import java.io.IOException;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import negotiationEngine.Cfp;
import systemManagement.Location;

public class AgvTransport extends SimpleBehaviour{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1976108898851894452L;

	private AGV myAgv;
	private Cfp cfp;
	
	public AgvTransport(AGV agv, Cfp cfp) {
		super(agv);
		this.myAgv = agv;
		this.cfp = cfp;
	}
	
	@Override
	public void action() {
		Location location = myAgv.getMachineLocation(cfp.getOrigin().getName());
		System.out.println(myAgent.getLocalName() + ": origin (" + cfp.getOrigin().getLocalName() + ") location = " + location);
		int timeToWait = ((int)myAgv.getLocation().distanceTo(location)/myAgv.getVelocity());
		
		//TRAVEL time to cfp origin
//		myAgent.doWait(timeToWait*1000);
		//TODO get Product from Machine
		removeProductAtOriginMachine();

		myAgv.setLocation(location);
		location = myAgv.getMachineLocation(cfp.getDestination().getName());
		timeToWait = ((int)myAgv.getLocation().distanceTo(location)/myAgv.getVelocity());

		//TRAVEL time to cfp destination
		//set location destination

//		myAgent.doWait(timeToWait*1000);
		//TODO set Product to destination Machine
		setProductAtDestinationMachine();
		myAgent.doWait(3000);
		myAgent.removeBehaviour(this);

	}

	private void setProductAtDestinationMachine() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
		msg.addReceiver(cfp.getOrigin());
		try {
			msg.setContentObject(cfp.getProduct());
			myAgent.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	private void removeProductAtOriginMachine() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
		msg.addReceiver(cfp.getOrigin());
		myAgent.send(msg);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
