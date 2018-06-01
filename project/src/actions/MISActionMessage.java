package actions;

import receivers.MISReceiver;

public class MISActionMessage extends MISAction{

	public String message;
	public MISReceiver receiver;
	
	public MISActionMessage(String message, MISReceiver receiver){
		this.message = message;
		this.receiver = receiver;
	}
}
