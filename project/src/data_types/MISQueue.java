package data_types;

import server.Client;

public class MISQueue {

	public int sceneQueue;
	public Client client;
	
	public MISQueue(Client client, int sceneQueue){
		this.client = client;
		this.sceneQueue = sceneQueue;
	}
	
}
