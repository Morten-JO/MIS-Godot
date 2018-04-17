package rules;

import data_types.MISNode;

public class MISRuleNode extends MISRule {
	
	public enum options {
		Bounds,
		Control
	}
	
	public options option;
	
	public MISNode node;
	
	public MISRuleNode(String name, MISNode node, options option) {
		super(name);
		this.node = node;
		this.option = option;
	}
	
	public String getUserInput(){
		return "default";
	}

}
