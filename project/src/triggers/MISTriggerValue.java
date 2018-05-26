package triggers;

import actions.MISAction;
import nodes.MISNode;
import nodes.MISNode2D;
import nodes.MISSpatial;

public class MISTriggerValue extends MISTrigger{

	public double valueTarget;
	
	public enum ValueComparer{
		Lower,Higher,Equal
	}
	
	public enum TargetType{
		x,y,rot,scaleX,scaleY,
		xx,xy,xz,yx,yy,yz,zx,zy,zz,xo,yo,zo
	}
	
	public TargetType targetType;
	
	public ValueComparer comparer;
	
	public MISTriggerValue(MISAction action, double valueTarget, ValueComparer comparer){
		super(action);
		this.valueTarget = valueTarget;
		this.comparer = comparer;
	}
	
	public boolean targetHit(MISNode node){
		double value;
		if(node instanceof MISNode2D){
			MISNode2D node2D = (MISNode2D) node;
			if(targetType == TargetType.x){
				value = node2D.transform.positionX;
			} else if(targetType == TargetType.y){
				value = node2D.transform.positionY;
			} else if(targetType == TargetType.rot){
				value = node2D.transform.rotation;
			} else if(targetType == TargetType.scaleX){
				value = node2D.transform.scaleX;
			} else if(targetType == TargetType.scaleY){
				value = node2D.transform.scaleY;
			} else {
				return false;
			}
		} else if(node instanceof MISSpatial){
			MISSpatial spatial = (MISSpatial) node;
			if(targetType == TargetType.xx){
				value = spatial.xx;
			} else if(targetType == TargetType.xy){
				value = spatial.xy;
			} else if(targetType == TargetType.xz){
				value = spatial.xz;
			} else if(targetType == TargetType.yx){
				value = spatial.yx;
			} else if(targetType == TargetType.yy){
				value = spatial.yy;
			} else if(targetType == TargetType.yz){
				value = spatial.yz;
			} else if(targetType == TargetType.zx){
				value = spatial.zx;
			} else if(targetType == TargetType.zy){
				value = spatial.zy;
			} else if(targetType == TargetType.zz){
				value = spatial.zz;
			} else if(targetType == TargetType.xo){
				value = spatial.xo;
			} else if(targetType == TargetType.yo){
				value = spatial.yo;
			} else if(targetType == TargetType.zo){
				value = spatial.zo;
			} else{
				return false;
			}
		} else{
			return false;
		}
		return compareValues(value);
	}
		
	
	
	private boolean compareValues(double value){
		if(comparer == ValueComparer.Lower){
			if(valueTarget > value){
				return true;
			}
		} else if(comparer == ValueComparer.Higher){
			if(valueTarget < value){
				return true;
			}
		} else{
			//Maybe it should be made so a swingoff can be inputted, pretty hard to hit the exact same double
			if(valueTarget == value){
				return true;
			}
		}
		return false;
	}
	
}
