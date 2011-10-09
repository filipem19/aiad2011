package agents;

import jade.core.Agent;

public class AGV extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 883196735452338183L;
	
	private int autonomy;
	private int cost;
	private int locationX;
	private int locationY;
	private int velocity;
	private double currentLoad;
	private double maxLoad;
	private String status;
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
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
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
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
	
	
	
}
