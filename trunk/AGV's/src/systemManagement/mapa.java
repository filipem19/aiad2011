package systemManagement;

import java.awt.*;
import javax.swing.JPanel;

public class mapa extends JPanel {
	private static final long serialVersionUID = -1038095695342942324L;
	
	private int agvSize = 10;
	private int machineSize = 20;
	public int[][] AgvLoc;
	public int[][] MachineLoc;

    public mapa(int[][] agvLoc, int[][] machineLoc) {
		AgvLoc = agvLoc;
		MachineLoc = machineLoc;
	}

	// Desenho do AGV
    private void drawAgv(Graphics g, int x, int y, int size) {
    	g.fillOval(x, y, size, size);
    }
	
    // Desenho da Maquina
    private void drawMachine(Graphics g, int x, int y, int size) {
    	g.fillRect(x, y, size, size);
    }
 
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
           RenderingHints.KEY_ANTIALIASING,                
           RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
      
        // Desenhar os AGVs 
        g.setColor(Color.MAGENTA);
    	for (int i = 0; i <= AgvLoc.length / 2; i++) {
        	drawAgv(g, AgvLoc[i][0] * this.getWidth() / 100 , AgvLoc[i][1] * this.getHeight() / 100, agvSize);
    	}
        
        // Desenhar as Máquinas
        g.setColor(Color.RED);
    	for (int i = 0; i <= MachineLoc.length / 2; i++) {
    		drawMachine(g, MachineLoc[i][0] * this.getWidth() / 100 , MachineLoc[i][1] * this.getHeight() / 100, machineSize);
    	}
    }
}
