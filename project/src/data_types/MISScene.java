package data_types;

import java.util.ArrayList;

public class MISScene {

	//This is set when the scene is created
	public int IDNumber;
	public ArrayList<MISNode> nodeList;
	public String name;
	public int loadSteps;
	public int format;
	
	//indentification number
	//an array of nodes
	
	//a set of general rules
	//a list of broadcasts with timer
	//a successcriteria, which when something happens, do something
	
	public MISScene(int id){
		this.IDNumber = id;
		nodeList = new ArrayList<MISNode>();
	}
	
	public void addNode(MISNode node){
		nodeList.add(node);
	}
	
	public void addRule(){
		
	}
	
	public void addBroadcast(){
		
	}
	
	public void addSuccessCriteria(){
		
	}
	
	public void addMessageOnAction(){
		
	}
	
	
}
