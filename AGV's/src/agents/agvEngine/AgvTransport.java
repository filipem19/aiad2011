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
		int timeToWait = (int) (myAgv.getLocation().distanceTo(location)/(myAgv.getVelocity()/10));
		
		//TRAVEL time to cfp origin
		System.out.println("w8 = " + timeToWait*110);
		myAgent.doWait(Math.abs(timeToWait*110));
		System.out.println("origin location " + location);
		myAgv.setLocation(location);
		removeProductAtOriginMachine();
		
		location = myAgv.getMachineLocation(cfp.getDestination().getName());
		timeToWait = (int) (myAgv.getLocation().distanceTo(location)/myAgv.getVelocity());

		//TRAVEL time to cfp destination
		//set location destination

		System.out.println("w8 = " + timeToWait*110);
		myAgent.doWait(Math.abs(timeToWait*110));
		System.out.println("destination location " + location);
		myAgv.setLocation(location);
		setProductAtDestinationMachine();
		myAgent.removeBehaviour(this);

	}

	private void setProductAtDestinationMachine() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
		msg.addReceiver(cfp.getDestination());
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
