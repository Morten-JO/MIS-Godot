package scriptbuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import actions.MISActionMessage;
import data_types.MISScene;
import game_types.MISCompetetiveGameType;
import nodes.MISControl;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISNodeScene;
import nodes.MISSpatial;
import project.MISProject;
import receivers.MISReceiverAll;
import receivers.MISReceiverNotPerson;
import receivers.MISReceiverNotTeam;
import receivers.MISReceiverPerson;
import receivers.MISReceiverTeam;
import triggers.MISTrigger;
import triggers.MISTriggerValue;

public class ScriptBuilder {

	private static String tcpConnectionVariableName = "tcp_connection";
	private static String tcpConnectionVariableIntialize = "StreamPeerTCP.new()";
	
	private static String baseServerIpVariableName = "base_server_ip";
	private static String teamIdVariableName = "team_id";
	private static String playerIdVariableName = "player_id";
	private static String timeCounterVariableName = "time_counter";
	private static String roomBegunVariableName = "room_begun";
	private static String splittedStringsVariableName = "splitted_strings";
	
	private static String timeoutDurationConst = "TIMEOUT_DURATION_CONNECT = 10";
	private static String minimumClientBuild;
	
	public static void main(String[] args) {
	}
	
	public static  boolean buildScript(MISProject project, MISScene scene, String name, String nodeType, String ip){
		minimumClientBuild = "MINIMUM_BUILD = "+project.minimumBuildVersion;
		String scriptString = "";
		
		scriptString = preVariableGeneration(scriptString, project, nodeType);
		
		scriptString = variableGeneration(scriptString, scene, ip);
		
		scriptString = readyFunctionGeneration(scriptString);
		
		scriptString = processFunctionGeneration(scriptString);
		
		scriptString = processFunctionRoomMessagesGeneration(scriptString, scene);
		
		scriptString = processFunctionControlUpdatesGeneration(scriptString, scene);
		
		scriptString = processFunctionTcpControlGeneration(scriptString);
		
		scriptString = connectFunctionGeneration(scriptString, ip, project);
		
		scriptString = customRoomBegunFunctionGeneration(scriptString, scene);		
		
		scriptString = queueFunctionsGeneration(scriptString, scene);
		
		scriptString = broadcastFunctionsGeneration(scriptString, scene);
		
		scriptString = gameActionFunctionsGeneration(scriptString, scene);
		
		scriptString = refreshFunctionsGeneration(scriptString, scene);
		
		scriptString = closeUpServerFunctionGeneration(scriptString);
		
		scriptString = nodeControlFunctionsGeneration(scriptString, scene);
		
		scriptString = triggerMessageFunctionsGeneration(scriptString, scene);
		
		JFileChooser directoryChooser = new JFileChooser();
		directoryChooser.setCurrentDirectory(new File("."));
		directoryChooser.setDialogTitle("Choose a directory for generation");
		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		directoryChooser.setAcceptAllFileFilterUsed(false);
		
		if(directoryChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			try {
				PrintWriter writer = new PrintWriter(directoryChooser.getSelectedFile()+"\\"+name+".gd", "UTF-8");
				writer.println(scriptString);
				writer.close();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static String createLineBreaks(int linebreaks){
		String str = "";
		for(int i = 0; i < linebreaks; i++){
			str += System.lineSeparator();
		}
		return str;
	}
	
	private static String createIndentations(int indents){
		String str = "";
		for(int i = 0; i < indents; i++){
			str += "\t";
		}
		return str;
	}
	
	private static String processFunctionRoomMessagesGeneration(String scriptString, MISScene scene){
		//roomcreate
		scriptString += createIndentations(3)+"if(\"roomcreate\" in "+splittedStringsVariableName+"[i]):"+createLineBreaks(1);
		scriptString += createIndentations(4)+"var split_messages = "+splittedStringsVariableName+"[i].split(\" \")"+createLineBreaks(1);
		scriptString += createIndentations(4)+"var room_id = split_messages[1]"+createLineBreaks(1);
		scriptString += createIndentations(4)+"var room_scene_id = split_messages[2]"+createLineBreaks(1);
		scriptString += createIndentations(4)+"var total_players = split_messages[3]"+createLineBreaks(1);
		scriptString += createIndentations(4)+teamIdVariableName+" = int(split_messages[4])"+createLineBreaks(1);
		scriptString += createIndentations(4)+playerIdVariableName+" = int(split_messages[5])"+createLineBreaks(1);
		scriptString += createIndentations(4)+"roomBegun(split_messages)"+createLineBreaks(2);

		scriptString += createIndentations(3)+"if(\"game_end\" in "+splittedStringsVariableName+"[i]):"+createLineBreaks(1);
		scriptString += createIndentations(4)+"var split_messages = "+splittedStringsVariableName+"[i].split(\" \")"+createLineBreaks(1);
		scriptString += createIndentations(4)+"var winning_team = int(split_messages[1])"+createLineBreaks(1);
		scriptString += createIndentations(4)+"onReceiveGameEnd(winning_team)"+createLineBreaks(2);
		
		if(MISProject.project.roomSettings.gameType instanceof MISCompetetiveGameType){
			scriptString += createIndentations(3)+"if (\"game_won\" in "+splittedStringsVariableName+"[i]):"+createLineBreaks(1);
			scriptString += createIndentations(4)+"var split_messages = "+splittedStringsVariableName+"[i].split(\" \")"+createLineBreaks(1);
			scriptString += createIndentations(4)+"var winning_team = int(split_messages[1])"+createLineBreaks(1);
			scriptString += createIndentations(4)+"onReceiveGameWon(winning_team)"+createLineBreaks(2);
		}
		
		for(int i = 0; i < scene.nodeList.size(); i++){
			MISNode node = scene.nodeList.get(i);
			for(int j = 0; j < scene.nodeList.get(i).trigger.size(); j++){
				scriptString += createIndentations(3)+"if(\"trigger_"+node.name+"_"+node.index+"_"+j+"\" in "+splittedStringsVariableName+"[i]):"+createLineBreaks(1);
				inmplement so the message is sent
				scriptString += createIndentations(4)+"onReceivedTrigger_"+node.name+"_"+node.index+"_"+j+"()"+createLineBreaks(1);
				scriptString += createIndentations(4)+"pass"+createLineBreaks(2);
			}
		}
		
		//node message(updates)
		scriptString += createIndentations(3)+"if(\"node\" in "+splittedStringsVariableName+"[i]):"+createLineBreaks(1);
		scriptString += createIndentations(4)+"var split_message = "+splittedStringsVariableName+"[i].split(\" \")"+createLineBreaks(1);
		for(int i = 0; i < scene.nodeList.size(); i++){
			if(scene.nodeList.get(i).shouldSendInformation && scene.nodeList.get(i).informationReceivers != null){
				scriptString += createIndentations(4)+"#"+scene.nodeList.get(i).name+createLineBreaks(1);
				scriptString += createIndentations(4)+"if int(split_message[2]) == "+scene.nodeList.get(i).index+":"+createLineBreaks(1);
				if(scene.nodeList.get(i).informationReceivers instanceof MISReceiverTeam){
					MISReceiverTeam teamNode = (MISReceiverTeam) scene.nodeList.get(i).informationReceivers;
					scriptString += createIndentations(5)+"if "+teamIdVariableName+" == "+teamNode.team+":"+createLineBreaks(1);
					scriptString = refreshNodeString(scriptString, scene.nodeList.get(i), 6);
				} else if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverPerson){
					MISReceiverPerson personReceiver = (MISReceiverPerson) scene.nodeList.get(i).informationReceivers;
					scriptString += createIndentations(5)+"if "+playerIdVariableName+" == "+personReceiver.person+":"+createLineBreaks(1);
					scriptString = refreshNodeString(scriptString, scene.nodeList.get(i), 6);
				} else if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverNotTeam){
					MISReceiverNotTeam teamNode = (MISReceiverNotTeam) scene.nodeList.get(i).informationReceivers;
					scriptString += createIndentations(5)+"if "+teamIdVariableName+" != "+teamNode.team+":"+createLineBreaks(1);
					scriptString = refreshNodeString(scriptString, scene.nodeList.get(i), 6);
				} else if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverNotPerson){
					MISReceiverNotPerson personReceiver = (MISReceiverNotPerson) scene.nodeList.get(i).informationReceivers;
					scriptString += createIndentations(5)+"if "+playerIdVariableName+" != "+personReceiver.person+":"+createLineBreaks(1);
					scriptString = refreshNodeString(scriptString, scene.nodeList.get(i), 6);
				} else{
					scriptString = refreshNodeString(scriptString, scene.nodeList.get(i), 5);
				}
			}
		}
				
		scriptString += createIndentations(4)+"pass"+createLineBreaks(4);
		return scriptString;
	}
	
	private static String refreshNodeString(String scriptString, MISNode node, int indents){
		String nameOfNode = getVariableNameForNode(node);
		if(node instanceof MISNode2D){
			scriptString += createIndentations(indents)+nameOfNode+".set_pos(Vector2(float(split_message[4]), float(split_message[5])))"+createLineBreaks(1);
			scriptString += createIndentations(indents)+nameOfNode+".set_rot(float(split_message[6]))"+createLineBreaks(1);
			scriptString += createIndentations(indents)+nameOfNode+".set_scale(Vector2(float(split_message[7]), float(split_message[8])))"+createLineBreaks(1);
		} else if(node instanceof MISControl){
			scriptString += createIndentations(indents)+"pass #Error, MISControl, not implemented"+createLineBreaks(1);
		} else if(node instanceof MISSpatial){
			scriptString += createIndentations(indents)+nameOfNode+".set_transform(Vector3(float(split_message[4]), float(split_message[5]), float(split_message[6])), Vector3(float(split_message[7]), float(split_message[8]), float(split_message[9])), Vector3(float(split_message[10]), float(split_message[11]), float(split_message[12])), Vector3(float(split_message[13]), float(split_message[14]), float(split_message[15])))"+createLineBreaks(1);
		} else if(node instanceof MISNodeScene){
			MISNodeScene nodeInQuestion = (MISNodeScene)node;
			if(nodeInQuestion.headNode instanceof MISNode2D){
				scriptString += createIndentations(indents)+nameOfNode+".set_pos(Vector2(float(split_message[4]), float(split_message[5])))"+createLineBreaks(1);
				scriptString += createIndentations(indents)+nameOfNode+".set_rot(float(split_message[6]))"+createLineBreaks(1);
				scriptString += createIndentations(indents)+nameOfNode+".set_scale(Vector2(float(split_message[7]), float(split_message[8])))"+createLineBreaks(1);
			} else if(nodeInQuestion.headNode instanceof MISSpatial){
				scriptString += createIndentations(indents)+nameOfNode+".set_transform(Vector3(float(split_message[4]), float(split_message[5]), float(split_message[6])), Vector3(float(split_message[7]), float(split_message[8]), float(split_message[9])), Vector3(float(split_message[10]), float(split_message[11]), float(split_message[12])), Vector3(float(split_message[13]), float(split_message[14]), float(split_message[15])))"+createLineBreaks(1);
			}
		}
		return scriptString;
	}
	
	private static String getVariableNameForNode(MISNode node){
		String nameOfNode = "";
		MISNode nodeTemp = node;
		while(nodeTemp.parent != null){
			nameOfNode += nodeTemp.parent.name+"_";
			nodeTemp = nodeTemp.parent;
		}
		nameOfNode += node.name;
		return nameOfNode;
	}
	
	private static String variableGeneration(String scriptString, MISScene scene, String ip){
		//Load onready node variables
		for(int i = 1; i < scene.nodeList.size(); i++){
			String onreadyString = "onready var ";
			String nameOfNode = getVariableNameForNode(scene.nodeList.get(i));
			onreadyString += nameOfNode+" = get_node(\"";
			String locationNode = "";
			MISNode node = scene.nodeList.get(i);
			//find upper node
			List<MISNode> nodeParentList = new ArrayList<MISNode>();
			while(node.parent != null && node.parent.index != 0){
				node = node.parent;
				nodeParentList.add(node);
			}
			for(int j = nodeParentList.size()-1; j >= 0; j--){
				locationNode += nodeParentList.get(j).name+"/";
			}
			locationNode += scene.nodeList.get(i).name+"\")";
			onreadyString += locationNode+createLineBreaks(1);
			scriptString += onreadyString;
		}
				
		scriptString += createLineBreaks(2);
				
		//Create variables and variablenames
		scriptString += "var "+tcpConnectionVariableName+" = "+tcpConnectionVariableIntialize+createLineBreaks(1);
		scriptString += "var "+baseServerIpVariableName+" = "+"\""+ip+"\""+createLineBreaks(1);
		scriptString += "var "+teamIdVariableName+createLineBreaks(1);
		scriptString += "var "+roomBegunVariableName+" = false"+createLineBreaks(1);
		scriptString += "var "+timeCounterVariableName+" = 0";
		scriptString += createLineBreaks(2);
				
		//Create consts
		scriptString += "const "+timeoutDurationConst+createLineBreaks(1);
		scriptString += "const "+minimumClientBuild+createLineBreaks(1);
			
		scriptString += createLineBreaks(2);
		return scriptString;
	}
	
	private static String preVariableGeneration(String scriptString, MISProject project, String nodeType){
		//Create pre variable things
		scriptString = "extends "+nodeType+createLineBreaks(2);
		scriptString += "#generated by MIS-Godot"+createLineBreaks(1);
		scriptString += "#minimum build version "+project.minimumBuildVersion+createLineBreaks(1);
		scriptString += "#project name: "+project.projectName+createLineBreaks(2);
		return scriptString;
	}
	
	private static String readyFunctionGeneration(String scriptString){
		//Create ready func
		scriptString += "func _ready():"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass";
				
		scriptString += createLineBreaks(2);
		return scriptString;
	}
	
	private static String processFunctionGeneration(String scriptString){
		String receivedMessageVariableName = "received_message";
		
		//Create process function
		scriptString += "func _process(delta):"+createLineBreaks(1);
		
		//received messages start
		scriptString += createIndentations(1)+"if "+tcpConnectionVariableName+".is_connected() && "+tcpConnectionVariableName+".get_available_bytes() > 0:"+createLineBreaks(1);
		scriptString += createIndentations(2)+"var "+receivedMessageVariableName+" = "+tcpConnectionVariableName+".get_utf8_string("+tcpConnectionVariableName+".get_available_bytes())"+createLineBreaks(1);
		scriptString += createIndentations(2)+"var "+splittedStringsVariableName+" = "+receivedMessageVariableName+".split(\"\\n\")"+createLineBreaks(1);
		scriptString += createIndentations(2)+"for i in range("+splittedStringsVariableName+".size()):"+createLineBreaks(1);
		return scriptString;
	}
	
	private static String processFunctionControlUpdatesGeneration(String scriptString, MISScene scene){
		//room_begun(control etc)
		scriptString += createIndentations(1)+"if "+roomBegunVariableName+":"+createLineBreaks(1);
		for(int i = 0; i < MISProject.project.roomSettings.teams; i++){
			scriptString += createIndentations(2)+"if "+teamIdVariableName+" == "+i+":"+createLineBreaks(1);
			for(int j = 0; j < scene.nodeList.size(); j++){
				if(scene.nodeList.get(j).isControllable){
					if(scene.nodeList.get(j).controlReceiver instanceof MISReceiverTeam){
						MISReceiverTeam team = (MISReceiverTeam)scene.nodeList.get(j).controlReceiver;
						if(team.team == i){
							String nameOfNode = getVariableNameForNode(scene.nodeList.get(j));
							scriptString = controlNodeString(scriptString, scene.nodeList.get(j), nameOfNode);
						}
					} else if(scene.nodeList.get(j).controlReceiver instanceof MISReceiverNotTeam){
						MISReceiverNotTeam team = (MISReceiverNotTeam)scene.nodeList.get(j).controlReceiver;
						if(team.team != i){
							String nameOfNode = getVariableNameForNode(scene.nodeList.get(j));
							scriptString = controlNodeString(scriptString, scene.nodeList.get(j), nameOfNode);
						}
					} 
				}
			}
			scriptString += createIndentations(3)+"pass"+createLineBreaks(1);
		}
		for(int i = 0; i < scene.nodeList.size(); i++){
			if(scene.nodeList.get(i).isControllable){
				if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverPerson){
					MISReceiverPerson person = (MISReceiverPerson)scene.nodeList.get(i).controlReceiver;
					scriptString += createIndentations(2)+"if "+playerIdVariableName+" == "+person.person+":"+createLineBreaks(1);
					String nameOfNode = getVariableNameForNode(scene.nodeList.get(i));
					scriptString += createIndentations(3)+controlNodeString(scriptString, scene.nodeList.get(i), nameOfNode);
				} else if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverNotPerson){
					MISReceiverNotPerson person = (MISReceiverNotPerson)scene.nodeList.get(i).controlReceiver;
					scriptString += createIndentations(2)+"if "+playerIdVariableName+" != "+person.person+":"+createLineBreaks(1);
					String nameOfNode = getVariableNameForNode(scene.nodeList.get(i));
					scriptString += createIndentations(3)+controlNodeString(scriptString, scene.nodeList.get(i), nameOfNode);
				} else if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverAll){
					String nameOfNode = getVariableNameForNode(scene.nodeList.get(i));
					scriptString += createIndentations(2)+controlNodeString(scriptString, scene.nodeList.get(i), nameOfNode);
				}
			}
		}
		scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
		return scriptString;
	}
	
	private static String controlNodeString(String scriptString, MISNode node, String nameOfNode){
		if(node instanceof MISNode2D){
			MISNode2D node2D = (MISNode2D) node;
			scriptString += createIndentations(3)+tcpConnectionVariableName+put_utf8_string(node2D, nameOfNode)+createLineBreaks(1);
		} else if(node instanceof MISSpatial){
			MISSpatial spatial = (MISSpatial) node;
			scriptString += createIndentations(3)+tcpConnectionVariableName+put_utf8_string(spatial, nameOfNode)+createLineBreaks(1);
		} else if(node instanceof MISNodeScene){
			MISNodeScene nodeScene = (MISNodeScene) node;
			if(nodeScene.headNode != null){
				if(nodeScene.headNode instanceof MISNode2D){
					MISNode2D node2D = (MISNode2D) nodeScene.headNode;
					scriptString += createIndentations(3)+tcpConnectionVariableName+put_utf8_string(node2D, nameOfNode, nodeScene.index)+createLineBreaks(1);
				} else if(nodeScene.headNode instanceof MISSpatial){
					MISSpatial spatial = (MISSpatial) nodeScene.headNode;
					scriptString += createIndentations(3)+tcpConnectionVariableName+put_utf8_string(spatial, nameOfNode, nodeScene.index)+createLineBreaks(1);
				}
			}
		} else{
			scriptString += createIndentations(3)+"pass #Error, Not other than MISNode2D and MISSpatial implemented yet."+createLineBreaks(1);
		}
		return scriptString;
	}

	private static String processFunctionTcpControlGeneration(String scriptString){
		//Tcp connection updates
		scriptString += createIndentations(1)+"if "+tcpConnectionVariableName+".get_status() == 1:"+createLineBreaks(1);
		scriptString += createIndentations(2)+timeCounterVariableName+" = "+timeCounterVariableName+" + delta"+createLineBreaks(2);
		scriptString += createIndentations(1)+"if "+timeCounterVariableName+" > TIMEOUT_DURATION_CONNECT:"+createLineBreaks(1);
		scriptString += createIndentations(2)+tcpConnectionVariableName+".disconnect()"+createLineBreaks(1);
		scriptString += createIndentations(2)+"set_process(false)"+createLineBreaks(3);
		return scriptString;
	}
	
	private static String connectFunctionGeneration(String scriptString, String ip, MISProject project){
		//Create connect function
		scriptString += "func connectToServer():"+createLineBreaks(1);
		scriptString += createIndentations(1)+tcpConnectionVariableName+".connect(\""+ip+"\", "+project.basePort.port+")"+createLineBreaks(1);
		scriptString += createIndentations(1)+tcpConnectionVariableName+".put_utf8_string(\"client_details \"+MINIMUM_BUILD)"+createLineBreaks(1);
		scriptString += createIndentations(1)+"set_process(true)"+createLineBreaks(1);
		scriptString += createLineBreaks(2);
		return scriptString;
	}
	
	private static String customRoomBegunFunctionGeneration(String scriptString, MISScene scene){
		//Create room_begun function (usermade code, can be called here)
		scriptString += "func roomBegun(data):"+createLineBreaks(1);
		scriptString += createIndentations(1)+"#Custom code can be added here(called when the game starts)"+createLineBreaks(1);
		scriptString += createIndentations(1)+roomBegunVariableName+" = true"+createLineBreaks(1);
		for(int i = 0; i < MISProject.project.roomSettings.teams; i++){
			scriptString += createIndentations(1)+"if "+teamIdVariableName+" == "+i+":"+createLineBreaks(1);
			scriptString += createIndentations(1)+"#Custom code can be added here(called when the player #"+i+" game starts)"+createLineBreaks(1);
			scriptString += createIndentations(2)+"pass"+createLineBreaks(1);
		}
				
		scriptString += createLineBreaks(2);
		return scriptString;
	}
	
	private static String queueFunctionsGeneration(String scriptString, MISScene scene){
		//create queue functions
		scriptString += "func onReceiveQueueStart(data):"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
				
		scriptString += "func onReceiveQueueEnd(data):"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
				
		scriptString += "func sendQueueStartRequest(data):"+createLineBreaks(1);
		scriptString += createIndentations(1)+"if "+tcpConnectionVariableName+".is_connected():"+createLineBreaks(1);
		scriptString += createIndentations(2)+tcpConnectionVariableName+".put_utf8_string(\"queuestart "+scene.IDNumber+"\\n\")"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
		return scriptString;
	}
	
	private static String broadcastFunctionsGeneration(String scriptString, MISScene scene){
		//Create broadcast functions
		for(int i = 0; i < scene.broadcasts.size(); i++){
			scriptString += "func onReceiveBroadcast"+scene.broadcasts.get(i).getBroadcastName()+"(data):"+createLineBreaks(1);
			scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
		}
		return scriptString;
	}
	
	private static String refreshFunctionsGeneration(String scriptString, MISScene scene){
		//Create refresh functions
		for(int i = 0; i < scene.nodeList.size(); i++){
			if(scene.nodeList.get(i).shouldSendInformation){
				scriptString += "func onReceiveRefresh"+scene.name+"_"+i+"():"+createLineBreaks(1);
				scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
			}
		}
		return scriptString;
	}
	
	private static String nodeControlFunctionsGeneration(String scriptString, MISScene scene){
		//Create node control functions
		for(int i = 0; i < scene.nodeList.size(); i++){
			if(scene.nodeList.get(i).isControllable){
				if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverAll){
					scriptString += "func updateNodeAll_"+scene.name+"_"+scene.nodeList.get(i).index+"():"+createLineBreaks(1);
					scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
				} else if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverTeam){
					MISReceiverTeam receiver = (MISReceiverTeam)scene.nodeList.get(i).controlReceiver;
					scriptString += "func updateNodeTeam_"+receiver.team+"_"+scene.name+"_"+scene.nodeList.get(i).index+"():"+createLineBreaks(1);
					scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
				} else if(scene.nodeList.get(i).controlReceiver instanceof MISReceiverPerson){
					MISReceiverPerson receiver = (MISReceiverPerson)scene.nodeList.get(i).controlReceiver;
					scriptString += "func updateNodePerson_"+receiver.person+"_"+scene.name+"_"+scene.nodeList.get(i).index+"():"+createLineBreaks(1);
					scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
				}
			}
		}
		return scriptString;
	}
	
	private static String closeUpServerFunctionGeneration(String scriptString){
		scriptString += "func closeUpServer():"+createLineBreaks(1);
		scriptString += createIndentations(1)+"if "+tcpConnectionVariableName+".is_connected():"+createLineBreaks(1);
		scriptString += createIndentations(2)+tcpConnectionVariableName+".disconnect_from_host()"+createLineBreaks(2);
		return scriptString;
	}
	
	private static String gameActionFunctionsGeneration(String scriptString, MISScene scene) {
		//gameEnd
		scriptString += "func onReceiveGameEnd(winning_team):"+createLineBreaks(1);
		scriptString += createIndentations(1)+"gameEnd()"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
		
		scriptString += "func gameEnd():"+createLineBreaks(1);
		scriptString += createIndentations(1)+"#Custom code can be added here(called when the game ends)"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
		
		if(MISProject.project.roomSettings.gameType instanceof MISCompetetiveGameType){
			scriptString += "func onReceiveGameWon(winning_team):"+createLineBreaks(1);
			scriptString += createIndentations(1)+"if "+teamIdVariableName+" == winning_team:"+createLineBreaks(1);
			scriptString += createIndentations(2)+"gameWon()"+createLineBreaks(2);
			
			scriptString += "func gameWon():"+createLineBreaks(1);
			scriptString += createIndentations(1)+"#Custom code can be added here(called when the player won)"+createLineBreaks(1);
			scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
		}
		
		return scriptString;
	}
	
	private static String triggerMessageFunctionsGeneration(String scriptString, MISScene scene){
		for(int i = 0; i < scene.nodeList.size(); i++){
			for(int j = 0; j < scene.nodeList.get(i).trigger.size(); j++){
				MISTrigger trigger = scene.nodeList.get(i).trigger.get(j);
				scriptString += "func onReceivedTrigger_"+scene.nodeList.get(i).name+"_"+scene.nodeList.get(i).index+"_"+j+"():"+createLineBreaks(1);
				scriptString += createIndentations(1)+"#Custom code can be added here(called when a trigger is called)"+createLineBreaks(1);
				scriptString += createIndentations(1)+"pass"+createLineBreaks(2);
			}
		}
		return scriptString;
	}
	
	private static String put_utf8_string(MISNode2D node, String name){
		return ".put_utf8_string(\"[node] "+node.name+" "+node.index+" [transform2d] \"+str("+name+".get_pos().x)+\" \"+str("+name+".get_pos().y)+\" \"+str("+name+".get_rot())+\" \"+str("+name+".get_scale().x)+\" \"+str("+name+".get_scale().y)+\"\\n\")";
	}
	
	private static String put_utf8_string(MISNode2D node, String name, int index){
		return ".put_utf8_string(\"[node] "+node.name+" "+index+" [transform2d] \"+str("+name+".get_pos().x)+\" \"+str("+name+".get_pos().y)+\" \"+str("+name+".get_rot())+\" \"+str("+name+".get_scale().x)+\" \"+str("+name+".get_scale().y)+\"\\n\")";
	}
	
	private static String put_utf8_string(MISSpatial spatial, String name) {
		return ".put_utf8_string(\"[node] "+spatial.name+" "+spatial.index+" [spatial] \"+str("
				+ name+".get_transform().basis.x.x)+\" \"+str("+name+".get_transform().basis.x.y)+\" \"+str("+name+".get_transform().basis.x.z)+\" \"+str("
				+ name+".get_transform().basis.y.x)+\" \"+str("+name+".get_transform().basis.y.y)+\" \"+str("+name+".get_transform().basis.y.z)+\" \"+str("
				+ name+".get_transform().basis.z.x)+\" \"+str("+name+".get_transform().basis.z.y)+\" \"+str("+name+".get_transform().basis.z.z)+\" \"+str("
				+ name+".get_transform().origin.x)+\" \"+str("+name+".get_transform().origin.y)+\" \"+str("+name+".get_transform().origin.z)+\"\\n\")";
	}
	
	private static String put_utf8_string(MISSpatial spatial, String name, int index) {
		return ".put_utf8_string(\"[node] "+spatial.name+" "+index+" [spatial] \"+str("
				+ name+".get_transform().basis.x.x)+\" \"+str("+name+".get_transform().basis.x.y)+\" \"+str("+name+".get_transform().basis.x.z)+\" \"+str("
				+ name+".get_transform().basis.y.x)+\" \"+str("+name+".get_transform().basis.y.y)+\" \"+str("+name+".get_transform().basis.y.z)+\" \"+str("
				+ name+".get_transform().basis.z.x)+\" \"+str("+name+".get_transform().basis.z.y)+\" \"+str("+name+".get_transform().basis.z.z)+\" \"+str("
				+ name+".get_transform().origin.x)+\" \"+str("+name+".get_transform().origin.y)+\" \"+str("+name+".get_transform().origin.z)+\"\\n\")";
	}
	
}
