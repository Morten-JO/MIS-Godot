package data_types;

public class MISRoomSettings {

	public boolean autoQueue;
	public int minimumPlayers;
	public int maximumPlayers;
	public int teams;
	
	public MISRoomSettings(int minimumPlayers, int maximumPlayers, int teams, boolean autoQueue){
		this.minimumPlayers = minimumPlayers;
		this.maximumPlayers = maximumPlayers;
		this.teams = teams;
		this.autoQueue = autoQueue;
	}
	
}
