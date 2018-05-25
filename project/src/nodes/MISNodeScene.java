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
	public MISNode headNode;
	
	/**
	 * Some questionable function
	 * @param scenes
	 * @return
	 */
	public boolean recorrectScene(List<MISScene> scenes) throws NullPointerException{
		if(scene == null){
			throw new NullPointerException();
		}
		boolean toReturn = false;
		for(int i = 0; i < scenes.size(); i++){
			String fixedPath = scenes.get(i).path.replaceAll(Pattern.quote("\\"), "/");
			if(scene.name.equals(scenes.get(i).name) && resource.path.equals(fixedPath) ){
				scene = scenes.get(i);
				toReturn = true;
				break;
			}
		}
		if(toReturn){
			headNode = scene.nodeList.get(0);
		}
		return toReturn;
	}
	
	@Override
	public String getReadyPacket(){
		String toSend = "[node] "+name+" "+index+" ";
		if(headNode != null){
			if(headNode instanceof MISNode2D){
				MISNode2D node2D = (MISNode2D) headNode;
				toSend += "[transform2d] "+node2D.transform.positionX+" "+node2D.transform.positionY+" "+node2D.transform.rotation+" "+node2D.transform.scaleX+" "+node2D.transform.scaleY;
			} else if(headNode instanceof MISSpatial){
				MISSpatial spatial = (MISSpatial) headNode;
				toSend += "[spatial] "+spatial.xx+" "+spatial.xy+" "+spatial.xz+" "+spatial.yx+" "+spatial.yy+" "+spatial.yz+" "+spatial.zx+" "+spatial.zy+" "+spatial.zz+" "+spatial.xo+" "+spatial.yo+" "+spatial.zo;
			} else if(headNode instanceof MISControl){
				MISControl control = (MISControl) headNode;
			} 
		}
		return toSend;
	}
	
}
