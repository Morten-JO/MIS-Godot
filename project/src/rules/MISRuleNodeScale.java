package rules;

import data_types.MISBounds;
import data_types.MISNode;

public class MISRuleNodeScale extends MISRuleNode {

	public MISBounds xBounds;
	public MISBounds yBounds;
	
	public MISRuleNodeScale(String name, MISNode node) {
		super(name, node);
	}

}
