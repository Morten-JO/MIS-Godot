package rules;

import data_types.MISNode;

public class MISRuleNode extends MISRule {
	
	enum options {
		Bounds,
		Control
	}
	
	public options option;
	
	public MISNode node;
	
	public MISRuleNode(String name, MISNode node) {
		super(name);
		this.node = node;
	}

}
