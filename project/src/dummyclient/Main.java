package dummyclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 1000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			List<String> received = new ArrayList<String>();
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							String messageReceived = reader.readLine();
							received.add(messageReceived);
							
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
						writer.println("Hey size is: "+received.size());
						try {
							Thread.sleep(454);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread.start();
			writerThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
