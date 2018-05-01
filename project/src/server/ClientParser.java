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
				boolean queueStart = serverReference.notifyServerQueueStart(client, sceneQueue);
				if(queueStart){
					client.addMessageToSend("queuestart "+sceneQueue);
					return true;
				}
			} catch(NumberFormatException e){
				e.printStackTrace();
			}
		} else if(message.startsWith("queueend")){
			try{
				int sceneQueue = Integer.parseInt(message.split(" ")[1]);
				boolean queueEnd = serverReference.notifyServerQueueEnd(client, sceneQueue);
				if(queueEnd){
					client.addMessageToSend("queueend "+sceneQueue);
					return true;
				}
			} catch(NumberFormatException e){
				e.printStackTrace();
			}
		} else if(message.startsWith("node")){
			if(client.getRoom() != null){
				
			}
		}
		return false;
	}
	
}
