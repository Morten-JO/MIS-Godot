package data_types;

public class MISLong extends MISVariable{

	protected Long value;
	
	public MISLong(String key, Long value) {
		super(key);
		this.value = value;
	}
	
	public Long getValue(){
		return value;
	}

}
