package data_types;

public abstract class MISVariable {

	protected String key;
	
	public MISVariable(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	
	public boolean eq(MISVariable comparer){
		if(key.equals(comparer.getKey())){
			return true;
		}
		return false;
	}
	
	public boolean keyToCompare(String key){
		if(this.key.equals(key)){
			return true;
		}
		return false;
	}
	
}
