package server;

public class ClientParser {

	private Server serverReference;
	private Client client;
	private boolean debug;
	
	public ClientParser(Server serverReference, Client client, boolean debug){
		this.serverReference = serverReference;
		this.client = client;
		this.debug = debug;
	}
	
	public boolean parseMessage(String message){
		if(message.startsWith("queuestart")){
			try{
				int sceneQueue = Integer.parseInt(message.split(" ")[1]);
				return serverReference.notifyServerQueueStart(client, sceneQueue);
			} catch(NumberFormatException e){
				e.printStackTrace();
			}
		} else if(message.startsWith("queueend")){
			try{
				int sceneQueue = Integer.parseInt(message.split(" ")[1]);
				return serverReference.notifyServerQueueEnd(client, sceneQueue);
			} catch(NumberFormatException e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
