package build;

import java.io.IOException;

import project.MISProject;

public class BuilderClass {

	//THIS CLASS IS CURRENTLY NOT WORKING.
	
	public static void main(String[] args) {
		try {
			generateServerJar("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean generateServerJar(String directory) throws IOException{
		if(MISProject.project == null){
			return false;
		}
		return false;
	}
	
}
