package systemManagement.GUI;

import jade.core.AID;

import java.awt.*;
import javax.swing.JPanel;

import systemManagement.Location;

import java.util.HashMap;

public class FactoryMap extends JPanel {
	private static final long serialVersionUID = -1038095695342942324L;

	private int agvSize = 10;
	private int machineSize = 20;
	private static int MAX_X = 100;
	private static int MAX_Y = 100;

	public HashMap<AID, Location> AgvsLocs = new HashMap<AID, Location>();
	private HashMap<AID, Location> MachinesLocs = new HashMap<AID, Location>();

	// Desenho do AGV
	private void drawAgv(Graphics g, int x, int y, int size) {
		g.fillOval(x, y, size, size);
	}

	// Desenho da Maquina
	private void drawMachine(Graphics g, int x, int y, int size) {
		g.fillRect(x, y, size, size);
	}

	public void changeAGVLoc(AID agvAID, Location location) {
		AgvsLocs.put(agvAID, location);
		repaint();
	}

	public void addAgvToMap(AID aid, Location location) {
		AgvsLocs.put(aid, location);
		repaint();
	}

	public void changeMachineLoc(HashMap<String, Location> machineMap) {
//		System.out.print("Vou carregar as máquinas.\n");
		for (String aid : machineMap.keySet()) {
			AID machineAID = new AID(aid, AID.ISLOCALNAME);
			MachinesLocs.put(machineAID, machineMap.get(aid));
		}
		repaint();
	}

	public void removeAGV(AID agvAID) {
		AgvsLocs.remove(agvAID);
	}

	public void removeMachine(AID machineAID) {
		AgvsLocs.remove(machineAID);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int locX, locY;

		// Desenhar as Máquinas
		// System.out.print(MachinesLocs.size() + "\n");
		for (AID chave : MachinesLocs.keySet()) {
			locX = MachinesLocs.get(chave).getX() * this.getWidth() / MAX_X;
			locY = MachinesLocs.get(chave).getY() * this.getWidth() / MAX_Y;
			// System.out.print(locX + " " + locY + "\n");
			g.setColor(Color.RED);
			drawMachine(g, locX, locY, machineSize);
			g.setColor(Color.BLACK);
			g.drawString(chave.getName().split("@")[0], locX, locY);
		}

		// Desenhar os AGVs
		// System.out.print("Vou imprimir.\n");
		if (AgvsLocs == null)
			return;
//		System.out.print(AgvsLocs.size() + "\n");
		for (AID chave : AgvsLocs.keySet()) {
			locX = AgvsLocs.get(chave).getX() * this.getWidth() / MAX_X;
			locY = AgvsLocs.get(chave).getY() * this.getWidth() / MAX_Y;
			// System.out.print(locX + " " + locY + "\n");
			g.setColor(Color.MAGENTA);
			drawAgv(g, locX, locY, agvSize);
			g.setColor(Color.BLACK);
			g.drawString(chave.getName().split("@")[0], locX, locY + agvSize);
		}
	}
}
