package project;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import actions.MISActionMessage;
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
import data_types.MISTempScene;
import enums.MISListType;
import enums.MISProtocol;
import enums.MISType;
import game_types.MISCompetetiveGameType;
import game_types.MISCustomGameType;
import game_types.MISEndlessGameType;
import game_types.MISGameType;
import loaders.MISLoader;
import nodes.MISControl;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISNodeScene;
import nodes.MISSpatial;
import receivers.MISReceiver;
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
import triggers.MISTrigger;
import triggers.MISTriggerValue;

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
			projectGeneralSettingsObject.put("ui_on_run", MISProject.project.uiOnRun);
			projectGeneralSettingsObject.put("main_ip", MISProject.project.ip);
			
			projectGeneralSettingsObject.put("roomScene", MISProject.project.roomSettings != null);
			if(MISProject.project.roomSettings != null){
				projectGeneralSettingsObject.put("roomSceneMinimum", MISProject.project.roomSettings.minimumPlayers);
				projectGeneralSettingsObject.put("roomSceneMaximum", MISProject.project.roomSettings.maximumPlayers);
				projectGeneralSettingsObject.put("roomSceneTeamSize", MISProject.project.roomSettings.teams);
				projectGeneralSettingsObject.put("roomSceneAutoQueue", MISProject.project.roomSettings.autoQueue);
				projectGeneralSettingsObject.put("roomSceneGameType", MISProject.project.roomSettings.gameType.getClass().getSimpleName());
				projectGeneralSettingsObject.put("roomSceneReferenceSceneId", MISProject.project.roomSettings.scene.IDNumber);
			}
			
			mainObject.put("project_settings", projectGeneralSettingsObject);
			
			JSONObject scenesObject = new JSONObject();
			mainObject.put("scenes_n", MISProject.project.scenes.size());
			if(MISProject.project.scenes.size() > 0){
				for(int i = 0; i < MISProject.project.scenes.size(); i++){
					MISScene scene = MISProject.project.scenes.get(i);
					JSONObject sceneObject = new JSONObject();
					sceneObject.put("name", scene.name);
					sceneObject.put("path", scene.path);
					sceneObject.put("load_steps", scene.loadSteps);
					sceneObject.put("format", scene.format);
					sceneObject.put("id", scene.IDNumber);
					
					
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
							MISSpatial spatial = (MISSpatial)node;
							nodeObject.put("xx", spatial.xx);
							nodeObject.put("yx", spatial.yx);
							nodeObject.put("zx", spatial.zx);
							
							nodeObject.put("xy", spatial.xy);
							nodeObject.put("yy", spatial.yy);
							nodeObject.put("zy", spatial.zy);
							
							nodeObject.put("xz", spatial.xz);
							nodeObject.put("yz", spatial.yz);
							nodeObject.put("zz", spatial.zz);
							
							nodeObject.put("xo", spatial.xo);
							nodeObject.put("yo", spatial.yo);
							nodeObject.put("zo", spatial.zo);
						} else if(node instanceof MISControl){
							// TODO Auto-generated method stub
						} else if(node instanceof MISNodeScene){
							MISNodeScene nodeScene = (MISNodeScene)node;
							nodeObject.put("nodeSceneReference", nodeScene.scene != null);
							if(nodeScene.scene != null){
								nodeObject.put("nodeSceneName", nodeScene.scene.name);
								nodeObject.put("nodeSceneIndex", nodeScene.scene.IDNumber);
							}
							nodeObject.put("extResourceReference", nodeScene.resource != null);
							if(nodeScene.resource != null){
								nodeObject.put("nodeExtResName", nodeScene.resource.name);
								nodeObject.put("nodeExtResId", nodeScene.resource.id);
								nodeObject.put("nodeExtResType", nodeScene.resource.type);
								nodeObject.put("nodeExtResPath", nodeScene.resource.path);
							}
							
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
						nodeObject.put("triggerSize", node.trigger.size());
						if(node.trigger.size() > 0 ){
							for(int l = 0; l < node.trigger.size(); l++){
								JSONObject triggerObject = new JSONObject();
								triggerObject.put("triggerClass", node.trigger.get(l).getClass().getSimpleName());
								if(node.trigger.get(l) instanceof MISTriggerValue){
									MISTriggerValue value = (MISTriggerValue) node.trigger.get(l);
									triggerObject.put("comparer", value.comparer);
									triggerObject.put("targetType", value.targetType);
									triggerObject.put("valueTarget", value.valueTarget);
									triggerObject.put("actionClass", value.action.getClass().getSimpleName());
									if(value.action instanceof MISActionMessage){
										MISActionMessage messageAction = (MISActionMessage) value.action;
										triggerObject.put("actionMessage", messageAction.message);
										triggerObject.put("actionReceiver", messageAction.receiver.getClass().getSimpleName());
										if(messageAction.receiver instanceof MISReceiverPerson){
											triggerObject.put("actionPerson", ((MISReceiverPerson)messageAction.receiver).person);
										} else if(messageAction.receiver instanceof MISReceiverTeam){
											triggerObject.put("actionTeam", ((MISReceiverTeam)messageAction.receiver).team);
										} else if(messageAction.receiver instanceof MISReceiverNotPerson){
											triggerObject.put("actionNotPerson", ((MISReceiverNotPerson)messageAction.receiver).person);
										} else if(messageAction.receiver instanceof MISReceiverNotTeam){
											triggerObject.put("actionNotTeam", ((MISReceiverNotTeam)messageAction.receiver).team);
										}
									}
								}
								nodeObject.put("trigger_"+l, triggerObject);
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
			//Possibly broken unfinished code
			
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
			boolean uiOnRun = (Boolean) projectSettings.get("ui_on_run");
			MISProject.project.uiOnRun = uiOnRun;
			String mainIp = (String)projectSettings.get("main_ip");
			MISProject.project.ip = mainIp;
			
			JSONObject scenes = (JSONObject) jsonObject.get("scenes");
			int numberOfScenes = toIntExact((Long) jsonObject.get("scenes_n"));
			for(int i = 0; i < numberOfScenes; i++){
				JSONObject sceneObject = (JSONObject) scenes.get(""+i);
				int id = toIntExact((Long) sceneObject.get("id"));
				MISScene scene = new MISScene(id);
				scene.format = toIntExact((Long) sceneObject.get("format"));
				scene.loadSteps = toIntExact((Long) sceneObject.get("load_steps"));
				scene.name = (String) sceneObject.get("name");
				scene.path = (String) sceneObject.get("path");
				
				
				
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
						double xx = (double) nodeObject.get("xx");
						double yx = (double) nodeObject.get("yx");
						double zx = (double) nodeObject.get("zx");
						
						double xy = (double) nodeObject.get("xy");
						double yy = (double) nodeObject.get("yy");
						double zy = (double) nodeObject.get("zy");
						
						double xz = (double) nodeObject.get("xz");
						double yz = (double) nodeObject.get("yz");
						double zz = (double) nodeObject.get("zz");
						
						double xo = (double) nodeObject.get("xo");
						double yo = (double) nodeObject.get("yo");
						double zo = (double) nodeObject.get("zo");
						MISSpatial nodeSpatial = new MISSpatial();
						nodeSpatial.xx = xx;
						nodeSpatial.yx = yx;
						nodeSpatial.zx = zx;
						
						nodeSpatial.xy = xy;
						nodeSpatial.yy = yy;
						nodeSpatial.zy = zy;
						
						nodeSpatial.xz = xz;
						nodeSpatial.yz = yz;
						nodeSpatial.zz = zz;
						
						nodeSpatial.xo = xo;
						nodeSpatial.yo = yo;
						nodeSpatial.zo = zo;
						node = nodeSpatial;
					} else if(className.equals(MISControl.class.getSimpleName())){
						// TODO Auto-generated method stub
						node = new MISControl();
					} else if(className.equals(MISNodeScene.class.getSimpleName())){
						System.err.println("IS OF CORRECT TYPE AKA MISNODESCENE");
						boolean nodeSceneReference = (Boolean) nodeObject.get("nodeSceneReference");
						boolean extResourceReference = (Boolean) nodeObject.get("extResourceReference");
						MISNodeScene nodeScene = new MISNodeScene();
						if(nodeSceneReference){
							System.err.println("Umm what?");
							String nodeSceneName = (String) nodeObject.get("nodeSceneName");
							int nodeSceneIndex = toIntExact((Long) nodeObject.get("nodeSceneIndex"));
							MISTempScene tempScene = new MISTempScene();
							tempScene.name = nodeSceneName;
							tempScene.IDNumber = nodeSceneIndex;
							nodeScene.scene = tempScene;
						}
						if(extResourceReference){
							String nodeExtResName = (String) nodeObject.get("nodeExtResName");
							int nodeExtResId = toIntExact((Long) nodeObject.get("nodeExtResId"));
							String nodeExtResType = (String) nodeObject.get("nodeExtResType");
							String nodeExtResPath = (String) nodeObject.get("nodeExtResPath");
							MISExternalResource extRes = new MISExternalResource();
							extRes.name = nodeExtResName;
							extRes.id = nodeExtResId;
							extRes.type = nodeExtResType;
							extRes.path = nodeExtResPath;
							nodeScene.resource = extRes;
						}
						node = nodeScene;
						
					} else {
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
					
					int triggerSize = toIntExact((Long)nodeObject.get("triggerSize"));
					for(int l = 0; l < triggerSize; l++){
						MISTrigger trigger = null;
						JSONObject triggerObject = (JSONObject) nodeObject.get("trigger_"+l);
						String triggerClass = (String ) triggerObject.get("triggerClass");
						if(triggerClass.equals(MISTriggerValue.class.getSimpleName())){
							MISTriggerValue.ValueComparer valueComparer = MISTriggerValue.ValueComparer.valueOf((String)triggerObject.get("comparer"));
							MISTriggerValue.TargetType targetType = MISTriggerValue.TargetType.valueOf((String)triggerObject.get("targetType"));
							double valueTarget = (Double) triggerObject.get("valueTarget");
							String actionClass = (String) triggerObject.get("actionClass");
							if(actionClass.equals(MISActionMessage.class.getSimpleName())){
								String actionMessage = 	(String) triggerObject.get("actionMessage");
								String actionReceiverClassName = (String)triggerObject.get("actionReceiver");
								MISReceiver receiver;
								if(actionReceiverClassName.equals(MISReceiverPerson.class.getSimpleName())){
									int person = toIntExact((Long) triggerObject.get("actionPerson"));
									receiver = new MISReceiverPerson(person);
								} else if(actionReceiverClassName.equals(MISReceiverTeam.class.getSimpleName())){
									int team = toIntExact((Long) triggerObject.get("actionTeam"));
									receiver = new MISReceiverTeam(team);
								} else if(actionReceiverClassName.equals(MISReceiverNotPerson.class.getSimpleName())){
									int notPerson = toIntExact((Long) triggerObject.get("actionNotPerson"));
									receiver = new MISReceiverNotPerson(notPerson);
								} else if(actionReceiverClassName.equals(MISReceiverNotTeam.class.getSimpleName())){
									int team = toIntExact((Long) triggerObject.get("actionNotTeam"));
									receiver = new MISReceiverNotTeam(team);
								} else{
									receiver = new MISReceiverAll();
								}
								MISActionMessage actionMessageObject = new MISActionMessage(actionMessage, receiver);
								trigger = new MISTriggerValue(actionMessageObject, valueTarget, valueComparer, targetType);
								
							}
						}
						if(trigger != null){
							node.trigger.add(trigger);
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
			boolean hasRoomInScene = (Boolean) projectSettings.get("roomScene");
			if(hasRoomInScene){
				int minimumPlayers = toIntExact((Long) projectSettings.get("roomSceneMinimum"));
				int maximumPlayers = toIntExact((Long) projectSettings.get("roomSceneMaximum"));
				boolean autoQueue = (Boolean) projectSettings.get("roomSceneAutoQueue");
				int teamSize = toIntExact((Long) projectSettings.get("roomSceneTeamSize"));
				String gameTypeClassName = (String) projectSettings.get("roomSceneGameType");
				int sceneId = toIntExact((Long) projectSettings.get("roomSceneReferenceSceneId"));
				MISScene ref = null;
				for(int i = 0; i < MISProject.project.scenes.size(); i++){
					if(MISProject.project.scenes.get(i).IDNumber == sceneId){
						ref = MISProject.project.scenes.get(i);
					}
				}
				MISRoomSettings settings;
				if(gameTypeClassName.equals(MISCompetetiveGameType.class.getSimpleName())){
					settings = new MISRoomSettings(ref, minimumPlayers, maximumPlayers, teamSize, autoQueue, new MISCompetetiveGameType());
				} else if(gameTypeClassName.equals(MISEndlessGameType.class.getSimpleName())){
					settings = new MISRoomSettings(ref, minimumPlayers, maximumPlayers, teamSize, autoQueue, new MISEndlessGameType());
				} else if(gameTypeClassName.equals(MISCustomGameType.class.getSimpleName())){
					settings = new MISRoomSettings(ref, minimumPlayers, maximumPlayers, teamSize, autoQueue, new MISCustomGameType());
				} else{
					settings = new MISRoomSettings(ref, minimumPlayers, maximumPlayers, teamSize, autoQueue, new MISGameType());
				}
				if(ref != null){
					MISProject.project.roomSettings = settings;
				} else{
					System.err.println("Refrence to scene is null.");
				}
				
			}
			
			for(int i = 0; i < MISProject.project.scenes.size(); i++){
				MISScene scene = MISProject.project.scenes.get(i);
				for(int j = 0; j < scene.nodeList.size(); j++){
					MISNode node = scene.nodeList.get(j);
					if(node instanceof MISNodeScene){
						MISNodeScene sceneNode = (MISNodeScene) node;
						try{
							sceneNode.recorrectScene(MISProject.project.scenes);
						} catch(NullPointerException e){
							e.printStackTrace();
						}
					}
				}
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
	public boolean uiOnRun = false;
	public String ip = "localhost";
	public MISRoomSettings roomSettings;
	
	/*
	 * General settings
	 */
	public String projectName;
	public String projectLocation;
	public String godotProjectLocation; 
}
