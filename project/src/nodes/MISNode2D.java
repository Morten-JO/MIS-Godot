package nodes;

import data_types.MIS2DTransform;
import rules.MISRuleNodeRotation;

public class MISNode2D extends MISNode{

	public MIS2DTransform transform;
	
	public MISNode2D(MIS2DTransform transform){
		this.transform = transform;
	}
	
	@Override
	public String getReadyPacket(){
		return "node "+name+" "+index+" transform2d "+transform.positionX+" "+transform.positionY+" "+transform.rotation+" "+transform.scaleX+" "+transform.scaleY;
	}
	
}
