package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{

	public ServerSocket socket;
	public boolean running;
	public Thread thread;
	public int port;
	List<Client> clientList;
	
	public Server(int port){
		this.port = port;
		clientList = new ArrayList<Client>();
		thread = new Thread(this);
	}
	
	public boolean startServer(){
		try {
			socket = new ServerSocket(port);
			running = true;
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
				Client clientObject = new Client(client, reader, writer);
				clientList.add(clientObject);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
