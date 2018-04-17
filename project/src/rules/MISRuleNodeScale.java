package rules;

import data_types.MISBounds;
import data_types.MISNode;
import rules.MISRuleNode.options;

public class MISRuleNodeScale extends MISRuleNode {

	public MISBounds xBounds;
	public MISBounds yBounds;
	
	public MISRuleNodeScale(String name, MISNode node, options option) {
		super(name, node, option);
	}
	
	@Override
	public String getUserInput(){
		return "(min,max):x("+xBounds.min+","+xBounds.max+") - y("+yBounds.min+","+yBounds.max+")";
	}

}
