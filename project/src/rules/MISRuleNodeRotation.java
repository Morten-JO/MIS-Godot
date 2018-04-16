package rules;

import data_types.MISBounds;
import data_types.MISNode;

public class MISRuleNodeRotation extends MISRuleNode {

	public MISBounds rotationBounds;
	
	public MISRuleNodeRotation(String name, MISNode node) {
		super(name, node);
	}

}
