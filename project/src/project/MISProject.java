package project;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data_types.MISPort;
import data_types.MISScene;
import enums.MISListType;
import enums.MISProtocol;
import enums.MISType;
import settings.MISGeneralSettings;

import static java.lang.Math.toIntExact;

public class MISProject {

	public static MISProject project;
	private boolean isSaving = false;
	private boolean isLoading = false;
	
	public MISProject(){
		listType = MISListType.ARRAY;
		refreshRate = MISGeneralSettings.STANDARD_REFRESH_RATE;
		maxMessagesPerClientPerSecond = MISGeneralSettings.STANDARD_MAX_MESSAGES_PER_CLIENT_PER_SECOND;
		timeOutDuration = MISGeneralSettings.STANDARD_TIMEOUT;
		ports = new ArrayList<MISPort>();
	}
	
	public MISProject(String projectName, String projectLocation, MISType targetEngine){
		this();
		this.projectName = projectName;
		this.projectLocation = projectLocation;
		this.targetEngine = targetEngine;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Saves the current temporal project in the specified project location
	 * @return true or false
	 */
	public static boolean saveProject(){
		if(MISProject.project.isSaving){
			return false;
		}
		MISProject.project.isSaving = true;
		JSONObject mainObject = new JSONObject();
		
		JSONObject generalSettingsObject = new JSONObject();
		generalSettingsObject.put("name", MISProject.project.projectName);
		generalSettingsObject.put("target_engine", MISProject.project.targetEngine.toString());
		generalSettingsObject.put("minimum_build_version", MISProject.project.minimumBuildVersion);
		mainObject.put("general_settings", generalSettingsObject);
		
		JSONObject projectGeneralSettingsObject = new JSONObject();
		projectGeneralSettingsObject.put("list_type", MISProject.project.listType.toString());
		projectGeneralSettingsObject.put("refresh_rate", MISProject.project.refreshRate);
		projectGeneralSettingsObject.put("mmpcps", MISProject.project.maxMessagesPerClientPerSecond);
		
		if(MISProject.project.ports != null && MISProject.project.ports.size() > 0){
			JSONObject portsObjects = new JSONObject();
			for(int i = 0; i < MISProject.project.ports.size(); i++){
				JSONObject portObj = new JSONObject();
				portObj.put("port", MISProject.project.ports.get(i).port);
				portObj.put("protocol", MISProject.project.ports.get(i).protocol.toString());
				portsObjects.put(i, portObj);
			}
			projectGeneralSettingsObject.put("ports", portsObjects);
			projectGeneralSettingsObject.put("ports_n", MISProject.project.ports.size());
		}
		mainObject.put("project_settings", projectGeneralSettingsObject);
		try {
			FileWriter file = new FileWriter(MISProject.project.projectLocation+"/project.json");
			file.write(mainObject.toJSONString());
			file.flush();
			file.close();
			MISProject.project.isSaving = false;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		MISProject.project.isSaving = false;
		return false;
	}
	
	public static void main(String[] args) {
		MISProject.project = new MISProject("testprojectname", "resources/testresources", MISType.Godot);
		MISProject.project.ports.add(new MISPort(123, MISProtocol.TCP));
		MISProject.project.ports.add(new MISPort(1234, MISProtocol.UDP));
		if(MISProject.saveProject()){
			System.out.println("SAVED SUCCESSFULLY!");
		}
		loadProject("resources/testresources");
	}
	
	/**
	 * Loads the project into the temporal space based on projectlocation
	 * @param projectLocation
	 * @return true or false
	 */
	public static boolean loadProject(String projectLocation){
		if(MISProject.project.isLoading){
			return false;
		}
		MISProject.project.isLoading = true;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(projectLocation+"/project.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject generalSettings = (JSONObject) jsonObject.get("general_settings");
			String projectName = (String) generalSettings.get("name");
			MISType projectTargetEngine = MISType.valueOf((String) generalSettings.get("target_engine"));
			MISProject.project = new MISProject(projectName, projectLocation, projectTargetEngine);
			MISProject.project.minimumBuildVersion = (double) generalSettings.get("minimum_build_version");
			
			JSONObject projectSettings = (JSONObject) jsonObject.get("project_settings");
			MISProject.project.listType = MISListType.valueOf((String) projectSettings.get("list_type"));
			MISProject.project.refreshRate = toIntExact((Long) projectSettings.get("refresh_rate"));
			MISProject.project.maxMessagesPerClientPerSecond = toIntExact((Long) projectSettings.get("mmpcps"));
			
			JSONObject ports = (JSONObject) projectSettings.get("ports");
			int numberOfPorts = toIntExact((Long) projectSettings.get("ports_n"));
			for(int i = 0; i < numberOfPorts; i++){
				JSONObject port = (JSONObject) ports.get(i+"");
				int portNumber = toIntExact((Long) port.get("port"));
				MISProtocol protocol = MISProtocol.valueOf((String) port.get("protocol"));
				MISPort dataPort = new MISPort(portNumber, protocol);
				MISProject.project.ports.add(dataPort);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		MISProject.project.isLoading = false;
		return false;
	}
	
	/*
	 * Architecture settings
	 */
	public MISType targetEngine;
	public MISListType listType;
	public int refreshRate;
	public int maxMessagesPerClientPerSecond;
	public int timeOutDuration;
	public ArrayList<MISPort> ports;
	public double minimumBuildVersion = 0.01;
	public ArrayList<MISScene> scenes; //todo add to save/load
	/*
	 * General settings
	 */
	public String projectName;
	public String projectLocation;
}
