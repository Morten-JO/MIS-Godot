package rules;

import data_types.MISNode;
import rules.MISRuleNode.options;

public class MISRuleNodeControl extends MISRuleNode {

	
	
	public MISRuleNodeControl(String name, MISNode node, options option) {
		super(name, node, option);
	}

	@Override
	public String getUserInput(){
		return "Control to be made";
	}
	
}
