package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import broadcasts.MISBroadcastMessage;
import data_types.MISQueue;
import data_types.MISScene;
import game_types.MISCompetetiveGameType;
import project.MISProject;
import receivers.MISReceiverAll;
import server_ui.ServerApplicationWindow;

public class Server implements Runnable{

	public ServerSocket socket;
	public boolean running;
	public Thread thread;
	public int port;
	public List<Client> clientList;
	private boolean ui = false;
	private ServerApplicationWindow serverUI;
	public int totalPlayers = 0;
	public Long timeStartedInMillis;
	private List<Room> rooms;
	public List<MISQueue> queues;
	private Thread queueHandler;
	private Thread clientControlHandler;
	private Server self;
	private boolean autoQueue = false;
	
	public Server(int port, boolean ui, boolean autoStart){
		self = this;
		this.port = port;
		clientList = new ArrayList<Client>();
		thread = new Thread(this);
		rooms = new ArrayList<Room>();
		queues = new ArrayList<MISQueue>();
		this.ui = ui;
		if(this.ui){
			serverUI = new ServerApplicationWindow(this, autoStart);
			serverUI.setVisible(true);
		}
		if(autoStart){
			startServer();
		}
	}
	
	public boolean startServer(){
		try {
			socket = new ServerSocket(port);
			timeStartedInMillis = System.currentTimeMillis();
			running = true;
			if(this.ui){
				serverUI.startServerUI();
			}
			startQueueHandler();
			startClientControlHandler();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		thread.start();
		return true;
	}
	
	
	@Override
	public void run() {
		while(running){
			try {
				Socket client = socket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
				Client clientObject = new Client(this, client, reader, writer);
				clientList.add(clientObject);
				clientObject.startThreads();
				totalPlayers++;
				if(this.ui){
					serverUI.updateServer();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean stopServer(){
		running = false;
		int messagesReceived = 0;
		int messagesSent = 0;
		for(int i = 0; i < clientList.size(); i++){
			try {
				messagesReceived += clientList.get(i).getMessagesReceived();
				messagesSent += clientList.get(i).getMessagesSent();
				clientList.get(i).getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		clientList.clear();
		for(int i = 0; i < rooms.size(); i++){
			rooms.get(i).closeRoom();
		}
		rooms.clear();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		serverUI.receivedMessagesFromLeft += messagesReceived;
		serverUI.sentMessagesToLeft += messagesSent;
		serverUI.updateServer();
		serverUI.updateServerInfo();
		serverUI.stopServerUI();
		thread = new Thread(this);
		return true;
	}
	
	/**
	 * Used for session timeout and client 
	 */
	private void startClientControlHandler(){
		clientControlHandler = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running){
					for(int i = 0; i < clientList.size(); i++){
						if(MISProject.project.timeOutDuration != 0){
							if(clientList.get(i).getLastResponse() + MISProject.project.timeOutDuration * 1000 < System.currentTimeMillis()){
								System.out.println("Client #"+clientList.get(i)+" removed due to lack of response");
								clientList.get(i).cleanUpClient();
								clientList.remove(i);
								i--;
							}
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void startQueueHandler(){
		queueHandler = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running){
					int[] sceneCounts = new int[MISProject.project.scenes.size()];
					for(int i = 0; i < sceneCounts.length; i++){
						sceneCounts[i] = 0;
					}
					for(int i = 0; i < queues.size(); i++){
						if(queues.get(i).sceneQueue < sceneCounts.length && queues.get(i).sceneQueue >= 0){
							sceneCounts[queues.get(i).sceneQueue]++;
						}
					}
					for(int i = 0; i < sceneCounts.length; i++){
						if(MISProject.project.roomSettings != null){
							if(sceneCounts[i] >= MISProject.project.roomSettings.minimumPlayers){
								int playersForRoom = MISProject.project.roomSettings.minimumPlayers;
								if(sceneCounts[i] >= MISProject.project.roomSettings.maximumPlayers){
									playersForRoom = MISProject.project.roomSettings.maximumPlayers;
								} else{
									playersForRoom = sceneCounts[i];
								}
								int count = 0;
								List<MISQueue> clients = new ArrayList<MISQueue>();
								for(int j = 0; j < queues.size(); j++){
									if(queues.get(j).sceneQueue == i){
										clients.add(queues.get(j));
										count++;
									}
									if(count >= playersForRoom){
										break;
									}
								}
								Client[] roomClients = new Client[clients.size()];
								for(int j = 0; j < clients.size(); j++){
									queues.remove(clients.get(j));
									roomClients[j] = clients.get(j).client;
								}
								//Make deep copy of scene
								MISScene originalScene = MISProject.project.scenes.get(i);
								for(int x = 0; x < originalScene.nodeList.size(); x++){
									System.out.println("X: #"+x+" and should: "+originalScene.nodeList.get(x).shouldSendInformation);
									System.out.println("and the datastruc: "+originalScene.nodeList.get(x).informationReceivers);
								}
								System.out.println("About to create room: ");
								MISScene newScene = originalScene.createDeepCopy();
								Room room = new Room(newScene, playersForRoom, self, roomClients);
								room.startBroadcastThread();
								room.startRefreshThread();
								room.startTriggerThread();
								rooms.add(room);
							}
						}
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		queueHandler.start();
	}
	
	public void notifyServerOfFailedClient(Client client){
		clientList.remove(client);
		for(int i = 0; i < queues.size(); i++){
			if(queues.get(i).client == client){
				queues.remove(i);
				break;
			}
		}
		if(this.ui){
			serverUI.receivedMessagesFromLeft += client.getMessagesReceived();
			serverUI.sentMessagesToLeft += client.getMessagesSent();
			serverUI.updateServer();
		}
		//race conditions?
		for(int j = 0; j < client.getRoom().clientsInRoom.size(); j++){
			if(client.getRoom().clientsInRoom.get(j) == client){
				client.getRoom().clientsInRoom.remove(j);
				break;
			}
		}
		
		for(int i = 0; i < client.getRoom().teams.size(); i++){
			if(client.getRoom().teams.get(i).contains(client)){
				client.getRoom().teams.get(i).remove(client);
				break;
			}
		}
		if(MISProject.project.roomSettings.gameType instanceof MISCompetetiveGameType){
			//Check room that player left
			int teamsWithPlayers = 0;
			for(int i = 0; i < client.getRoom().teams.size(); i++){
				if(client.getRoom().teams.get(i).size() > 0){
					teamsWithPlayers++;
				}
			}
			if(teamsWithPlayers <= 1){
				client.getRoom().closeRoom();
				if(teamsWithPlayers == 1){
					for(int i = 0; i < client.getRoom().teams.size(); i++){
						if(client.getRoom().teams.get(i).size() > 0){
							for(int j = 0; j < client.getRoom().teams.get(i).size(); j++){
								System.out.println("Notified.....");
								client.getRoom().teams.get(i).get(j).notifyWonRoom(i);
								client.getRoom().teams.get(i).get(j).notifyGameEnd(i);
							}
						}
					}
				}
				rooms.remove(client.getRoom());
			}
		}
		
	}

	public boolean notifyServerQueueStart(Client client, int sceneQueue) {
		boolean exists = false;
		for(int i = 0; i < queues.size(); i++){
			if(queues.get(i).client == client){
				exists = true;
			}
		}
		for(int i = 0 ; i < rooms.size(); i++){
			for(int j = 0; j < rooms.get(i).clientsInRoom.size(); j++){
				if(rooms.get(i).clientsInRoom.get(j) == client){
					exists = true;
				}
			}
		}
		if(!exists){
			queues.add(new MISQueue(client, sceneQueue));
			return true;
		}
		return false;
	}

	public boolean notifyServerQueueEnd(Client client, int sceneQueue) {
		for(int i = 0; i < queues.size(); i++){
			if(queues.get(i).client == client && queues.get(i).sceneQueue == sceneQueue){
				queues.remove(i);
				return true;
			}
		}
		return false;
	}

	public void notifyFatalRoomError() {
		// TODO Auto-generated method stub
		
	}

	public void notifyBadBuildVersion(Client client) {
		client.addMessageToSend("bad minimum_build");
		client.cleanUpClient();
		clientList.remove(client);
	}
	
	
	
}
