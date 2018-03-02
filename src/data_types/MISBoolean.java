package data_types;

public class MISBoolean extends MISVariable{

	protected boolean value;
	
	public MISBoolean(String key, boolean value) {
		super(key);
		this.value = value;
	}
	
	public boolean getValue(){
		return value;
	}

}
