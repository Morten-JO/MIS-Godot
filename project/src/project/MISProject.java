package project;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import broadcasts.MISBroadcast;
import broadcasts.MISBroadcastData;
import broadcasts.MISBroadcastValue;
import broadcasts.MISBroadcastMessage;
import data_types.MIS2DTransform;
import data_types.MISBounds;
import data_types.MISExternalResource;
import data_types.MISPort;
import data_types.MISRoomSettings;
import data_types.MISScene;
import enums.MISListType;
import enums.MISProtocol;
import enums.MISType;
import loaders.MISLoader;
import nodes.MISControl;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISSpatial;
import receivers.MISReceiverAll;
import receivers.MISReceiverNotPerson;
import receivers.MISReceiverNotTeam;
import receivers.MISReceiverPerson;
import receivers.MISReceiverTeam;
import rules.MISRule;
import rules.MISRuleNode;
import rules.MISRuleNodePosition;
import rules.MISRuleNodeRotation;
import rules.MISRuleNodeScale;
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
		JSONObject mainObject = new JSONObject();
		try{
			MISProject.project.isSaving = true;
			
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
			projectGeneralSettingsObject.put("base_port", project.basePort.port);
			projectGeneralSettingsObject.put("base_protocol", project.basePort.protocol.toString());
			
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
					//Possibly broken unfinished code
					sceneObject.put("roomScene",scene.roomSettings != null);
					if(scene.roomSettings != null){
						sceneObject.put("roomSceneMinimum", scene.roomSettings.minimumPlayers);
						sceneObject.put("roomSceneMaximum", scene.roomSettings.maximumPlayers);
						sceneObject.put("roomSceneTeamSize", scene.roomSettings.teams);
						sceneObject.put("roomSceneAutoQueue", scene.roomSettings.autoQueue);
					}
					
					JSONObject nodesObject = new JSONObject();
					nodesObject.put("nodes_n", scene.nodeList.size());
					for(int j = 0; j < scene.nodeList.size(); j++){
						JSONObject nodeObject = new JSONObject();
						MISNode node = scene.nodeList.get(j);
						nodeObject.put("class", node.getClass().getSimpleName());
						if(node instanceof MISNode2D){
							MISNode2D node2D = (MISNode2D)node;
							nodeObject.put("positionX", node2D.transform.positionX);
							nodeObject.put("positionY", node2D.transform.positionY);
							nodeObject.put("rotation", node2D.transform.rotation);
							nodeObject.put("scaleX", node2D.transform.scaleX);
							nodeObject.put("scaleY", node2D.transform.scaleY);
						} else if(node instanceof MISSpatial){
							// TODO Auto-generated method stub
						} else if(node instanceof MISControl){
							// TODO Auto-generated method stub
						}
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
						nodeObject.put("shouldSendInformation", node.shouldSendInformation && node.informationReceivers != null);
						if(node.shouldSendInformation){
							if(node.informationReceivers != null){
								nodeObject.put("informationReceivers", node.informationReceivers.getClass().getSimpleName());
								if(node.informationReceivers instanceof MISReceiverPerson){
									nodeObject.put("informationReceiversPerson", ((MISReceiverPerson)node.informationReceivers).person);
								} else if(node.informationReceivers instanceof MISReceiverTeam){
									nodeObject.put("informationReceiversTeam", ((MISReceiverTeam)node.informationReceivers).team);
								} else if(node.informationReceivers instanceof MISReceiverNotPerson){
									nodeObject.put("informationReceiversNotPerson", ((MISReceiverNotPerson)node.informationReceivers).person);
								} else if(node.informationReceivers instanceof MISReceiverNotTeam){
									nodeObject.put("informationReceiversNotTeam", ((MISReceiverNotTeam)node.informationReceivers).team);
								}
							}
						}
						nodeObject.put("isControllable", node.isControllable && node.controlReceiver != null);
						if(node.isControllable){
							if(node.controlReceiver != null){
								nodeObject.put("controllableName", node.controlReceiver.getClass().getSimpleName());
								if(node.controlReceiver instanceof MISReceiverPerson){
									nodeObject.put("controllablePerson", ((MISReceiverPerson)node.controlReceiver).person);
								} else if(node.controlReceiver instanceof MISReceiverTeam){
									nodeObject.put("controllableTeam", ((MISReceiverTeam)node.controlReceiver).team);
								} else if(node.controlReceiver instanceof MISReceiverNotPerson){
									nodeObject.put("controllableNotPerson", ((MISReceiverNotPerson)node.controlReceiver).person);
								} else if(node.controlReceiver instanceof MISReceiverNotTeam){
									nodeObject.put("controllableNotTeam", ((MISReceiverNotTeam)node.controlReceiver).team);
								}
							}
							
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
						broadcastObject.put("name", broadcast.getBroadcastName());
						broadcastObject.put("type", broadcast.getClass().getSimpleName());
						broadcastObject.put("sps", broadcast.timesPerMinute);
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
						MISRule rule = scene.ruleList.get(j);
						ruleObject.put("name", rule.ruleName);
						ruleObject.put("class", rule.getClass().getName());
						if(rule instanceof MISRuleNode){
							int index = -1;
							for(int z = 0; z < scene.nodeList.size(); z++){
								if(scene.nodeList.get(z) == ((MISRuleNode)rule).node){
									index = z;
									break;
								}
							}
							ruleObject.put("node_index", index);
							ruleObject.put("option", ((MISRuleNode)rule).option.name());
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
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
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
				System.out.println("sps: "+broadcast.timesPerMinute);
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
			MISProtocol baseProtocol = MISProtocol.valueOf((String) projectSettings.get("base_protocol"));
			int basePortNumber = toIntExact((Long) projectSettings.get("base_port"));
			MISPort basePort = new MISPort(basePortNumber, baseProtocol);
			MISProject.project.basePort = basePort;
			
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
				boolean hasRoomInScene = (Boolean) sceneObject.get("roomScene");
				if(hasRoomInScene){
					int minimumPlayers = toIntExact((Long) sceneObject.get("roomSceneMinimum"));
					int maximumPlayers = toIntExact((Long) sceneObject.get("roomSceneMaximum"));
					boolean autoQueue = (Boolean) sceneObject.get("roomSceneAutoQueue");
					int teamSize = toIntExact((Long) sceneObject.get("roomSceneTeamSize"));
					MISRoomSettings settings = new MISRoomSettings(minimumPlayers, maximumPlayers, teamSize, autoQueue);
					scene.roomSettings = settings;
				}
				
				
				JSONObject nodesObject = (JSONObject) sceneObject.get("nodes");
				int amountNodes = toIntExact((Long) nodesObject.get("nodes_n"));
				for(int j = 0; j < amountNodes; j++){
					JSONObject nodeObject = (JSONObject) nodesObject.get(""+j);
					
					MISNode node;
					String className = (String) nodeObject.get("class");
					if(className.equals(MISNode2D.class.getSimpleName())){
						double positionX = (double) nodeObject.get("positionX");
						double positionY = (double) nodeObject.get("positionY");
						double rotation = (double) nodeObject.get("rotation");
						double scaleX = (double) nodeObject.get("scaleX");
						double scaleY = (double) nodeObject.get("scaleY");
						MISNode2D node2D = new MISNode2D(new MIS2DTransform(positionX, positionY, rotation, scaleX, scaleY));
						node = node2D;
					} else if(className.equals(MISSpatial.class.getSimpleName())){
						// TODO Auto-generated method stub
						node = new MISSpatial();
					} else if(className.equals(MISControl.class.getSimpleName())){
						// TODO Auto-generated method stub
						node = new MISControl();
					} else{
						node = new MISNode();
					}
					
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
						String parentName = (String) nodeObject.get("parent_name");
						int parentIndex = toIntExact((Long) nodeObject.get("parent_index"));
						for(int x = 0; x < scene.nodeList.size(); x++){
							if(scene.nodeList.get(x).name.equals(parentName) && scene.nodeList.get(x).index == parentIndex ){
								node.parent = scene.nodeList.get(x);
								break;
							}
						}
					}
					
					boolean shouldSendInfo = (Boolean) nodeObject.get("shouldSendInformation");
					if(shouldSendInfo){
						node.shouldSendInformation = shouldSendInfo;
						String shouldSendReceiverName = (String) nodeObject.get("informationReceivers");
						if(shouldSendReceiverName.equals(MISReceiverPerson.class.getSimpleName())){
							int person = toIntExact((Long)nodeObject.get("informationReceiversPerson"));
							node.informationReceivers = new MISReceiverPerson(person);
						} else if(shouldSendReceiverName.equals(MISReceiverAll.class.getSimpleName())){
							node.informationReceivers = new MISReceiverAll();
						} else if(shouldSendReceiverName.equals(MISReceiverTeam.class.getSimpleName())){
							int team = toIntExact((Long)nodeObject.get("informationReceiversTeam"));
							node.informationReceivers = new MISReceiverTeam(team);
						} else if(shouldSendReceiverName.equals(MISReceiverNotPerson.class.getSimpleName())){
							int person = toIntExact((Long)nodeObject.get("informationReceiversNotPerson"));
							node.informationReceivers = new MISReceiverNotPerson(person);
						} else if(shouldSendReceiverName.equals(MISReceiverNotTeam.class.getSimpleName())){
							int team = toIntExact((Long)nodeObject.get("informationReceiversNotTeam"));
							node.informationReceivers = new MISReceiverNotTeam(team);
						}
					}
					
					boolean isControllable = (Boolean) nodeObject.get("isControllable");
					if(isControllable){
						node.isControllable = isControllable;
						String controllableName = (String) nodeObject.get("controllableName");
						if(controllableName.equals(MISReceiverPerson.class.getSimpleName())){
							int person = toIntExact((Long)nodeObject.get("controllablePerson"));
							node.controlReceiver = new MISReceiverPerson(person);
						} else if(controllableName.equals(MISReceiverAll.class.getSimpleName())){
							node.controlReceiver = new MISReceiverAll();
						} else if(controllableName.equals(MISReceiverTeam.class.getSimpleName())){
							int team = toIntExact((Long)nodeObject.get("controllableTeam"));
							node.controlReceiver = new MISReceiverTeam(team);
						} else if(controllableName.equals(MISReceiverNotPerson.class.getSimpleName())){
							int person = toIntExact((Long)nodeObject.get("controllableNotPerson"));
							node.informationReceivers = new MISReceiverNotPerson(person);
						} else if(controllableName.equals(MISReceiverNotTeam.class.getSimpleName())){
							int team = toIntExact((Long)nodeObject.get("controllableNotTeam"));
							node.informationReceivers = new MISReceiverNotTeam(team);
						}
					}
					
					scene.addNode(node);
				}
				
				JSONObject broadcastsObject = (JSONObject) sceneObject.get("broadcasts");
				int amountBroadcasts = toIntExact((Long) broadcastsObject.get("broadcast_n"));
				for(int j = 0; j < amountBroadcasts; j++){
					JSONObject broadcastObject = (JSONObject) broadcastsObject.get(""+j);
					String name = (String) broadcastObject.get("name");
					String type = (String) broadcastObject.get("type");
					float secondsPerSend = (float)((double) broadcastObject.get("sps"));
					String data = (String) broadcastObject.get("data");
					MISBroadcast broadcast = null;
					if(type.equals(MISBroadcastData.class.getSimpleName())){
						broadcast = new MISBroadcastData(name, secondsPerSend);
					} else if(type.equals(MISBroadcastValue.class.getSimpleName())){
						broadcast = new MISBroadcastValue(name, secondsPerSend);
					} else if(type.equals(MISBroadcastMessage.class.getSimpleName())){
						broadcast = new MISBroadcastMessage(name, secondsPerSend, data);
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
				
				JSONObject rulesObject = (JSONObject) sceneObject.get("rules");
				if(rulesObject != null){
					int amountOfRules = toIntExact((Long) rulesObject.get("rules_n"));
					for(int j = 0; j < amountOfRules; j++){
						JSONObject ruleObject = (JSONObject) rulesObject.get(""+j);
						MISRule rule;
						String ruleName = (String) ruleObject.get("name");
						String type = (String) ruleObject.get("class");
						if(MISRuleNodePosition.class.getName().equals(type)){
							int nodeIndex = toIntExact((Long) ruleObject.get("node_index"));
							String option = (String) ruleObject.get("option");
							rule = new MISRuleNodePosition(ruleName, scene.nodeList.get(nodeIndex), MISRuleNode.options.valueOf(option));
							float xMin = ((Double)(ruleObject.get("xmin"))).floatValue();
							float xMax = ((Double)(ruleObject.get("xmax"))).floatValue();
							((MISRuleNodePosition)rule).xBounds = new MISBounds(xMin, xMax);
							float yMin = ((Double)(ruleObject.get("ymin"))).floatValue();
							float yMax = ((Double)(ruleObject.get("ymax"))).floatValue();
							((MISRuleNodePosition)rule).yBounds = new MISBounds(yMin, yMax);
							float zMin = ((Double)(ruleObject.get("zmin"))).floatValue();
							float zMax = ((Double)(ruleObject.get("zmax"))).floatValue();
							((MISRuleNodePosition)rule).zBounds = new MISBounds(zMin, zMax);
							scene.addRule(rule);
						} else if(MISRuleNodeRotation.class.getName().equals(type)){
							int nodeIndex = toIntExact((Long) ruleObject.get("node_index"));
							String option = (String) ruleObject.get("option");
							rule = new MISRuleNodeRotation(ruleName, scene.nodeList.get(nodeIndex), MISRuleNode.options.valueOf(option));
							float rotMin = ((Double)(ruleObject.get("rotmin"))).floatValue();
							float rotMax = ((Double)(ruleObject.get("rotmax"))).floatValue();
							((MISRuleNodeRotation)rule).rotationBounds = new MISBounds(rotMin, rotMax);
							scene.addRule(rule);
						} else if(MISRuleNodeScale.class.getName().equals(type)){
							int nodeIndex = toIntExact((Long) ruleObject.get("node_index"));
							String option = (String) ruleObject.get("option");
							rule = new MISRuleNodeScale(ruleName, scene.nodeList.get(nodeIndex), MISRuleNode.options.valueOf(option));
							float xMin = ((Double)(ruleObject.get("xmin"))).floatValue();
							float xMax = ((Double)(ruleObject.get("xmax"))).floatValue();
							((MISRuleNodeScale)rule).xBounds = new MISBounds(xMin, xMax);
							float yMin = ((Double)(ruleObject.get("ymin"))).floatValue();
							float yMax = ((Double)(ruleObject.get("ymax"))).floatValue();
							((MISRuleNodeScale)rule).yBounds = new MISBounds(yMin, yMax);
							scene.addRule(rule);
						}
					}
				}
				
				MISProject.project.scenes.add(scene);
			}
			MISProject.project.isLoading = false;
			return true;
		} catch (Exception e) {
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
	public MISPort basePort;
	public double minimumBuildVersion = 0.01;
	public ArrayList<MISScene> scenes;
	
	
	/*
	 * General settings
	 */
	public String projectName;
	public String projectLocation;
	public String godotProjectLocation; 
}
