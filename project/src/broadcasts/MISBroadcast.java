package broadcasts;

import receivers.MISReceiver;

public abstract class MISBroadcast {
	
	public MISReceiver receiver;
	
	public float timesPerMinute;
	private long millisTimeLastSend;
	private String broadcastName;
	
	
	public MISBroadcast(String broadcastName, float timesPerMinute){
		this.timesPerMinute = timesPerMinute;
		this.broadcastName = broadcastName;
		millisTimeLastSend = System.currentTimeMillis();
	}
	
	public abstract String dataToSend();
	
	public boolean shouldSend(){
		if(System.currentTimeMillis() > (millisTimeLastSend + (long)(60000 / timesPerMinute))){
			millisTimeLastSend = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	public Long getTimeForNextSend(){
		return (millisTimeLastSend + (long)(60000 / timesPerMinute)) - System.currentTimeMillis();
	}
	
	public String getBroadcastName(){
		return broadcastName;
	}
	
}
