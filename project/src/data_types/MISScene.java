package data_types;

import java.util.ArrayList;

import broadcasts.MISBroadcast;
import rules.MISRule;

public class MISScene {

	//This is set when the scene is created
	public int IDNumber;
	public ArrayList<MISNode> nodeList;
	public ArrayList<MISRule> ruleList;
	public String name;
	public int loadSteps;
	public int format;
	public ArrayList<MISExternalResource> externalResources;
	public MISRoomSettings roomSettings;
	
	
	private static int globalID = 0;
	
	//indentification number
	//an array of nodes
	
	//a set of general rules
	//a list of broadcasts with timer
	public ArrayList<MISBroadcast> broadcasts;
	//a successcriteria, which when something happens, do something
	
	public MISScene(){
		this.IDNumber = globalID;
		globalID++;
		nodeList = new ArrayList<MISNode>();
		externalResources = new ArrayList<MISExternalResource>();
		broadcasts = new ArrayList<MISBroadcast>();
		ruleList = new ArrayList<MISRule>();
	}
	
	public MISScene(int id){
		this.IDNumber = id;
		globalID = id + 1;
		nodeList = new ArrayList<MISNode>();
		externalResources = new ArrayList<MISExternalResource>();
		broadcasts = new ArrayList<MISBroadcast>();
		ruleList = new ArrayList<MISRule>();
	}
	
	
	public void addNode(MISNode node){
		node.index = nodeList.size();
		nodeList.add(node);
	}
	
	public void addExternalResource(MISExternalResource resource){
		boolean exists = false;
		for(MISExternalResource extRes : externalResources){
			if(extRes.path.equals(resource.path)){
				if(extRes.id == resource.id){
					exists = true;
				}
			}
		}
		if(!exists){
			externalResources.add(resource);
		}
	}
	
	public void addRule(MISRule rule){
		ruleList.add(rule);
	}
	
	public void addBroadcast(MISBroadcast broadcast){
		broadcasts.add(broadcast);
	}
	
	public void addSuccessCriteria(){
		
	}
	
	public void addMessageOnAction(){
		
	}
	
	
}
