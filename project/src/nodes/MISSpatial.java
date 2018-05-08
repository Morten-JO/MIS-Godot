package nodes;

public class MISSpatial extends MISNode{

	public static String[] collectiveMatches = {"Spatial", "Camera", "InterpolatedCamera", "Skeleton", "BoneAttachment", "Listener", "TestCube", "ImmediateGeometry", "MeshInstance",
												"Sprite3D", "AnimatedSprite3D", "Particles", "Quad", "MultiMeshInstance", "DirectionalLight", "OmniLight", "SpotLight", "Portal",
												"Room", "BakedLightInstance", "BakedLightSampler", "Area", "StaticBody", "RigidBody", "KinematicBody", "VehicleBody", "VehicleWheel",
												"NavigationMeshInstance", "Navigation", "Position3D", "Generic6DOFJoint", "ConeTwistJoint", "HingeJoint", "Pinjoint", "SliderJoint",
												"SpatialStreamPlayer", "SpatialSamplePlayer", "CollisionPolygon", "ProximityGroup", "RayCast", "CollisionShape", "VisibilityNotifier",
												"VisibilityEnabler", "Path", "PathFollow", "WorldEnvironment", "GridMap", "InverseKinematics"};
	

	public static boolean isType(String type){
		for(int i = 0; i < collectiveMatches.length; i++){
			if(type.equals(collectiveMatches[i])){
				return true;
			}
		}
		return false;
	}

}
