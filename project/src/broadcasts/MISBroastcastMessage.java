package broadcasts;

public class MISBroastcastMessage extends MISBroadcast{

	public String message;
	
	public MISBroastcastMessage(String broadcastName, float timesPerMinute, String message) {
		super(broadcastName, timesPerMinute);
		this.message = message;
	}
	
	@Override
	public String dataToSend() {
		return message;
	}

	
	
}
