package systemManagement;

import javax.swing.JFrame;

public class SystemManagerGUI extends JFrame{

	public SystemManagerGUI() {
		initializeWindowPreferences();
	}
	
	private void initializeWindowPreferences(){
		setTitle("Initial platform");
		setVisible(true);
		setSize(200, 200);
		setLocation(200, 400);
	}
}
