package data_types;

import java.util.ArrayList;

import rules.MISRule;
import scene.MISBroadcast;

public class MISScene {

	//This is set when the scene is created
	public int IDNumber;
	public ArrayList<MISNode> nodeList;
	public ArrayList<MISRule> ruleList;
	public String name;
	public int loadSteps;
	public int format;
	public ArrayList<MISExternalResource> externalResources;
	
	//indentification number
	//an array of nodes
	
	//a set of general rules
	//a list of broadcasts with timer
	public ArrayList<MISBroadcast> broadcasts;
	//a successcriteria, which when something happens, do something
	
	public MISScene(int id){
		this.IDNumber = id;
		nodeList = new ArrayList<MISNode>();
		externalResources = new ArrayList<MISExternalResource>();
		broadcasts = new ArrayList<MISBroadcast>();
		ruleList = new ArrayList<MISRule>();
	}
	
	public void addNode(MISNode node){
		boolean exists = false;
		for(MISNode tempNode : nodeList){
			if(tempNode.name.equals(node.name)){
				if(tempNode.index == node.index){
					if(tempNode.type.equals(node.type)){
						exists = true;
					}
				}
			}
		}
		if(!exists){
			nodeList.add(node);
		}
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
	
	public void addRule(){
		
	}
	
	public void addBroadcast(MISBroadcast broadcast){
		broadcasts.add(broadcast);
	}
	
	public void addSuccessCriteria(){
		
	}
	
	public void addMessageOnAction(){
		
	}
	
	
}
