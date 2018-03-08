package scene;

public class MISBroastcastMessage extends MISBroadcast{

	public String message;
	
	public MISBroastcastMessage(float secondsPerSend, String message) {
		super(secondsPerSend);
		this.message = message;
	}
	
	@Override
	public String dataToSend() {
		return message;
	}

	
	
}
