package data_types;

public class MISPosition {

	public int x;
	public int y;
	
	public MISPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}
	
}
