package settings;

import enums.MISListType;
import enums.MISProtocol;

public class MISGeneralSettings {
	/*
	 * Architecture settings
	 */
	public static MISListType listType = MISListType.ARRAY;
	public static int refreshRate = 60; //how many times the server will send per second
	public static int maxMessagesPerClientPerSecond = 20; //how many messages the server will process during one specific period(rest will be disgarded)
	public static int timeOutDuration = 3600; //in seconds
	public static int[] ports = new int[]{123}; //In array for allowing differnet types
	public static MISProtocol[] protocols = new MISProtocol[]{MISProtocol.TCP}; // Is linked with above.
	public static double minimumBuildVersion = 0.01;
	
	/*
	 * General seetings
	 */
	public static String projectLocation = "";
	
}
