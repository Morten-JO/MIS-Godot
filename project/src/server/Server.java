package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import broadcasts.MISBroadcastMessage;
import data_types.MISScene;
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
	
	
	public Server(int port, boolean ui, boolean autoStart){
		this.port = port;
		clientList = new ArrayList<Client>();
		thread = new Thread(this);
		rooms = new ArrayList<Room>();
		this.ui = ui;
		if(this.ui){
			serverUI = new ServerApplicationWindow(this, autoStart);
			serverUI.setVisible(true);
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
				if(clientList.size() > 1){
					MISScene scene = new MISScene(0);
					MISBroadcastMessage broadcast = new MISBroadcastMessage("TestBroadcast", 27, "Hello there sir.");
					broadcast.receiver = new MISReceiverAll();
					scene.broadcasts.add(broadcast);
					Room room = new Room(scene, 2, clientList.get(0), clientList.get(1));
					clientList.get(0).joinRoom(room);
					clientList.get(1).joinRoom(room);
					clientList.get(0).isLookingForGame = false;
					clientList.get(1).isLookingForGame = true;
					room.startBroadcastThread();
					rooms.add(room);
				}
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
	
	public void notifyServerOfFailedClient(Client client){
		clientList.remove(client);
		if(this.ui){
			serverUI.receivedMessagesFromLeft += client.getMessagesReceived();
			serverUI.sentMessagesToLeft += client.getMessagesSent();
			serverUI.updateServer();
		}
		//race conditions?
		for(int i = 0; i < rooms.size(); i++){
			for(int j = 0; j < rooms.get(i).clientsInRoom.size(); j++){
				if(rooms.get(i).clientsInRoom.get(j) == client){
					rooms.get(i).clientsInRoom.remove(j);
					i = rooms.size();
					break;
				}
			}
		}
		
	}
	
	
	
}
