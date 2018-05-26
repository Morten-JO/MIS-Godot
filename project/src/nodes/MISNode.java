package nodes;

import receivers.MISReceiver;
import triggers.MISTrigger;

public class MISNode {

	public String name;
	public boolean scriptAttached;
	public String scriptName;
	public int scriptId;
	public String type;
	
	public int index;
	
	public MISNode parent;
	
	public boolean shouldSendInformation;
	public MISReceiver informationReceivers;
	
	public boolean isControllable;
	public MISReceiver controlReceiver;
	
	public MISTrigger trigger;
	
	public MISNode(){
		
	}
	
	public String getReadyPacket(){
		return "[node] "+name+" "+index;
	}
	
	public static boolean isType(String type){
		if(type.equals("Node")){
			return true;
		}
		return false;
	}
	
	
	
}
