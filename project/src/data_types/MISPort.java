package data_types;

import enums.MISProtocol;

public class MISPort {

	public int port;
	public MISProtocol protocol;
	
	public MISPort(int port, MISProtocol protocol){
		this.port = port;
		this.protocol = protocol;
	}
	
}
