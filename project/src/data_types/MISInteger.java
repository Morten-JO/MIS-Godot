package data_types;

public class MISInteger extends MISVariable{

	protected int value;
	
	public MISInteger(String key, int value) {
		super(key);
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}

}
