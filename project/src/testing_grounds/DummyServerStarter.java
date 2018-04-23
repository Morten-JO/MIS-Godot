package testing_grounds;

import server.Server;

public class DummyServerStarter {

	public static void main(String[] args) {
		Server server = new Server(1000, true, false);
	}
	
}
