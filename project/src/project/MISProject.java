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
import loaders.MISLoader;
import rules.MISRule;
import rules.MISRuleNode;
import rules.MISRuleNodePosition;
import rules.MISRuleNodeRotation;
import rules.MISRuleNodeScale;
import scene.MISBroadcast;
import scene.MISBroadcastData;
import scene.MISBroadcastValue;
import scene.MISBroastcastMessage;
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
		generalSettingsObject.put("godot_project_location", MISProject.project.godotProjectLocation);
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
				
				JSONObject nodesObject = new JSONObject();
				nodesObject.put("nodes_n", scene.nodeList.size());
				for(int j = 0; j < scene.nodeList.size(); j++){
					JSONObject nodeObject = new JSONObject();
					MISNode node = scene.nodeList.get(j);
					nodeObject.put("name", node.name);
					nodeObject.put("type", node.type);
					nodeObject.put("index", node.index);
					nodeObject.put("script_attached", node.scriptAttached);
					if(node.scriptAttached){
						nodeObject.put("script_name", node.scriptName);
						nodeObject.put("script_id", node.scriptId);
					}
					nodeObject.put("parent", node.parent != null);
					if(node.parent != null){
						nodeObject.put("parent_name", node.parent.name);
						nodeObject.put("parent_index", node.parent.index);
					}
					nodesObject.put(""+j, nodeObject);
				}
				sceneObject.put("nodes", nodesObject);
				
				JSONObject broadcastsObject = new JSONObject();
				broadcastsObject.put("broadcast_n", scene.broadcasts.size());
				for(int j = 0; j < scene.broadcasts.size(); j++){
					JSONObject broadcastObject = new JSONObject();
					MISBroadcast broadcast = scene.broadcasts.get(j);
					//Not sure if the below line works.
					broadcastObject.put("type", broadcast.getClass().getSimpleName());
					broadcastObject.put("sps", broadcast.secondsPerSend);
					broadcastObject.put("data", broadcast.dataToSend());
					broadcastsObject.put(""+j, broadcastObject);
				}
				sceneObject.put("broadcasts", broadcastsObject);
				
				JSONObject extresObject = new JSONObject();
				extresObject.put("external_resources_n", scene.externalResources.size());
				
				for(int j = 0; j < scene.externalResources.size(); j++){
					JSONObject resourceObject = new JSONObject();
					MISExternalResource resource = scene.externalResources.get(j);
					resourceObject.put("name", resource.name);
					resourceObject.put("id", resource.id);
					resourceObject.put("path", resource.path);
					resourceObject.put("type", resource.type);
					extresObject.put(""+j, resourceObject);
				}
				
				JSONObject rulesObject = new JSONObject();
				rulesObject.put("rules_n", scene.ruleList.size());
				
				for(int j = 0; j < scene.ruleList.size(); j++){
					JSONObject ruleObject = new JSONObject();
					MISRule rule = scene.ruleList.get(i);
					ruleObject.put("name", rule.ruleName);
					ruleObject.put("class", rule.getClass());
					if(rule instanceof MISRuleNode){
						int index = -1;
						for(int z = 0; z < scene.nodeList.size(); z++){
							if(scene.nodeList.get(i) == ((MISRuleNode)rule).node){
								index = z;
								break;
							}
						}
						ruleObject.put("node_index", ""+index);
						ruleObject.put("option", ((MISRuleNode)rule).option);
						if(rule instanceof MISRuleNodePosition){
							MISRuleNodePosition pos = (MISRuleNodePosition)rule;
							ruleObject.put("xmin", pos.xBounds.min);
							ruleObject.put("xmax", pos.xBounds.max);
							ruleObject.put("ymin", pos.yBounds.min);
							ruleObject.put("ymax", pos.yBounds.max);
							ruleObject.put("zmin", pos.zBounds.min);
							ruleObject.put("zmax", pos.zBounds.max);
						} else if(rule instanceof MISRuleNodeRotation){
							MISRuleNodeRotation rot = (MISRuleNodeRotation)rule;
							ruleObject.put("rotMin", rot.rotationBounds.min);
							ruleObject.put("rotMax", rot.rotationBounds.max);
						} else if(rule instanceof MISRuleNodeScale){
							MISRuleNodeScale scale = (MISRuleNodeScale)rule;
							ruleObject.put("xmin", scale.xBounds.min);
							ruleObject.put("xmax", scale.xBounds.max);
							ruleObject.put("ymin", scale.yBounds.min);
							ruleObject.put("ymax", scale.yBounds.max);
						}
					}
					rulesObject.put(""+j, ruleObject);
				}
					
				sceneObject.put("externalResources", extresObject);
				sceneObject.put("rules", rulesObject);
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
		/*MISProject.project = new MISProject("testprojectname", "resources/testresources", MISType.Godot);
		MISProject.project.ports.add(new MISPort(123, MISProtocol.TCP));
		MISProject.project.ports.add(new MISPort(1234, MISProtocol.UDP));
		MISProject.project.scenes.add(MISLoader.loadSceneByLocation("resources/testresources/Sce.tscn"));
		for(int i = 0; i < MISProject.project.scenes.size(); i++){
			MISProject.project.scenes.get(i).addBroadcast(new MISBroastcastMessage(1.0f, "testMessage"));
		}
		if(MISProject.saveProject()){
			System.out.println("SAVED SUCCESSFULLY!");
		}*/
		loadProject("resources/testresources");
		System.out.println("ProjectName: "+MISProject.project.projectName);
		MISProject.printProjectData();
	}
	
	public static void printProjectData(){
		System.out.println();
		System.out.println("Printing Project data!");
		System.out.println("ProjectName: "+MISProject.project.projectName);
		System.out.println("ProjectLocation: "+MISProject.project.projectLocation);
		System.out.println("Refresh rate: "+MISProject.project.refreshRate);
		System.out.println("mmpcps: "+MISProject.project.maxMessagesPerClientPerSecond);
		System.out.println("minimum Build Version: "+MISProject.project.minimumBuildVersion);
		System.out.println("Target engine: "+MISProject.project.targetEngine);
		System.out.println("Timeout duration: "+MISProject.project.timeOutDuration);
		System.out.println("List type: "+MISProject.project.listType);
		for(int i  = 0; i < MISProject.project.ports.size(); i++){
			System.out.println();
			System.out.println("For port #"+i);
			MISPort port = MISProject.project.ports.get(i);
			System.out.println("Port: "+port.port);
			System.out.println("Protocol: "+port.protocol);
		}
		for(int j = 0; j < MISProject.project.scenes.size(); j++){
			System.out.println();
			System.out.println("Scene data for #"+j);
			MISScene scene = MISProject.project.scenes.get(j);
			System.out.println("Scene name: "+scene.name);
			System.out.println("Scene load_steps: "+scene.loadSteps);
			System.out.println("Scene formats: "+scene.format);
			System.out.println("Scene id_number: "+scene.IDNumber);
			for(int i = 0; i < scene.nodeList.size(); i++){
				MISNode node = scene.nodeList.get(i);
				System.out.println();
				System.out.println("For node #"+i);
				System.out.println("Name: "+node.name);
				System.out.println("Type: "+node.type);
				if(node.parent != null){
					System.out.println("Parent: "+node.parent.name);
					System.out.println("Parent_Index: "+node.parent.index);
				}
				System.out.println("index: "+node.index);
				if(node.scriptAttached){
					System.out.println("Script_Name: "+node.scriptName);
					System.out.println("Script_Id: "+node.scriptId);
				}
			}
			for(int i = 0; i < scene.broadcasts.size(); i++){
				MISBroadcast broadcast = scene.broadcasts.get(i);
				System.out.println();
				System.out.println("For broadcast #"+i);
				System.out.println("sps: "+broadcast.secondsPerSend);
				System.out.println("type: "+broadcast.getClass().getSimpleName());
				System.out.println("data: "+broadcast.dataToSend());
			}
			for(int i = 0; i < scene.externalResources.size(); i++){
				MISExternalResource resource = scene.externalResources.get(i);
				System.out.println();
				System.out.println("For resource #"+i);
				System.out.println("name: "+resource.name);
				System.out.println("path: "+resource.path);
				System.out.println("type: "+resource.type);
				System.out.println("id: "+resource.id);
			}
		}
	}
	
	/**
	 * Loads the project into the temporal space based on projectlocation
	 * @param projectLocation
	 * @return true or false
	 */
	public static boolean loadProject(String projectLocation){
		if(MISProject.project != null){
			if(MISProject.project.isLoading){
				return false;
			}
		}
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(projectLocation+"/project.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject generalSettings = (JSONObject) jsonObject.get("general_settings");
			String projectName = (String) generalSettings.get("name");
			MISType projectTargetEngine = MISType.valueOf((String) generalSettings.get("target_engine"));
			MISProject.project = new MISProject(projectName, projectLocation, projectTargetEngine);
			if(!MISProject.project.isLoading){
				MISProject.project.isLoading = true;
			}
			MISProject.project.minimumBuildVersion = (double) generalSettings.get("minimum_build_version");
			MISProject.project.godotProjectLocation = (String) generalSettings.get("godot_project_location");
			
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

				JSONObject nodesObject = (JSONObject) sceneObject.get("nodes");
				int amountNodes = toIntExact((Long) nodesObject.get("nodes_n"));
				for(int j = 0; j < amountNodes; j++){
					JSONObject nodeObject = (JSONObject) nodesObject.get(""+j);
					MISNode node = new MISNode();
					node.name = (String) nodeObject.get("name");
					node.type = (String) nodeObject.get("type");
					node.index = toIntExact((Long) nodeObject.get("index"));
					node.scriptAttached = (Boolean) nodeObject.get("script_attached");
					if(node.scriptAttached){
						node.scriptName = (String) nodeObject.get("script_name");
						node.scriptId = toIntExact((Long) nodeObject.get("script_id"));
					}
					boolean hasParent = (Boolean) nodeObject.get("parent");
					if(hasParent){
						node.parent = new MISNode();
						node.parent.name = (String) nodeObject.get("parent_name");
						node.parent.index = toIntExact((Long) nodeObject.get("parent_index"));
					}
					scene.addNode(node);
				}
				
				JSONObject broadcastsObject = (JSONObject) sceneObject.get("broadcasts");
				int amountBroadcasts = toIntExact((Long) broadcastsObject.get("broadcast_n"));
				for(int j = 0; j < amountBroadcasts; j++){
					JSONObject broadcastObject = (JSONObject) broadcastsObject.get(""+j);
					String type = (String) broadcastObject.get("type");
					float secondsPerSend = (float)((double) broadcastObject.get("sps"));
					String data = (String) broadcastObject.get("data");
					MISBroadcast broadcast = null;
					if(type.equals(MISBroadcastData.class.getSimpleName())){
						broadcast = new MISBroadcastData(secondsPerSend);
					} else if(type.equals(MISBroadcastValue.class.getSimpleName())){
						broadcast = new MISBroadcastValue(secondsPerSend);
					} else if(type.equals(MISBroastcastMessage.class.getSimpleName())){
						broadcast = new MISBroastcastMessage(secondsPerSend, data);
					}
					if(broadcast != null){
						scene.addBroadcast(broadcast);
					} 
				}
				
				JSONObject externalResourcesObject = (JSONObject) sceneObject.get("externalResources");
				int amountResources = toIntExact((Long) externalResourcesObject.get("external_resources_n"));
				for(int j = 0; j < amountResources; j++){
					JSONObject externalResourceObject = (JSONObject) externalResourcesObject.get(""+j);
					MISExternalResource resource = new MISExternalResource();
					resource.name = (String) externalResourceObject.get("name");
					resource.path = (String) externalResourceObject.get("path");
					resource.type = (String) externalResourceObject.get("type");
					resource.id = toIntExact((Long) externalResourceObject.get("id"));
					scene.addExternalResource(resource);
				}
				MISProject.project.scenes.add(scene);
			}
			MISProject.project.isLoading = false;
			return true;
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
	public String godotProjectLocation; 
}
