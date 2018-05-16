package loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.sun.xml.internal.bind.v2.TODO;

import data_types.MIS2DTransform;
import data_types.MISExternalResource;
import data_types.MISScene;
import nodes.MISControl;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISSpatial;
import project.MISProject;
import project.MISProjectInformation;

public class MISLoader {

	public static void main(String[] args) {
		MISLoader.loadSceneByLocation("resources/testresources/Sce.tscn");
	}
	
	public static ArrayList<MISScene> loadScenesByLocation(String location){
		ArrayList<MISScene> scenes = new ArrayList<MISScene>();
		File file = new File(location);
		if(!file.isDirectory()){
			System.err.println("Location isnt a directory in MISLoader");
			return scenes;
		} else{
			System.out.println("Recursive search start!");
			recursiveSearchForScenes(file, scenes);
		}
		return scenes;
	}
	
	private static void recursiveSearchForScenes(File file, List<MISScene> scenes){
		System.out.println("Recur on file: "+file.getName());
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i = 0; i < files.length; i++){
				recursiveSearchForScenes(files[i], scenes);
			}
		} else{
			if(file.getName().contains(".tscn")){
				scenes.add(loadSceneByLocation(file.getAbsolutePath()));
			}
		}
	}
	
	/**
	 * Reads and returns the loaded scene based on location(file included)
	 * @param location
	 * @return MISScene or null
	 */
	public static MISScene loadSceneByLocation(String location){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(location));
			String readLine = "";
			MISScene scene = new MISScene();
			if(location.contains("/")){
				String[] temp = location.split("/");
				scene.name = temp[temp.length-1].split(".tscn")[0];
			} else if(location.contains("\\")){
				String[] temp = location.split(Pattern.quote("\\"));
				scene.name = temp[temp.length-1].split(".tscn")[0];
			} else {
				scene.name = location;
			}
			
			MISNode lastNode = null;
			while((readLine = reader.readLine()) != null){
				if(readLine.startsWith("[ext_resource")){
					MISExternalResource externalResource = new MISExternalResource();
					if(readLine.contains("path=\"")){
						externalResource.path = readLine.split("path=\"")[1].split("\"")[0];
						String[] values = externalResource.path.split("/");
						externalResource.name = values[values.length-1];		
					}
					if(readLine.contains("type=\"")){
						externalResource.type = readLine.split("type=\"")[1].split("\"")[0];
					}
					if(readLine.contains("id=")){
						externalResource.id = Integer.parseInt(readLine.split("id=")[1].split("]")[0]);
					}
					scene.addExternalResource(externalResource);
				} else if(readLine.startsWith("[node")){
					MISNode node = new MISNode();
					if(readLine.contains("name=\"")){
						node.name = readLine.split("name=\"")[1].split("\"")[0];
						System.out.println("Node from readline: "+readLine);
						System.out.println("Node name is: "+node.name);
						System.out.println("Node name set.");
						
					}
					if(readLine.contains("type=\"")){
						node.type = readLine.split("type=\"")[1].split("\"")[0];
						if(MISNode2D.isType(node.type)){
							MISNode2D node2D = new MISNode2D(new MIS2DTransform(0, 0, 0, 0, 0));
							node2D.name = node.name;
							node2D.type = node.type;
							node = node2D;
						} else if(MISSpatial.isType(node.type)) {
							MISSpatial spatial = new MISSpatial();
							spatial.name = node.name;
							spatial.type = node.type;
							node = spatial;
						} else if(MISControl.isType(node.type)){
							MISControl control = new MISControl();
							control.name = node.name;
							control.type = node.type;
							node = control;
						}
					}
					if(readLine.contains("parent=\"")){
						String value = readLine.split("parent=\"")[1].split("\"")[0];
						if(!value.equals(".")){
							if(value.contains("/")){
								String[] parents = value.split("/");
								value = parents[parents.length-1];
							}
							for(int i = scene.nodeList.size()-1; i >= 0; i--){
								if(scene.nodeList.get(i).name.equals(value)){
									node.parent = scene.nodeList.get(i);
									break;
								}
							}
						} else{
							System.out.println("Just set parent parent parent.");
							node.parent = scene.nodeList.get(0);
						}
					}
					lastNode = node;
					scene.addNode(node);
					for(int i = 0; i < scene.nodeList.size(); i++){
						System.out.println("#"+i+" : "+scene.nodeList.get(i).name);
					}
				} else if(readLine.startsWith("script = ExtResource")){
					if(lastNode != null){
						readLine = readLine.replaceAll(" ", "");
						String stringOfIndex = readLine.split("\\(")[1].split("\\)")[0];
						lastNode.scriptAttached = true;
						int index = Integer.parseInt(stringOfIndex);
						for(MISExternalResource res : scene.externalResources){
							if(index == res.id){
								lastNode.scriptId = index;
								lastNode.scriptName = res.name;
								break;
							}
						}
					}
				} else if(readLine.startsWith("[gd_scene")){
					String loadStepsString = readLine.split("load_steps=")[1].split(" ")[0];
					scene.loadSteps = Integer.parseInt(loadStepsString);
					String formatString = readLine.split("format=")[1].split("]")[0];
					scene.format = Integer.parseInt(formatString);
				} else if(readLine.startsWith("transform")){
					if(readLine.startsWith("transform/pos")){
						try{
							if(lastNode instanceof MISNode2D){
								String parseString = readLine.split(Pattern.quote("("))[1].replaceAll(Pattern.quote(")"), "").replaceAll(" ", "");
								String xPosition = parseString.split(",")[0];
								String yPosition = parseString.split(",")[1];
								MISNode2D node2D = ((MISNode2D)lastNode);
								node2D.transform.positionX = Double.parseDouble(xPosition);
								node2D.transform.positionY = Double.parseDouble(yPosition);
							} 
						} catch(Exception e){
							e.printStackTrace();
						}
						
					} else if(readLine.startsWith("transform/scale")){
						try{
							
							if(lastNode instanceof MISNode2D){
								String parseString = readLine.split(Pattern.quote("("))[1].replaceAll(Pattern.quote(")"), "").replaceAll(" ", "");
								String xSize = parseString.split(",")[0];
								String ySize = parseString.split(",")[1];
								MISNode2D node2D = ((MISNode2D)lastNode);
								node2D.transform.scaleX = Double.parseDouble(xSize);
								node2D.transform.scaleY = Double.parseDouble(ySize);
							} 
						} catch(Exception e){
							e.printStackTrace();
						}
					} else if(readLine.startsWith("transform/scale")){
						try{
							if(lastNode instanceof MISSpatial){
								String parseString = readLine.split(Pattern.quote("("))[1].replaceAll(Pattern.quote(""), "").replaceAll(" ", "");
								String[] values = parseString.split(",");
								
								if(values.length >= 12){
									((MISSpatial)lastNode).xx = Double.parseDouble(values[0]);
									((MISSpatial)lastNode).xy = Double.parseDouble(values[1]);
									((MISSpatial)lastNode).xz = Double.parseDouble(values[2]);
									
									((MISSpatial)lastNode).yx = Double.parseDouble(values[3]);
									((MISSpatial)lastNode).yy = Double.parseDouble(values[4]);
									((MISSpatial)lastNode).yz = Double.parseDouble(values[5]);
									
									((MISSpatial)lastNode).zx = Double.parseDouble(values[6]);
									((MISSpatial)lastNode).zy = Double.parseDouble(values[7]);
									((MISSpatial)lastNode).zz = Double.parseDouble(values[8]);
									
									((MISSpatial)lastNode).xo = Double.parseDouble(values[9]);
									((MISSpatial)lastNode).yo = Double.parseDouble(values[10]);
									((MISSpatial)lastNode).zo = Double.parseDouble(values[11]);
								}
							}
						} catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			reader.close();
			return scene;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean saveProjectLocation(String[] locations, String[] date){
		FileWriter file;
		try {
			file = new FileWriter("resources/testresources/projectlocations.txt");
			for(int i = 0; i < locations.length; i++){
				file.write(locations[i]+" "+date[i]+System.lineSeparator());
			}
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean saveProjectLocation(ArrayList<MISProjectInformation> infos){
		String[] locations = new String[infos.size()];
		String[] dates = new String[infos.size()];
		for(int i = 0; i < infos.size(); i++){
			locations[i] = infos.get(i).getProjectLocation();
			dates[i] = infos.get(i).getProjectDateCreation();
		}
		return saveProjectLocation(locations, dates);
	}
	
	public static MISProjectInformation getInformationFromString(String str){
		MISProjectInformation information = null;
		String[] data = str.split(" ");
		String projectPath = data[0];
		System.out.println("project path: "+projectPath);
		String projectName = "";
		if(projectPath.contains("\\")){
			String[] pathFolders = projectPath.split(Pattern.quote("\\"));
			
			if(pathFolders.length > 0){
				projectName = pathFolders[pathFolders.length-1].replaceAll(Pattern.quote("\\"), "");
			} else{
				projectName = projectPath;
			}
		} else if(projectPath.contains("/")){
			String[] pathFolders = projectPath.split("/");
			
			if(pathFolders.length > 0){
				projectName = pathFolders[pathFolders.length-1].replaceAll("/", "");
			} else{
				projectName = projectPath;
			}
		} else{
			projectName = projectPath;
		}
		String projectDate = data[1];
		boolean found = false;
		File file = new File(projectPath+"/project.json");
		System.out.println("The path which was tried to load:"+file.getAbsolutePath());
		if(file.exists() && !file.isDirectory()){
			found = true;
			System.out.println("Found the file");
		} else{
			System.out.println("Didn't find the file.");
		}
		information = new MISProjectInformation(projectName, projectPath, projectDate, found);
		return information;
	}
	
	public static ArrayList<MISProjectInformation> loadProjectLocations(){
		ArrayList<MISProjectInformation> projectLocationsObjects = new ArrayList<MISProjectInformation>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("resources/testresources/projectlocations.txt"));
			String readLine = "";
			while((readLine = reader.readLine()) != null){
				MISProjectInformation info = MISLoader.getInformationFromString(readLine);
				projectLocationsObjects.add(info);
			}
			reader.close();
			return projectLocationsObjects;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectLocationsObjects;
		
	}
	
}
