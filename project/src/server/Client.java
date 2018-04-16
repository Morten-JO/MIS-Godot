package server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private Socket socket;
	private Long lastResponse;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public Client(Socket socket, BufferedReader reader, PrintWriter writer){
		this.socket = socket;
		this.lastResponse = System.nanoTime();
		this.reader = reader;
		this.writer = writer;
	}
	
}
