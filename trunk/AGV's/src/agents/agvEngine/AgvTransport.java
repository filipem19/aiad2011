package agents.agvEngine;

import jade.core.behaviours.SimpleBehaviour;
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
		this.myAgent = agv;
		this.cfp = cfp;
	}
	
	@Override
	public void action() {
		Location location = myAgv.getmachiLocation(cfp.getOrigin().getName());
		System.out.println("origin (" + cfp.getOrigin().getLocalName() + ") location = " + location);
		int timeToWait = ((int)myAgv.getLocation().distanceTo(location)/myAgv.getVelocity());
		//TODO get Product from Machine
		//TRAVEL time to cfp origin
		myAgent.doWait(timeToWait);
		myAgv.setLocation(location);
		location = myAgv.getmachiLocation(cfp.getDestination().getName());
		timeToWait = ((int)myAgv.getLocation().distanceTo(location)/myAgv.getVelocity());

		//TRAVEL time to cfp destination
		//set location destination

		myAgent.doWait(timeToWait);
		//TODO set Product to destination Machine

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
