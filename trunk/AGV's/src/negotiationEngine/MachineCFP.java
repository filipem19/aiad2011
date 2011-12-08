package negotiationEngine;

import jade.core.AID;

import java.io.Serializable;
import java.util.HashMap;

import products.Product;
import systemManagement.SystemManager;

public class MachineCFP implements Serializable {

	private static final long serialVersionUID = -2312982920303681093L;

	private Product product;
	private AID origin, destination;
	private HashMap<AID, Integer> machineCostMap;

	private SystemManager.ObjectType type;

	public MachineCFP(Product product, AID origin) {
		// TODO Auto-generated constructor stub
		this.product = product;
		this.origin = origin;
	}

	public Product getProduct() {
		return product;
	}

	public SystemManager.ObjectType getType() {
		return type;
	}

	public void setType(SystemManager.ObjectType type) {
		this.type = type;
	}

	public AID getOrigin() {
		return origin;
	}

	public AID getDestination() {
		return destination;
	}

	public void setDestination(AID destination) {
		this.destination = destination;
	}

	public HashMap<AID, Integer> getMachineCostMap() {
		return machineCostMap;
	}

	public void setMachineCostMap(HashMap<AID, Integer> machineCostMap) {
		this.machineCostMap = machineCostMap;
	}

	@Override
	public String toString() {
		if (machineCostMap != null)
			return "origin = " + getOrigin().getLocalName() 
				+ " objecttype = " + getType() 
				+ " costs: " + getMachineCostMap().keySet();
		else
			return "origin = " + getOrigin().getLocalName() + " destination: "
			+ getDestination().getLocalName() + " objecttype = "
			+ getType();
	}

	protected MachineCFP clone() {
		MachineCFP mcfp = new MachineCFP(this.getProduct(), this.getOrigin());
		mcfp.setDestination(this.getDestination());
		mcfp.setType(this.getType());
		mcfp.setMachineCostMap(this.getMachineCostMap());
		return mcfp;
	}

}
