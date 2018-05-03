package nodes;

import receivers.MISReceiver;

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
	
	public MISNode(){
		
	}
	
	public String getReadyPacket(){
		return "node "+name+" "+index;
	}
	
}
