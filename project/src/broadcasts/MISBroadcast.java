package broadcasts;

import data_types.MISReceiver;

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
		if(System.currentTimeMillis() > (millisTimeLastSend + (long)(timesPerMinute * 1000))){
			return true;
		}
		return false;
	}
	
	public String getBroadcastName(){
		return broadcastName;
	}
	
}
