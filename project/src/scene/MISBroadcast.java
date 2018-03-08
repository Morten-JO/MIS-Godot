package scene;

public abstract class MISBroadcast {
	
	public float secondsPerSend;
	private long millisTimeLastSend;
	
	public MISBroadcast(float secondsPerSend){
		this.secondsPerSend = secondsPerSend;
		millisTimeLastSend = System.currentTimeMillis();
	}
	
	public abstract String dataToSend();
	
	public boolean shouldSend(){
		if(System.currentTimeMillis() > (millisTimeLastSend + (long)(secondsPerSend * 1000))){
			return true;
		}
		return false;
	}
	
}
