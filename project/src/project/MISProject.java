package project;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data_types.MISExternalResource;
import data_types.MISNode;
import data_types.MISPort;
import data_types.MISScene;
import enums.MISListType;
import enums.MISProtocol;
import enums.MISType;
import scene.MISBroadcast;
import scene.MISBroadcastData;
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
		scenes = new ArrayList<MISScene>();
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
		
		JSONObject portsObjects = new JSONObject();
		if(MISProject.project.ports != null && MISProject.project.ports.size() > 0){
			for(int i = 0; i < MISProject.project.ports.size(); i++){
				JSONObject portObj = new JSONObject();
				portObj.put("port", MISProject.project.ports.get(i).port);
				portObj.put("protocol", MISProject.project.ports.get(i).protocol.toString());
				portsObjects.put(i, portObj);
			}
		}
		projectGeneralSettingsObject.put("ports", portsObjects);
		projectGeneralSettingsObject.put("ports_n", MISProject.project.ports.size());
		
		mainObject.put("project_settings", projectGeneralSettingsObject);
		
		JSONObject scenesObject = new JSONObject();
		mainObject.put("scenes_n", MISProject.project.scenes.size());
		if(MISProject.project.scenes.size() > 0){
			for(int i = 0; i < MISProject.project.scenes.size(); i++){
				MISScene scene = MISProject.project.scenes.get(i);
				JSONObject sceneObject = new JSONObject();
				sceneObject.put("name", scene.name);
				sceneObject.put("load_steps", scene.loadSteps);
				sceneObject.put("format", scene.format);
				sceneObject.put("id", scene.IDNumber);
				if(scene.nodeList.size() > 0){
					JSONObject nodesObject = new JSONObject();
					for(int j = 0; j < scene.nodeList.size(); j++){
						JSONObject nodeObject = new JSONObject();
						MISNode node = scene.nodeList.get(j);
						todo
						//save the node
					}
				}
				if(scene.broadcasts.size() > 0){
					JSONObject broadcastsObject = new JSONObject();
					for(int j = 0; j < scene.broadcasts.size(); j++){
						JSONObject broadcastObject = new JSONObject();
						MISBroadcast broadcast = scene.broadcasts.get(j);
						//Not sure if the below line works.
						broadcastObject.put("type", broadcast.getClass().getTypeName());
						broadcastObject.put("sps", broadcast.secondsPerSend);
						broadcastObject.put("data", broadcast.dataToSend());
						broadcastsObject.put(""+j, broadcastObject);
					}
					sceneObject.put("broadcasts", broadcastsObject);
				}
				if(scene.externalResources.size() > 0){
					JSONObject extresObject = new JSONObject();
					for(int j = 0; j < scene.externalResources.size(); j++){
						JSONObject resourceObject = new JSONObject();
						MISExternalResource resource = scene.externalResources.get(j);
						resourceObject.put("name", resource.name);
						resourceObject.put("id", resource.id);
						resourceObject.put("path", resource.path);
						resourceObject.put("type", resource.type);
						extresObject.put(""+j, resourceObject);
					}
					sceneObject.put("externalResources", extresObject);
				}
				scenesObject.put(""+i, sceneObject);
				
			}
		}
		mainObject.put("scenes", scenesObject);
		
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
			JSONObject scenes = (JSONObject) jsonObject.get("scenes");
			int numberOfScenes = toIntExact((Long) jsonObject.get("scenes_n"));
			for(int i = 0; i < numberOfScenes; i++){
				JSONObject sceneObject = (JSONObject) scenes.get(""+i);
				int id = toIntExact((Long) sceneObject.get("id"));
				MISScene scene = new MISScene(id);
				scene.format = toIntExact((Long) sceneObject.get("format"));
				scene.loadSteps = toIntExact((Long) sceneObject.get("load_steps"));
				scene.name = (String) sceneObject.get("name");
				todo
				//iterate over -> nodes, broadcasts, external resources
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
