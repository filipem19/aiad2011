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
		// Obter posição da machine de destino
		int destX = location.getX(); //machineLocation;
		int destY = location.getY(); //machineLocation;
		
		// Posição de inicio do AGV.
		double locX, locY;
		locX = location.getX();
		locY = location.getY();
		
		double stepSize = Math.sqrt(Math.pow(Math.abs(locX - destX), 2) + Math.pow(Math.abs(locY - destY), 2))/ velocity;
		while(locY != destY) {		
			if(locX != destX) {
				if(locX < destX) { // Direção positiva do X.
					if(Math.abs(locX - destX) < stepSize) { // Podemos dar o passo inteiro.
						locX += stepSize;
					}
					else { // Não podemos dar passo inteiro
						if (locY < destY) {	// Dar o resto do passo no Y. NOTA: Tem de ser antes de mudar o locX.
							locY += stepSize - (locX - destX);	// Y no sentido positivo.
						}
						else {
							locY -= stepSize - (locX - destX);	// Y no sentido negativo.
						}
						locX = destX;	// Colocar o X no destino
					}
				}
				else {	// Direção negativa do X.
					if(Math.abs(locX - destX) < stepSize) { // Podemos dar o passo inteiro.
						locX -= stepSize;
					}
					else { // Não podemos dar passo inteiro.
						if (locY < destY) {	// Dar o resto do passo no Y. NOTA: Tem de ser antes de mudar o locX.
							locY += stepSize - (locX - destX);	// Y no sentido positivo.
						}
						else {
							locY -= stepSize - (locX - destX);	// Y no sentido negativo.
						}
						locX = destX;	// Colocar o X no destino
					}
				}
				location.setX((int)locX);
				location.setY((int)locY);
			}
			else{	// Já percorremos todo X, vamos percorrer Y.
				if(locY < destY) { // Direção positiva do Y.
					if(Math.abs(locY - destY) < stepSize) { // Podemos dar o passo inteiro.
						locY += stepSize;
					}
					else { // Não podemos dar passo inteiro
						locY = destY;	// Colocar o Y no destino
					}
				}
				else {	// Direção negativa do Y.
					if(Math.abs(locY - destY) < stepSize) { // Podemos dar o passo inteiro.
						locY -= stepSize;
					}
					else { // Não podemos dar passo inteiro.
						locY = destY;	// Colocar o Y no destino	
					}
				}
				location.setY((int)locY);
			}		
			try {
				// Avisar que o mapa de que se moveu.
				informLocation(location);
				Thread.sleep(500);	// Aguardar 500 milissegundos para simular movimento real.
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}	// Fim do while
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
	
	public Location getmachiLocation (String machineLocalName){
		return machineLocation.get(machineLocalName);
	}
	
	
}
