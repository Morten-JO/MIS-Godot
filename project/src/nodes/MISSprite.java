package nodes;

import data_types.MIS2DTransform;

public class MISSprite extends MISNode{

	public MIS2DTransform transform;
	public int textureId;
	
	public MISSprite(MIS2DTransform transform, int textureId){
		this.transform = transform;
		this.textureId = textureId;
	}
	
	
	@Override
	public String getReadyPacket(){
		return "node "+name+" "+index+" transform2d "+transform.positionX+" "+transform.positionY+" "+transform.rotation+" "+transform.scaleX+" "+transform.scaleY+" texture "+textureId;
	}
	
}
