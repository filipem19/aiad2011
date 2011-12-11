package agents.agvEngine;

import jade.core.Agent;
import jade.domain.DFDBKB;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.HashMap;

import negotiationEngine.ContractResponder;
import systemManagement.Location;

public class AGV extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 883196735452338183L;

	private int autonomy;
	private int cost;
	private Location location;
	
	private int velocity;
	private double currentLoad;
	private double maxLoad;
	private String status, agvName;

	private HashMap<String, Location> machineLocation = new HashMap<String, Location>();

	public void setAgvProperties(int autonomy, int cost, int locationX,
			int locationY, int velocity, double maxLoad, String status,
			String agvName) {
		this.autonomy = autonomy;
		this.cost = cost;
		location = new Location(locationX, locationY);
		this.velocity = velocity;
		this.currentLoad = 0;
		this.maxLoad = maxLoad;
		this.status = status;
		this.agvName = agvName;
		// System.out.println("\nCreated => " + toString());
	}

	@Override
	protected void setup() {
		Object[] args = getArguments();

		if (args.length < 8) {
			System.out.println("takedown agent: " + getAgvName());
			this.takeDown();
		} else {
			setAgvProperties((Integer) args[0], (Integer) args[1],
					(Integer) args[2], (Integer) args[3], (Integer) args[4],
					(Double) args[5], (String) args[6], (String) args[7]);

			registerAgentAtDF("Transport:" + getAgvName(), "Transport");

			initializeAgvContractResponder();

			addBehaviour(new AgvInformIfMessageHandler(this));
			addBehaviour(new AgvInformRefHandler(this));
		}
	}

	protected void shutdownAgent() {
		doDelete();
	}

	/**
	 * initializes contract responder for product transport proposals from
	 * machines
	 */
	private void initializeAgvContractResponder() {
		MessageTemplate template = MessageTemplate
				.and(MessageTemplate
						.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
						MessageTemplate.MatchPerformative(ACLMessage.CFP));

		addBehaviour(new ContractResponder(this, template));

	}

	/**
	 * Register a service with the given parameters at DF agent
	 * 
	 * @param serviceName
	 * @param serviceType
	 */
	private void registerAgentAtDF(String serviceName, String serviceType) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription service = new ServiceDescription();
		service.setName(serviceName);
		service.setType(serviceType);
		dfd.addServices(service);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method will deregister the previous services that have been
	 * registered in DFService
	 */
	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getAutonomy() {
		return autonomy;
	}

	public void setAutonomy(int autonomy) {
		this.autonomy = autonomy;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getLocationX() {
		return location.getX();
	}

	public void setLocationX(int locationX) {
		location.setX(locationX);
	}

	public int getLocationY() {
		return location.getY();
	}

	public void setLocationY(int locationY) {
		location.setY(locationY);
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public double getCurrentLoad() {
		return currentLoad;
	}

	public void setCurrentLoad(double currentLoad) {
		this.currentLoad = currentLoad;
	}

	public double getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(double maxLoad) {
		this.maxLoad = maxLoad;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAgvName() {
		return this.agvName;
	}

	public Location getLocation() {
		return location;
	}
	
	public void informLocation(Location location) {		
		//lookup for DFSERVICE(system management)
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM_IF);
		for(DFAgentDescription agent : getAgentListWithService("SystemManagement")){
			msg.addReceiver(agent.getName());
		}
		try {
			msg.setContentObject(getLocation());
			send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	public void setLocation(Location location) {
		this.location = location;
		informLocation(location);
	}

	@Override
	public String toString() {
		return "AGV (" + getAgvName() + "): \n\tautonomy = " + autonomy
				+ "\tcost = " + cost + "\tlocationX = " + location.getX()
				+ "\tlocationY = " + location.getY() + "\tvelocity = " + velocity
				+ "\tmaxLoad = " + maxLoad + "\tstatus = " + status;
	}

	public DFAgentDescription[] getAgentListWithService(String serviceName) {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(serviceName);
		dfd.addServices(sd);
		DFAgentDescription[] agents = null;
		try {
			agents = DFService.search(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return agents;
	}

	public HashMap<String, Location> getMachineLocation() {
		return machineLocation;
	}

	public void setMachineMap(HashMap<String, Location> machineLocation) {
		this.machineLocation = machineLocation;

	}
	
	public Location getMachineLocation (String machineLocalName){
//		for(String s: machineLocation.keySet())
//			System.out.println("machineLocation for = " + s + " machinelocalname = " + machineLocalName);
		return machineLocation.get(machineLocalName);
	}
	
	
}
