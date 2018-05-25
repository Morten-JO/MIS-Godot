package triggers;

import actions.MISAction;

public class MISTriggerValue extends MISTrigger{

	public double valueTarget;
	
	public enum ValueComparer{
		Lower,Higher,Equal
	}
	
	public ValueComparer comparer;
	
	public MISTriggerValue(MISAction action, double valueTarget, ValueComparer comparer){
		super(action);
		this.valueTarget = valueTarget;
		this.comparer = comparer;
	}
	
	public boolean targetHit(double value){
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
