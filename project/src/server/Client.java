package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import project.MISProject;

public class Client implements Runnable{

	private Socket socket;
	private Long lastResponse;
	private BufferedReader reader;
	private PrintWriter writer;
	
	private boolean writerRunning = false;
	private boolean readerRunning = false;
	
	private Thread writerThread;
	private Thread readerThread;
	
	private List<String> receivedMessages;
	private List<String> toBeSentMessages;
	
	private int messagesSent = 0;
	private int messagesReceived = 0;
	
	public boolean isLookingForGame = true;
	
	private static int globalID = 0;
	public int id;
	
	private Server server;
	private Client client;
	private ClientParser parser;
	private Room room;
	
	private String name = "";
	
	public ArrayList<String> sentMessagesDataStorage;
	public ArrayList<String> receivedMessagesDataStorage;
	
	private ArrayList<Long> messageLimiter;
	
	public Client(Server server, Socket socket, BufferedReader reader, PrintWriter writer){
		this.socket = socket;
		this.lastResponse = System.nanoTime();
		this.reader = reader;
		this.writer = writer;
		writerThread = new Thread(this);
		lastResponse = System.currentTimeMillis();
		messageLimiter = new ArrayList<Long>();
		this.id = globalID;
		globalID++;
		this.server = server;
		this.client = this;
		Runnable run = new Runnable() {
			
			@Override
			public void run() {
				while(readerRunning){
					try {
						for(int i = 0; i < messageLimiter.size(); i++){
							if(messageLimiter.get(i) < System.currentTimeMillis()){
								messageLimiter.remove(i);
								i--;
							}
						}
						if(messageLimiter.size() < MISProject.project.maxMessagesPerClientPerSecond){
							String message = reader.readLine();
							receivedMessages.add(message);
							messagesReceived++;
							receivedMessagesDataStorage.add(message);
							parser.parseMessage(message);
							lastResponse = System.currentTimeMillis();
							messageLimiter.add(System.currentTimeMillis() + 1000L);
						} else{
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} catch(SocketException e){
						server.notifyServerOfFailedClient(client);
						readerRunning = false;
						writerRunning = false;
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					} catch (IOException  e) {
						e.printStackTrace();
					} 
				}
			}
		};
		readerThread = new Thread(run);
		receivedMessages = new ArrayList<String>();
		toBeSentMessages = new ArrayList<String>();
		sentMessagesDataStorage = new ArrayList<String>();
		receivedMessagesDataStorage = new ArrayList<String>();
		parser = new ClientParser(server, this, true);
	}
	
	public void startThreads(){
		writerRunning = true;
		readerRunning = true;
		writerThread.start();
		readerThread.start();
	}

	@Override
	public void run() {
		while(writerRunning){
			if(toBeSentMessages.size() > 0 ){
				writer.println(toBeSentMessages.get(0));
				sentMessagesDataStorage.add(toBeSentMessages.get(0));
				toBeSentMessages.remove(0);
			} else {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addMessageToSend(String message){
		toBeSentMessages.add(message);
		messagesSent++;
	}
	
	public int getMessagesSent(){
		return messagesSent;
	}
	
	public int getMessagesReceived(){
		return messagesReceived;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public Long getLastResponse(){
		return lastResponse;
	}
	
	public void joinRoom(Room room){
		this.room = room;
	}
	
	public void leaveRoom(Room room){
		room = null;
	}
	
	public Room getRoom(){
		return room;
	}
	
	public void notifyCreatedRoom(Room room, int totalPlayers, int teamId, int clientId){
		System.out.println("Team id is: "+teamId);
		String messageForClient = "roomcreate "+room.getRoomID()+" "+room.getSceneId()+" "+totalPlayers+" "+teamId+" "+clientId;
		addMessageToSend(messageForClient);
	}
	
	public void notifyJoinedRoom(Room room){
		String messageForClient = "roomJoin ";
	}
	
	public void notifyLeavedRoom(Room room){
		
	}
	
	public void notifySetName(String name){
		this.name = name;
	}
	
	public String getIp(){
		return socket.getInetAddress().getHostAddress();
	}
	
	public void cleanUpClient(){
		try {
			readerRunning = false;
			writerRunning = false;
			reader.close();
			for(int i = 0; i < toBeSentMessages.size(); i++){
				writer.println(toBeSentMessages.get(i));
			}
			writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void notifyValidBuildVersion() {
		addMessageToSend("good build_version");
	}

	public void notifyWonRoom(int winningTeamIndex) {
		addMessageToSend("game_won "+winningTeamIndex);
	}

	public void notifyGameEnd(int winningTeamIndex) {
		addMessageToSend("game_end "+winningTeamIndex);
		room = null;
	}
}
