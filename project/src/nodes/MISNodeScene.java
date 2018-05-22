package nodes;

import java.util.List;
import java.util.regex.Pattern;

import data_types.MISExternalResource;
import data_types.MISScene;
import data_types.MISTempScene;
import project.MISProject;

public class MISNodeScene extends MISNode{

	public MISExternalResource resource;
	public MISScene scene;
	
	/**
	 * Some questionable function
	 * @param scenes
	 * @return
	 */
	public boolean recorrectScene(List<MISScene> scenes) throws NullPointerException{
		if(scene == null){
			throw new NullPointerException();
		}
		for(int i = 0; i < scenes.size(); i++){
			String fixedPath = scenes.get(i).path.replaceAll(Pattern.quote("\\"), "/");
			if(scene.name.equals(scenes.get(i).name) && resource.path.equals(fixedPath) ){
				scene = scenes.get(i);
				return true;
			}
		}
		return false;
	}
	
}
