package triggers;

import actions.MISAction;

public class MISTrigger {

	public boolean hasTriggered = false;
	
	public MISTrigger(MISAction action) {
		this.action = action;
	}

	public MISAction action;
	
}
