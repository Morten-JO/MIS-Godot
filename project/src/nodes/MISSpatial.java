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
	
	public double xx;
	public double yx;
	public double zx;
	
	public double xy;
	public double yy;
	public double zy;
	
	public double xz;
	public double yz;
	public double zz;
	
	public double xo;
	public double yo;
	public double zo;
	
	public MISSpatial() {
		super();
	}
	
	@Override
	public String getReadyPacket(){
		return "[node] "+name+" "+index+" [spatial] "+xx+" "+xy+" "+xz+" "+yx+" "+yy+" "+yz+" "+zx+" "+zy+" "+zz+" "+xo+" "+yo+" "+zo;
	}

}
