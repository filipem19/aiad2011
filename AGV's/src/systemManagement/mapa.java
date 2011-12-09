package systemManagement;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.JPanel;

public class mapa extends JPanel {
	private static final long serialVersionUID = -1038095695342942324L;
	private Ellipse2D.Double circle = new Ellipse2D.Double(10, 10, 350, 350);
	private Rectangle2D.Double square = new Rectangle2D.Double(10, 10, 350, 350);
	
	@Override
	public void paintComponent(Graphics g) {
		clear(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.fill(circle);
		g2d.draw(square);
	}

	protected void clear(Graphics g) {
		super.paintComponent(g);
	}
	
	protected Ellipse2D.Double getCircle() {
		return(circle);
	}
}
