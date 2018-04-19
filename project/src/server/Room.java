package server;

import java.util.List;

import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastValue;
import broadcasts.MISBroastcastMessage;
import data_types.MISScene;
import receivers.MISReceiverAll;

public class Room {

	private MISScene scene;
	
	private Thread broadcastThread;
	
	private boolean broadcastRunning = true;
	
	private List<Client> clientsInRoom;
	
	public Room(MISScene scene){
		createBroadcastThread();
	}
	
	private void createBroadcastThread(){
		broadcastThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(broadcastRunning){
					for(int i = 0; i < scene.broadcasts.size(); i++){
						if(scene.broadcasts.get(i).shouldSend()){
							String dataToSend = "";
							if(scene.broadcasts.get(i) instanceof MISBroastcastMessage){
								dataToSend = ((MISBroastcastMessage)scene.broadcasts.get(i)).message;
							} else if(scene.broadcasts.get(i) instanceof MISBroadcastData){
								dataToSend = "broadcastDatanot imeplement";
							} else if(scene.broadcasts.get(i) instanceof MISBroadcastValue){
								dataToSend = "broadcastValuenot imeplement";
							}
							if(scene.broadcasts.get(i).receiver instanceof MISReceiverAll){
								for(int j = 0; j < clientsInRoom.size(); j++){
									clientsInRoom.get(i).addMessageToSend(dataToSend);
								}
							}
						}
					}
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void startBroadcastThread(){
		broadcastRunning = true;
		broadcastThread.start();
	}
	
}
