package loaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import data_types.MISNode;
import data_types.MISScene;

public class MISLoader {

	public static void main(String[] args) {
		MISLoader.loadSceneByLocation("resources/testresources/Sce.tscn");
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
			MISScene scene = new MISScene(1234);
			String[] temp = location.split("/");
			scene.name = temp[temp.length-1].split(".tscn")[0];
			while((readLine = reader.readLine()) != null){
				if(readLine.startsWith("[ext_resource")){
					//add external resource
				} else if(readLine.startsWith("[node")){
					MISNode node = new MISNode();
					if(readLine.contains("name=\"")){
						node.name = readLine.split("name=\"")[1].split("\"")[0];
					}
					if(readLine.contains("type=\"")){
						node.type = readLine.split("type=\"")[1].split("\"")[0];
					}
					if(readLine.contains("parent=\"")){
						String value = readLine.split("parent=\"")[1].split("\"")[0];
						if(!value.equals(".")){
							if(value.contains("/")){
								String[] parents = value.split("/");
								value = parents[parents.length-1];
							}
							for(int i = 0; i < scene.nodeList.size(); i++){
								if(scene.nodeList.get(i).name.equals(value)){
									node.parent = scene.nodeList.get(i);
									break;
								}
							}
						}
					}
					if(readLine.contains("index=\"")){
						node.index = Integer.parseInt(readLine.split("index=\"")[1].split("\"")[0]);
					}
					scene.addNode(node);
				}
			}
			reader.close();
			System.out.println("SCENE DATA");
			System.out.println("Scene name: "+scene.name);
			for(int i = 0; i < scene.nodeList.size(); i++){
				System.out.println();
				System.out.println("For node #"+i);
				System.out.println("Name: "+scene.nodeList.get(i).name);
				System.out.println("Type: "+scene.nodeList.get(i).type);
				if(scene.nodeList.get(i).parent != null){
					System.out.println("Parent: "+scene.nodeList.get(i).parent.name);
				}
				System.out.println("index: "+scene.nodeList.get(i).index);
			}
			return scene;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
