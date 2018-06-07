package server;

import java.util.ArrayList;
import java.util.List;

import actions.MISActionMessage;
import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastValue;
import broadcasts.MISBroadcastMessage;
import data_types.MIS2DTransform;
import data_types.MISScene;
import nodes.MISControl;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISNodeScene;
import nodes.MISSpatial;
import project.MISProject;
import receivers.MISReceiverAll;
import receivers.MISReceiverNotPerson;
import receivers.MISReceiverNotTeam;
import receivers.MISReceiverPerson;
import receivers.MISReceiverTeam;
import triggers.MISTrigger;
import triggers.MISTriggerValue;

public class Room {

	private MISScene scene;
	
	private Thread broadcastThread;
	private Thread refreshThread;
	private Thread triggerThread;
	
	private boolean broadcastRunning = true;
	private boolean refreshRunning = true;
	private boolean triggerRunning = true;
	public List<Client> clientsInRoom;
	
	public List<List<Client>> teams;
	
	private int roomSize;
	
	private int roomId;
	private static int globalRoomID;
	
	
	
	public Room(MISScene scene, int roomSize, Server server){
		this.scene = scene;
		roomId = globalRoomID;
		globalRoomID++;
		createBroadcastThread();
		createRefreshThread();
		createTriggerThread();
		this.roomSize = roomSize;
		clientsInRoom = new ArrayList<Client>();
		teams = new ArrayList<List<Client>>();
		for(int i = 0; i < scene.nodeList.size(); i++){
			System.out.println("#"+i+" Should send: "+scene.nodeList.get(i).shouldSendInformation);
			if(scene.nodeList.get(i).shouldSendInformation){
				System.out.println("data: "+scene.nodeList.get(i).informationReceivers);
			}
		}
		System.out.println("Scene nodelistTypes Class whatever:");
		for(int i = 0; i < scene.nodeList.size(); i++){
			System.out.println("#"+i+": "+scene.nodeList.get(i).name+" - "+scene.nodeList.get(i).getClass().getSimpleName());
		}
	}
	
	public Room(MISScene scene, int roomSize, Server server,  Client... client){
		this(scene, roomSize, server);
		try{
			if(MISProject.project.roomSettings.teams == 0){
				for(int i = 0; i < client.length; i++){
					List<Client> clientList = new ArrayList<Client>();
					clientList.add(client[i]);
					teams.add(clientList);
				}
			} else{
				for(int i = 0; i < MISProject.project.roomSettings.teams; i++){
					teams.add(new ArrayList<Client>());
				}
				int teamCounter = 0;
				for(int i = 0; i < client.length; i++){
					teams.get(teamCounter).add(client[i]);
					teamCounter++;
					if(teamCounter >= teams.size()){
						teamCounter = 0;
					}
				}
			}
			
			for(int i = 0; i < teams.size(); i++){
				for(int j = 0; j < teams.get(i).size(); j++){
					clientsInRoom.add(teams.get(i).get(j));
					teams.get(i).get(j).joinRoom(this);
					teams.get(i).get(j).notifyCreatedRoom(this, roomSize, i, clientsInRoom.size()-1);
				}
			}
		} catch(NullPointerException e){
			e.printStackTrace();
			System.out.println("Error, apparently creating a room without a setting in scene: "+scene.name);
			server.notifyFatalRoomError();
		}
	}
	
	private void createTriggerThread(){
		triggerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(triggerRunning){
					for(int i = 0; i< scene.nodeList.size(); i++){
						MISNode node = scene.nodeList.get(i);
						for(int j = 0; j < scene.nodeList.get(i).trigger.size(); j++){
							MISTrigger trigger = scene.nodeList.get(i).trigger.get(j);
							if(trigger.hasTriggered){
								continue;
							}
							boolean shouldTrigger = false;
							if(trigger instanceof MISTriggerValue){
								MISTriggerValue valueTrigger = (MISTriggerValue) trigger;
								double value;
								if(node instanceof MISNode2D){
									if(valueTrigger.targetType == MISTriggerValue.TargetType.x){
										value = ((MISNode2D) node).transform.positionX;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.y){
										value = ((MISNode2D) node).transform.positionY;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.rot){
										value = ((MISNode2D) node).transform.rotation;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.scaleX){
										value = ((MISNode2D) node).transform.scaleX;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.scaleY){
										value = ((MISNode2D) node).transform.scaleY;
									} else{
										continue;
									}
								} else if(node instanceof MISSpatial){
									if(valueTrigger.targetType == MISTriggerValue.TargetType.xx){
										value = ((MISSpatial) node).xx;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.xy){
										value = ((MISSpatial) node).xy;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.xz){
										value = ((MISSpatial) node).xz;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.yx){
										value = ((MISSpatial) node).yx;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.yy){
										value = ((MISSpatial) node).yy;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.yz){
										value = ((MISSpatial) node).yz;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.zx){
										value = ((MISSpatial) node).zx;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.zy){
										value = ((MISSpatial) node).zy;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.zz){
										value = ((MISSpatial) node).zz;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.xo){
										value = ((MISSpatial) node).xo;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.yo){
										value = ((MISSpatial) node).yo;
									} else if(valueTrigger.targetType == MISTriggerValue.TargetType.zo){
										value = ((MISSpatial) node).zo;
									} else {
										continue;
									}
								} else{
									continue;
								}
								
								if(valueTrigger.comparer == MISTriggerValue.ValueComparer.Equal){
									shouldTrigger = valueTrigger.valueTarget == value;
								} else if(valueTrigger.comparer == MISTriggerValue.ValueComparer.Higher){
									shouldTrigger = valueTrigger.valueTarget < value;
								} else if(valueTrigger.comparer == MISTriggerValue.ValueComparer.Lower){
									shouldTrigger = valueTrigger.valueTarget > value;
								}
							}
							if(shouldTrigger){
								trigger.hasTriggered = true;
								if(trigger.action instanceof MISActionMessage){
									MISActionMessage actionMessage = (MISActionMessage) trigger.action;
									if(actionMessage.receiver instanceof MISReceiverAll){
										for(int x = 0; x < clientsInRoom.size(); x++){
											clientsInRoom.get(x).addMessageToSend("trigger_"+node.name+"_"+node.index+"_"+j+" "+actionMessage.message);
										}
									} else if(actionMessage.receiver instanceof MISReceiverTeam){
										MISReceiverTeam team = (MISReceiverTeam) actionMessage.receiver;
										for(int x = 0; x < teams.get(team.team).size(); x++){
											teams.get(team.team).get(x).addMessageToSend("trigger_"+node.name+"_"+node.index+"_"+j+" "+actionMessage.message);
										}
									} else if(actionMessage.receiver instanceof MISReceiverPerson){
										MISReceiverPerson person = (MISReceiverPerson) actionMessage.receiver;
										clientsInRoom.get(person.person).addMessageToSend(actionMessage.message);
									} else if(actionMessage.receiver instanceof MISReceiverNotTeam){
										MISReceiverNotTeam team = (MISReceiverNotTeam) actionMessage.receiver;
										for(int x = 0; x < teams.size(); x++){
											if(x != team.team){
												for(int z = 0; z < teams.get(x).size(); z++){
													teams.get(x).get(z).addMessageToSend("trigger_"+node.name+"_"+node.index+"_"+j+" "+actionMessage.message);
												}
											}
										}
									} else if(actionMessage.receiver instanceof MISReceiverNotPerson){
										MISReceiverNotPerson person = (MISReceiverNotPerson) actionMessage.receiver;
										for(int x = 0; x < clientsInRoom.size(); x++){
											if(person.person != x){
												clientsInRoom.get(x).addMessageToSend("trigger_"+node.name+"_"+node.index+"_"+j+" "+actionMessage.message);
											}
										}
									}
								}
							}
						}
					}
					try {
						Thread.sleep(1000/MISProject.project.refreshRate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void createRefreshThread(){
		refreshThread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(refreshRunning){
					for(int i = 0; i < scene.nodeList.size(); i++){
						if(scene.nodeList.get(i).shouldSendInformation){
							if(scene.nodeList.get(i).informationReceivers != null){
								if(scene.nodeList.get(i).informationReceivers instanceof MISReceiverAll){
									for(int j = 0; j < clientsInRoom.size(); j++){
										clientsInRoom.get(j).addMessageToSend(scene.nodeList.get(i).getReadyPacket());
									}
								} else if(scene.nodeList.get(i).informationReceivers instanceof MISReceiverTeam){
									MISReceiverTeam teamReceivers = (MISReceiverTeam)scene.nodeList.get(i).informationReceivers;
									for(int j = 0; j < teams.get(teamReceivers.team).size(); j++){
										teams.get(teamReceivers.team).get(j).addMessageToSend(scene.nodeList.get(i).getReadyPacket());
									}
								} else if(scene.nodeList.get(i).informationReceivers instanceof MISReceiverPerson){
									MISReceiverPerson personReceiver = (MISReceiverPerson)scene.nodeList.get(i).informationReceivers;
									if(personReceiver.person >= 0 && personReceiver.person < clientsInRoom.size()){
										clientsInRoom.get(personReceiver.person).addMessageToSend(scene.nodeList.get(i).getReadyPacket());
									}
								} else if(scene.nodeList.get(i).informationReceivers instanceof MISReceiverNotPerson){
									MISReceiverNotPerson personReceiver = (MISReceiverNotPerson)scene.nodeList.get(i).informationReceivers;
									for(int j = 0; j < clientsInRoom.size(); j++){
										if(personReceiver.person != j){
											clientsInRoom.get(j).addMessageToSend(scene.nodeList.get(i).getReadyPacket());
										}
									}
								} else if(scene.nodeList.get(i).informationReceivers instanceof MISReceiverNotTeam){
									MISReceiverNotTeam teamReceiver = (MISReceiverNotTeam)scene.nodeList.get(i).informationReceivers;
									for(int j = 0; j < teams.size(); j++){
										if(teamReceiver.team != j){
											for(int x = 0; x < teams.get(j).size(); x++){
												teams.get(j).get(x).addMessageToSend(scene.nodeList.get(i).getReadyPacket());
											}
										}
									}
								}
							}

						}
					}
					try {
						Thread.sleep(1000/MISProject.project.refreshRate);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void createBroadcastThread(){
		broadcastThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(broadcastRunning){
					for(int i = 0; i < scene.broadcasts.size(); i++){
						if(scene.broadcasts.get(i).shouldSend()){
							String dataToSend = "";
							if(scene.broadcasts.get(i) instanceof MISBroadcastMessage){
								dataToSend = ((MISBroadcastMessage)scene.broadcasts.get(i)).message;
							} else if(scene.broadcasts.get(i) instanceof MISBroadcastData){
								dataToSend = "broadcastData not implement";
							} else if(scene.broadcasts.get(i) instanceof MISBroadcastValue){
								dataToSend = "broadcastValue not implement";
							}
							if(scene.broadcasts.get(i).receiver instanceof MISReceiverAll){
								for(int j = 0; j < clientsInRoom.size(); j++){
									clientsInRoom.get(j).addMessageToSend(dataToSend);
								}
							}
						}
					}
					Long timeSleep = 500L;
					if(scene.broadcasts.size() > 0){
						timeSleep = scene.broadcasts.get(0).getTimeForNextSend();
					}
					for(int i = 0; i < scene.broadcasts.size(); i++){
						if(scene.broadcasts.get(i).getTimeForNextSend() < timeSleep){
							timeSleep = scene.broadcasts.get(i).getTimeForNextSend();
						}
					}
					if(timeSleep > 0L){
						try {
							Thread.sleep(timeSleep);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	public void startBroadcastThread(){
		broadcastRunning = true;
		broadcastThread.start();
	}
	
	public void startRefreshThread(){
		refreshRunning = true;
		refreshThread.start();
	}
	
	public void startTriggerThread(){
		triggerRunning = true;
		triggerThread.start();
	}
	
	public void stopBroadcastThread(){
		broadcastRunning = false;
	}
	
	public void stopRefreshThread(){
		refreshRunning = false;
	}
	
	public int getRoomID(){
		return roomId;
	}
	
	public boolean closeRoom(){
		broadcastRunning = false;
		refreshRunning = false;
		return true;
	}
	
	public int getSceneId(){
		return scene.IDNumber;
	}

	
	//Sensitive code since users can change the message, and therefore should be handled so it doesnt crash.
	public void notifyNodeChange(Client client, String message) {
		try{
			String[] messageFragments = message.split(" ");
			String name = "";
			int index = -1;
			if(message.contains("[node]")){
				int indexFragmentAt = 0;
				for(int i = 0; i < messageFragments.length; i++){
					if(messageFragments[i].equals("[node]")){
						indexFragmentAt = i;
						break;
					}
				}
				name = messageFragments[indexFragmentAt+1];
				index = Integer.parseInt(messageFragments[indexFragmentAt+2]);
			}
			if(!scene.nodeList.get(index).isControllable){
				client.addMessageToSend("[node] "+name+" "+index+" denied");
				return;
			}
			boolean isAllowedToUpdate = false;
			if(scene.nodeList.get(index).controlReceiver instanceof MISReceiverPerson){
				int indexInList = -1;
				for(int i = 0; i < clientsInRoom.size(); i++){
					if(clientsInRoom.get(i) == client){
						indexInList = i;
						break;
					}
				}
				if(indexInList != -1){
					if(((MISReceiverPerson)scene.nodeList.get(index).controlReceiver).person == indexInList){
						isAllowedToUpdate = true;
					}
				}
			} else if(scene.nodeList.get(index).controlReceiver instanceof MISReceiverTeam){
				int controllingTeam = ((MISReceiverTeam)scene.nodeList.get(index).controlReceiver).team;
				if(teams.get(controllingTeam).contains(client)){
					isAllowedToUpdate = true;
				}
			} else if(scene.nodeList.get(index).controlReceiver instanceof MISReceiverNotPerson){
				int indexInList = -1;
				for(int i = 0; i < clientsInRoom.size(); i++){
					if(clientsInRoom.get(i) == client){
						indexInList = i;
						break;
					}
				}
				if(indexInList != -1){
					if(((MISReceiverNotPerson)scene.nodeList.get(index).controlReceiver).person != indexInList){
						isAllowedToUpdate = true;
					}
				}
			} else if(scene.nodeList.get(index).controlReceiver instanceof MISReceiverNotTeam) {
				int controllingTeam = ((MISReceiverNotTeam)scene.nodeList.get(index).controlReceiver).team;
				if(!teams.get(controllingTeam).contains(client)){
					isAllowedToUpdate = true;
				}
			} else{
				isAllowedToUpdate = true;
			}
			if(!isAllowedToUpdate){
				return;
			}
			if(message.contains("[transform2d]")){
				int indexFragmentAt = 0;
				for(int i = 0; i < messageFragments.length; i++){
					if(messageFragments[i].equals("[transform2d]")){
						indexFragmentAt = i;
						break;
					}
				}
				double xPos = Double.parseDouble(messageFragments[indexFragmentAt+1]);
				double yPos = Double.parseDouble(messageFragments[indexFragmentAt+2]);
				double rot = Double.parseDouble(messageFragments[indexFragmentAt+3]);
				double xScale = Double.parseDouble(messageFragments[indexFragmentAt+4]);
				double yScale = Double.parseDouble(messageFragments[indexFragmentAt+5]);
				MIS2DTransform transform = new MIS2DTransform(xPos, yPos, rot, xScale, yScale);
				if(scene.nodeList.get(index) instanceof MISNode2D){
					((MISNode2D)scene.nodeList.get(index)).transform = transform;
				} else if(scene.nodeList.get(index) instanceof MISNodeScene){
					MISNode headNode = ((MISNodeScene)scene.nodeList.get(index)).headNode;
					if(headNode instanceof MISNode2D){
						((MISNode2D)headNode).transform = transform;
					}
				}
			}
			if(message.contains("[spatial]")){
				int indexFragmentAt = 0;
				for(int i = 0; i < messageFragments.length; i++){
					if(messageFragments[i].equals("[spatial]")){
						indexFragmentAt = i;
						break;
					}
				}
				double xx = Double.parseDouble(messageFragments[indexFragmentAt+1]);
				double xy = Double.parseDouble(messageFragments[indexFragmentAt+2]);
				double xz = Double.parseDouble(messageFragments[indexFragmentAt+3]);
				
				double yx = Double.parseDouble(messageFragments[indexFragmentAt+4]);
				double yy = Double.parseDouble(messageFragments[indexFragmentAt+5]);
				double yz = Double.parseDouble(messageFragments[indexFragmentAt+6]);
				
				double zx = Double.parseDouble(messageFragments[indexFragmentAt+7]);
				double zy = Double.parseDouble(messageFragments[indexFragmentAt+8]);
				double zz = Double.parseDouble(messageFragments[indexFragmentAt+9]);
				
				double xo = Double.parseDouble(messageFragments[indexFragmentAt+10]);
				double yo = Double.parseDouble(messageFragments[indexFragmentAt+11]);
				double zo = Double.parseDouble(messageFragments[indexFragmentAt+12]);
				
				if(scene.nodeList.get(index) instanceof MISSpatial){
					((MISSpatial)scene.nodeList.get(index)).xx = xx;
					((MISSpatial)scene.nodeList.get(index)).yx = yx;
					((MISSpatial)scene.nodeList.get(index)).zx = zx;
					
					((MISSpatial)scene.nodeList.get(index)).xy = xy;
					((MISSpatial)scene.nodeList.get(index)).yy = yy;
					((MISSpatial)scene.nodeList.get(index)).zy = zy;
					
					((MISSpatial)scene.nodeList.get(index)).xz = xz;
					((MISSpatial)scene.nodeList.get(index)).yz = yz;
					((MISSpatial)scene.nodeList.get(index)).zz = zz;
					
					((MISSpatial)scene.nodeList.get(index)).xo = xo;
					((MISSpatial)scene.nodeList.get(index)).yo = yo;
					((MISSpatial)scene.nodeList.get(index)).zo = zo;
				} else if(scene.nodeList.get(index) instanceof MISNodeScene){
					MISNode headNode = ((MISNodeScene)scene.nodeList.get(index)).headNode;
					if(headNode instanceof MISSpatial){
						((MISSpatial)headNode).xx = xx;
						((MISSpatial)headNode).yx = yx;
						((MISSpatial)headNode).zx = zx;
						
						((MISSpatial)headNode).xy = xy;
						((MISSpatial)headNode).yy = yy;
						((MISSpatial)headNode).zy = zy;
						
						((MISSpatial)headNode).xz = xz;
						((MISSpatial)headNode).yz = yz;
						((MISSpatial)headNode).zz = zz;
						
						((MISSpatial)headNode).xo = xo;
						((MISSpatial)headNode).yo = yo;
						((MISSpatial)headNode).zo = zo;
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public MISScene getScene(){
		return scene;
	}
	
}
