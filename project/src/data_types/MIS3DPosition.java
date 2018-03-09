package data_types;

public class MIS3DPosition extends MISPosition{

	public int z;
	
	public MIS3DPosition(int x, int y, int z) {
		super(x, y);
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+","+z+"]";
	}
}
