package scene;

import data_types.MISNode;
import enums.MISRuleType;

public class MISRule {

	public MISRuleType typeRule;
	
	public String ruleName;
	
	public MISNode node;
	//what kind of rules?
	//if pos of node == 500, win game
	//
	
	public MISRule(MISRuleType type, String name, MISNode node){
		this.typeRule = type;
		this.ruleName = name;
		if(type == MISRuleType.Node_Property || type == MISRuleType.Node_Structure){
			this.node = node;
		}
	}
	
}
