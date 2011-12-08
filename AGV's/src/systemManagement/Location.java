package systemManagement;

import java.io.Serializable;

public class Location implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1711058280970499010L;
	
	private int x,y;
	
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public double distanceTo(Location destination){
		return Math.sqrt(Math.pow((destination.getX() - this.getX()),2) + Math.pow(destination.getY() - this.getY(),2));
	}
	
	public static double distanceTo(Location origin ,Location destination){
		return Math.sqrt(Math.pow((destination.getX() - origin.getX()),2) + Math.pow(destination.getY() - origin.getY(),2));
	}
	
}
