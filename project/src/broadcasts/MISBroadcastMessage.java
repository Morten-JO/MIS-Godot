package broadcasts;

public class MISBroadcastMessage extends MISBroadcast{

	public String message;
	
	public MISBroadcastMessage(String broadcastName, float timesPerMinute, String message) {
		super(broadcastName, timesPerMinute);
		this.message = message;
	}
	
	@Override
	public String dataToSend() {
		return message;
	}

	
	
}
