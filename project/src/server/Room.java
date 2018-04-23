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
	
	private int roomSize;
	
	private int roomId;
	private static int globalRoomID;
	
	public Room(MISScene scene, int roomSize){
		this.scene = scene;
		roomId = globalRoomID;
		globalRoomID++;
		createBroadcastThread();
		this.roomSize = roomSize;
		clientsInRoom = new ArrayList<Client>();
	}
	
	public Room(MISScene scene, int roomSize, Client... client){
		this(scene, roomSize);
		for(int i = 0; i < client.length; i++){
			clientsInRoom.add(client[i]);
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
	
}
