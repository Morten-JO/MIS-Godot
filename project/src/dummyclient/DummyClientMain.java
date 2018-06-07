package dummyclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class DummyClientMain {

	public static void main(String[] args) {
		try {
			DummyClientUI ui = new DummyClientUI();
			Socket socket = new Socket("ubuntu4.saluton.dk", 1234);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			List<String> received = new ArrayList<String>();
			List<String> toSend = new ArrayList<String>();
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							String messageReceived = reader.readLine();
							received.add(messageReceived);
							ui.addElement(messageReceived);
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			Thread writerThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						if(toSend.size() > 0){
							writer.println(toSend.get(0));
							toSend.remove(0);
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread.start();
			writerThread.start();
			toSend.add("queuestart 0");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
