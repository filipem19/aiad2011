package systemManagement;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import systemManagement.SystemManagerGUI.ObjectType;

public class SystemManager extends GuiAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4612421677795560441L;

	
	transient protected SystemManagerGUI myGui;  // The gui
	
	@Override
	protected void setup() {
		loadProgramData("ProgramData");
		
		myGui = new SystemManagerGUI();
	}

	/**
	 * Load data from file and creates instances of objects specified in this file
	 * @param filename
	 */
	private void loadProgramData(String filename) {
		try{
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream	in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			ObjectType objectType = null;
			while((strLine = br.readLine()) != null){
				System.out.println(strLine);
				//file decoding and object creation
				objectType = decodeLine(strLine, objectType);
			}
			in.close();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.err.println("error: " + e.getMessage());
			System.out.println("erro na leitura do ficheiro:" + filename);
		}
	}

	private ObjectType decodeLine(String strLine, ObjectType objectType) {
		String[] parts = strLine.split(":");
		ObjectType currentType = null;
		if(parts.length == 1){
			currentType = identifyObjectType(parts[0],currentType);
			if(currentType == null){
				System.err.println("error parsing file : File corrupt");
			}
		}
		else{
			createObject(parts, objectType);
		}
		
		return currentType;
	}

	private void createObject(String[] parts, ObjectType objectType) {
		// TODO Auto-generated method stub
				
	}

	private ObjectType identifyObjectType(String string, ObjectType currentType) {
		
		if(string.compareTo("Machines") == 0)
			return ObjectType.Machine;
		
		if(string.compareTo("Products") == 0)
			return ObjectType.Product;
		
		if(string.compareTo("Operations") == 0)
			return ObjectType.Operation;
	
		return null;
	}

	
	@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
