package data_types;

import game_types.MISGameType;

public class MISRoomSettings {

	public boolean autoQueue;
	public int minimumPlayers;
	public int maximumPlayers;
	public int teams;
	public MISGameType gameType;
	public MISScene scene;
	
	public MISRoomSettings(MISScene scene, int minimumPlayers, int maximumPlayers, int teams, boolean autoQueue, MISGameType gameType){
		this.scene = scene;
		this.minimumPlayers = minimumPlayers;
		this.maximumPlayers = maximumPlayers;
		this.teams = teams;
		this.autoQueue = autoQueue;
		this.gameType = gameType;
	}
	
}
