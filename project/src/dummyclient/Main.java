package dummyclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 1000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							System.out.println("Message: "+reader.readLine());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
