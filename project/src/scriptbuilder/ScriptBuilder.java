package scriptbuilder;

import project.MISProject;

public class ScriptBuilder {

	private static String tcpConnectionVariableName = "tcp_connection";
	private static String tcpConnectionVariableIntialize = "StreamPeerTCP.new()";
	
	private static String baseServerIpVariableName = "base_server_ip";
	
	private static String timeoutDurationConst = "TIMEOUT_DURATION_CONNECT = 10";
	
	public static void main(String[] args) {
	}
	
	public static  boolean buildScript(MISProject project, String location, String name, String nodeType, String ip){
		
		String scriptString = "";
		
		//Create pre variable things
		scriptString = "extends "+nodeType+createLineBreaks(2);
		scriptString += "#generated by MIS-Godot"+createLineBreaks(1);
		scriptString += "#minimum build version "+project.minimumBuildVersion+createLineBreaks(1);
		scriptString += "#project name: "+project.projectName+createLineBreaks(2);
		
		//Create variables and variablenames
		scriptString += "var "+tcpConnectionVariableName+" = "+tcpConnectionVariableIntialize+createLineBreaks(1);
		scriptString += "var "+baseServerIpVariableName+" = "+"\""+ip+"\"";
		
		scriptString += createLineBreaks(2);
		
		//Create consts
		scriptString += "const "+timeoutDurationConst+createLineBreaks(1);
		
		scriptString += createLineBreaks(2);
		
		//Create ready func
		scriptString += "func _ready():"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass";
		
		scriptString += createLineBreaks(2);
		
		//Create proces function
		scriptString += "func _process(delta):"+createLineBreaks(1);
		scriptString += createIndentations(1)+"pass";
		
		scriptString += createLineBreaks(2);
				
		
		//Create connect function
		scriptString += "func connectToServer():"+createLineBreaks(1);
		scriptString += createIndentations(1)+tcpConnectionVariableName+".connect("+ip+", "+project.basePort.port+")";
		
		
		System.out.println("Script:");
		System.out.println(scriptString);
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
	
}
