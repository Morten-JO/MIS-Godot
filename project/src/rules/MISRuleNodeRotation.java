package rules;

import data_types.MISBounds;
import data_types.MISNode;
import rules.MISRuleNode.options;

public class MISRuleNodeRotation extends MISRuleNode {

	public MISBounds rotationBounds;
	
	public MISRuleNodeRotation(String name, MISNode node, options option) {
		super(name, node, option);
	}
	
	@Override
	public String getUserInput(){
		return "(min,max):("+rotationBounds.min+","+rotationBounds.max+")";
	}

}
