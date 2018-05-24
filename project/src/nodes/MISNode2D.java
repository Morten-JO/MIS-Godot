package nodes;

import data_types.MIS2DTransform;
import rules.MISRuleNodeRotation;

public class MISNode2D extends MISNode{

	private static String[] collectiveMatches = {"Node2D", "CanvasModulate", "Particles2D", "ParallaxLayer", "SamplePlayer2D", "RemoteTransform2D", "TouchScreenButton", "Position2D",
												 "Area2D", "Sprite", "ParticleAttractor2D", "AnimatedSprite", "ViewportSprite", "VisibilityNotifier2D", "VisibilityEnabler2D", "Polygon2D",
												 "RayCast2D", "CollisionShape2D", "CollisionPolygon2D", "Light2D", "LightOccluder2D", "YSort", "BackBufferCopy", "PinJoint2D", "GrooveJoint2D",
												 "DampedSpringJoint2D", "Camera2D", "TileMap", "Navigation2D", "Path2D", "PathFollow2D", "NavigationPolygonInstance", "KinematicBody2D"};
	
	public MIS2DTransform transform;
	
	public MISNode2D(MIS2DTransform transform){
		this.transform = transform;
	}
	
	@Override
	public String getReadyPacket(){
		return "[node] "+name+" "+index+" [transform2d] "+transform.positionX+" "+transform.positionY+" "+transform.rotation+" "+transform.scaleX+" "+transform.scaleY;
	}
	
	public static boolean isType(String type){
		for(int i = 0; i < collectiveMatches.length; i++){
			if(type.equals(collectiveMatches[i])){
				return true;
			}
		}
		return false;
	}
	
}
