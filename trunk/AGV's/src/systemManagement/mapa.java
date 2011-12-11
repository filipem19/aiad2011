package systemManagement;

import jade.core.AID;

import java.awt.*;
import javax.swing.JPanel;
import java.util.HashMap;

public class mapa extends JPanel {
	private static final long serialVersionUID = -1038095695342942324L;
	
	private int agvSize = 10;
	private int machineSize = 20;
	
	private HashMap<AID, Location> AgvsLocs = new HashMap<AID, Location>();
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
    }
    
    public void changeMachineLoc(HashMap<String, Location> machineMap) {
    	for(String aid: machineMap.keySet())
    	{
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
        g2d.setRenderingHint(
           RenderingHints.KEY_ANTIALIASING,                
           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Desenhar os AGVs 
        int locX, locY;
        for(AID chave: AgvsLocs.keySet()) {
        	locX = AgvsLocs.get(chave).getX();
        	locY = AgvsLocs.get(chave).getY();
            g.setColor(Color.MAGENTA);
        	drawAgv(g, locX * this.getWidth() / 100 , locY * this.getHeight() / 100, agvSize);
        	g.setColor(Color.BLACK);
            g.drawString(chave.getLocalName(), locX, locY);       	
        }
        
        // Desenhar as Máquinas
        for(AID chave: MachinesLocs.keySet()) {
        	locX = MachinesLocs.get(chave).getX();
        	locY = MachinesLocs.get(chave).getY();
            g.setColor(Color.RED);
            drawMachine(g, locX * this.getWidth() / 100 , locY * this.getHeight() / 100, machineSize);
        	g.setColor(Color.BLACK);
            g.drawString(chave.getLocalName(), locX, locY);       	
        }
	}
}
