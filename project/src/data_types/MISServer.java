package data_types;

import java.net.InetAddress;

public class MISServer {

	public MISRegion region;
	public String name;
	public String ip;
	
	public MISServer(String name, String ip, MISRegion region){
		this.ip = ip;
		this.name = name;
		this.region = region;
		
	}
	
}
