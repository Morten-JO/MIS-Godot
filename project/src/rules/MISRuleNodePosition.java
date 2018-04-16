package rules;

import data_types.MISBounds;
import data_types.MISNode;

public class MISRuleNodePosition extends MISRuleNode {

	public MISBounds xBounds;
	public MISBounds yBounds;
	public MISBounds zBounds;
	
	public MISRuleNodePosition(String name, MISNode node) {
		super(name, node);
	}

}
