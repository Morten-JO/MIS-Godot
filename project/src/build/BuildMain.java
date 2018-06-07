package build;

import java.io.File;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import project.MISProject;
import server.Server;

public class BuildMain {

	//The runner
	
	public static void main(String[] args) {
		try {
			String fileStartedFrom = new File(BuildMain.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
			
			File directory = new File(fileStartedFrom);
			if(directory.isDirectory()){
				File[] files = directory.listFiles();
				File projectFile = null;
				for(int i = 0; i < files.length; i++){
					if(files[i].getName().equals("project.json")){
						projectFile = files[i];
						break;
					}
				}
				if(projectFile != null){
					try{
						boolean success = MISProject.loadProject(projectFile.getParent());
						if(success){
							new Server(MISProject.project.basePort.port, MISProject.project.uiOnRun, true);
						} 
					} catch(Exception e){
						e.printStackTrace();
					}
					
				} 
			} else{
				JOptionPane.showMessageDialog(null, "not directory");
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
	}
	
}
