package data_types;

import java.util.ArrayList;
import java.util.List;

import broadcasts.MISBroadcast;
import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastMessage;
import broadcasts.MISBroadcastValue;
import nodes.MISControl;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISNodeScene;
import nodes.MISSpatial;
import project.MISProject;
import rules.MISRule;
import rules.MISRuleNodePosition;
import rules.MISRuleNodeRotation;
import rules.MISRuleNodeScale;
import triggers.MISTrigger;
import triggers.MISTriggerValue;

public class MISScene {

	//This is set when the scene is created
	public int IDNumber;
	public ArrayList<MISNode> nodeList;
	public ArrayList<MISRule> ruleList;
	public String name;
	public int loadSteps;
	public int format;
	public ArrayList<MISExternalResource> externalResources;
	public String path;
	
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
			MISNode node = createDeepCopyOfNode(this.nodeList.get(i));
			newScene.nodeList.add(node);
		}
		//add rules
		for(int i = 0; i < this.ruleList.size(); i++){
			MISRule rule = null;
			if(this.ruleList.get(i) instanceof MISRuleNodePosition){
				MISRuleNodePosition old = (MISRuleNodePosition)this.ruleList.get(i);
				MISRuleNodePosition tempRule = new MISRuleNodePosition(old.ruleName, old.node, old.option);
				tempRule.xBounds = new MISBounds(old.xBounds.min, old.xBounds.max);
				tempRule.yBounds = new MISBounds(old.yBounds.min, old.yBounds.max);
				tempRule.zBounds = new MISBounds(old.zBounds.min, old.zBounds.max);
				rule = tempRule;
			} else if(this.ruleList.get(i) instanceof MISRuleNodeRotation){
				MISRuleNodeRotation old = (MISRuleNodeRotation)this.ruleList.get(i);
				MISRuleNodeRotation tempRule = new MISRuleNodeRotation(old.ruleName, old.node, old.option);
				tempRule.rotationBounds = new MISBounds(old.rotationBounds.min, old.rotationBounds.max);
				rule = tempRule;
			} else if(this.ruleList.get(i) instanceof MISRuleNodeScale){
				MISRuleNodeScale old = (MISRuleNodeScale)this.ruleList.get(i);
				MISRuleNodeScale tempRule = new MISRuleNodeScale(old.ruleName, old.node, old.option);
				tempRule.xBounds = new MISBounds(old.xBounds.min, old.xBounds.max);
				tempRule.yBounds = new MISBounds(old.yBounds.min, old.yBounds.max);
				rule = tempRule;
			}
		}
		//add external resources
		for(int i = 0; i < this.externalResources.size(); i++){
			MISExternalResource resource = new MISExternalResource();
			resource.id = this.externalResources.get(i).id;
			resource.name = this.externalResources.get(i).name;
			resource.path = this.externalResources.get(i).path;
			resource.type = this.externalResources.get(i).type;
		}
		return newScene;
		
	}
	
	private MISNode createDeepCopyOfNode(MISNode nodeToCopy){
		if(nodeToCopy == null){
			return null;
		}
		MISNode node = null;
		if(nodeToCopy instanceof MISNode2D){
			MISNode2D old = (MISNode2D) nodeToCopy;
			node = new MISNode2D(new MIS2DTransform(old.transform.positionX, old.transform.positionY, old.transform.rotation, old.transform.scaleX, old.transform.scaleY));
		} else if(nodeToCopy instanceof MISControl){
			// TODO Auto-generated method stub
			node = new MISControl();
		} else if(nodeToCopy instanceof MISSpatial){
			MISSpatial old = (MISSpatial) nodeToCopy;
			MISSpatial spatialCopy = new MISSpatial();
			spatialCopy.xx = old.xx;
			spatialCopy.yx = old.yx;
			spatialCopy.zx = old.zx;
			
			spatialCopy.xy = old.xy;
			spatialCopy.yy = old.yy;
			spatialCopy.zy = old.zy;
			
			spatialCopy.xz = old.xz;
			spatialCopy.yz = old.yz;
			spatialCopy.zz = old.zz;
			
			spatialCopy.xo = old.xo;
			spatialCopy.yo = old.yo;
			spatialCopy.zo = old.zo;
			node = spatialCopy;
		} else if(nodeToCopy instanceof MISNodeScene){
			MISNodeScene old = (MISNodeScene) nodeToCopy;
			MISNodeScene sceneCopy = new MISNodeScene();
			sceneCopy.scene = old.scene;
			sceneCopy.resource = old.resource;
			sceneCopy.headNode = createDeepCopyOfNode(old.headNode);
			node = sceneCopy;
		} else if(nodeToCopy instanceof MISNode){
			node = new MISNode();
		}
		MISNode old = nodeToCopy;
		node.index = old.index;
		node.informationReceivers = old.informationReceivers;
		node.name = old.name;
		node.parent = old.parent;
		node.scriptAttached = old.scriptAttached;
		node.scriptId = old.scriptId;
		node.scriptName = old.scriptName;
		node.type = old.type;
		node.shouldSendInformation = old.shouldSendInformation;
		node.isControllable = old.isControllable;
		node.controlReceiver = old.controlReceiver;
		List<MISTrigger> triggers = new ArrayList<MISTrigger>();
		for(int i = 0; i < old.trigger.size(); i++){
			if(old.trigger.get(i) instanceof MISTriggerValue){
				MISTriggerValue oldTrigger = (MISTriggerValue) old.trigger.get(i);
				triggers.add(new MISTriggerValue(oldTrigger.action, oldTrigger.valueTarget, oldTrigger.comparer, oldTrigger.targetType));
			} else{
				triggers.add(new MISTrigger(old.trigger.get(i).action));
			}
		}
		node.trigger = triggers;
		return node;
	}
	
	
}
