package connector;

import data_types.MISPackage;

public abstract class MISConnector {

	protected int port;
	
	public MISConnector(int port){
		this.port = port;
	}
	
	public abstract boolean start();
	public abstract boolean stop();
	public abstract boolean drop();
	public abstract boolean sendPacket(MISPackage packet);
	public int getPort(){
		return port;
	}	
}