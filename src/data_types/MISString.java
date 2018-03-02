package data_types;

public class MISString extends MISVariable{

	protected String value;
	
	public MISString(String key, String value) {
		super(key);
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}

}
