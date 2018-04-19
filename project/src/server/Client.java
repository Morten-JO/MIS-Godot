package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
	
	public Client(Socket socket, BufferedReader reader, PrintWriter writer){
		this.socket = socket;
		this.lastResponse = System.nanoTime();
		this.reader = reader;
		this.writer = writer;
		writerThread = new Thread(this);
		Runnable run = new Runnable() {
			
			@Override
			public void run() {
				while(readerRunning){
					try {
						receivedMessages.add(reader.readLine());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		readerThread = new Thread(run);
		receivedMessages = new ArrayList<String>();
		toBeSentMessages = new ArrayList<String>();
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
	}
	
}
