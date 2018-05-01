package data_types;

import java.util.ArrayList;

import broadcasts.MISBroadcast;
import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastMessage;
import broadcasts.MISBroadcastValue;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISSprite;
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
	
	public MISScene createDeepCopy(){
		MISScene newScene = new MISScene();
		newScene.format = this.format;
		newScene.IDNumber = this.IDNumber;
		newScene.name = this.name;
		newScene.loadSteps = this.loadSteps;
		//add broadcasts
		for(int i = 0; i < this.broadcasts.size(); i++){
			MISBroadcast broadcast = null;
			if(this.broadcasts.get(i) instanceof MISBroadcastValue){
				MISBroadcastValue old = (MISBroadcastValue)this.broadcasts.get(i);
				broadcast = new MISBroadcastValue(old.getBroadcastName(), old.timesPerMinute);
			} else if(this.broadcasts.get(i) instanceof MISBroadcastData){
				MISBroadcastData old = (MISBroadcastData)this.broadcasts.get(i);
				broadcast = new MISBroadcastData(old.getBroadcastName(), old.timesPerMinute);
			} else if(this.broadcasts.get(i) instanceof MISBroadcastMessage){
				MISBroadcastMessage old = (MISBroadcastMessage)this.broadcasts.get(i);
				broadcast = new MISBroadcastMessage(old.getBroadcastName(), old.timesPerMinute, old.message);
			}
			if(broadcast == null){
				System.err.println("Error making a deepcopy of the scene");
			}
			newScene.broadcasts.add(broadcast);
		}
		//add nodes
		for(int i = 0; i < this.nodeList.size(); i++){
			MISNode node = null;
			if(this.nodeList.get(i) instanceof MISNode2D){
				MISNode2D old = (MISNode2D) this.nodeList.get(i);
				node = new MISNode2D(new MIS2DTransform(old.transform.positionX, old.transform.positionY, old.transform.rotation, old.transform.scaleX, old.transform.scaleY));
			} else if(this.nodeList.get(i) instanceof MISSprite){
				MISSprite old = (MISSprite) this.nodeList.get(i);
				node = new MISSprite(new MIS2DTransform(old.transform.positionX, old.transform.positionY, old.transform.rotation, old.transform.scaleX, old.transform.scaleY), old.textureId);
			} else if(this.nodeList.get(i) instanceof MISNode){
				node = new MISNode();
			}
			MISNode old = this.nodeList.get(i);
			node.index = old.index;
			node.informationReceivers = old.informationReceivers;
			node.name = old.name;
			node.parent = old.parent;
			node.scriptAttached = old.scriptAttached;
			node.scriptId = old.scriptId;
			node.scriptName = old.scriptName;
			node.type = old.type;
			node.shouldSendInformation = old.shouldSendInformation;
			newScene.nodeList.add(node);
		}
		//add rules
		for(int i = 0; i < this.ruleList.size(); i++){
			
		}
		return newScene;
		
	}
	
	
}
