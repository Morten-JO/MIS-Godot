package server;

import java.util.ArrayList;
import java.util.List;

import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastValue;
import broadcasts.MISBroadcastMessage;
import data_types.MISScene;
import receivers.MISReceiverAll;

public class Room {

	private MISScene scene;
	
	private Thread broadcastThread;
	
	private boolean broadcastRunning = true;
	
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
		this.roomSize = roomSize;
		clientsInRoom = new ArrayList<Client>();
		teams = new ArrayList<List<Client>>();
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
	
	public int getRoomID(){
		return roomId;
	}
	
	public boolean closeRoom(){
		broadcastRunning = false;
		return true;
	}
	
	public int getSceneId(){
		return scene.IDNumber;
	}
	
}
