package server;

import java.util.ArrayList;
import java.util.List;

import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastValue;
import broadcasts.MISBroadcastMessage;
import data_types.MIS2DTransform;
import data_types.MISScene;
import nodes.MISControl;
import nodes.MISNode2D;
import nodes.MISSpatial;
import project.MISProject;
import receivers.MISReceiverAll;
import receivers.MISReceiverNotPerson;
import receivers.MISReceiverNotTeam;
import receivers.MISReceiverPerson;
import receivers.MISReceiverTeam;

public class Room {

	private MISScene scene;
	
	private Thread broadcastThread;
	private Thread refreshThread;
	
	private boolean broadcastRunning = true;
	private boolean refreshRunning = true;
	
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
		this.roomSize = roomSize;
		clientsInRoom = new ArrayList<Client>();
		teams = new ArrayList<List<Client>>();
		for(int i = 0; i < scene.nodeList.size(); i++){
			System.out.println("#"+i+" Should send: "+scene.nodeList.get(i).shouldSendInformation);
			if(scene.nodeList.get(i).shouldSendInformation){
				System.out.println("data: "+scene.nodeList.get(i).informationReceivers);
			}
		}
	}
	
	public Room(MISScene scene, int roomSize, Server server,  Client... client){
		this(scene, roomSize, server);
		try{
			if(scene.roomSettings.teams == 0){
				for(int i = 0; i < client.length; i++){
					List<Client> clientList = new ArrayList<Client>();
					clientList.add(client[i]);
					teams.add(clientList);
				}
			} else{
				for(int i = 0; i < scene.roomSettings.teams; i++){
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
				System.out.println("Ran this through, how many times?");
				for(int j = 0; j < teams.get(i).size(); j++){
					clientsInRoom.add(teams.get(i).get(j));
					teams.get(i).get(j).joinRoom(this);
					teams.get(i).get(j).notifyCreatedRoom(this, roomSize, i);
				}
			}
		} catch(NullPointerException e){
			e.printStackTrace();
			System.out.println("Error, apparently creating a room without a setting in scene: "+scene.name);
			server.notifyFatalRoomError();
		}
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
								dataToSend = "broadcastDatanot imeplement";
							} else if(scene.broadcasts.get(i) instanceof MISBroadcastValue){
								dataToSend = "broadcastValuenot imeplement";
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
				} else if(scene.nodeList.get(index) instanceof MISControl){
					// TODO Auto-generated method stub
				} else if(scene.nodeList.get(index) instanceof MISSpatial){
					// TODO Auto-generated method stub
				}
			}
			// TODO Auto-generated method stub
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
