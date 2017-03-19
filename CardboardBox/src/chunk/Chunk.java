package chunk;

public class Chunk {
	public static final int SIDE_LENGTH = 32;
	private int[] colors = new int[SIDE_LENGTH*SIDE_LENGTH*SIDE_LENGTH];
	
	private int getColor(int x, int y, int z) {
		return colors[x*SIDE_LENGTH*SIDE_LENGTH+y*SIDE_LENGTH+z];
	}
	
	private boolean getExists(int x, int y, int z) {
		return colors[x*SIDE_LENGTH*SIDE_LENGTH+y*SIDE_LENGTH+z] != 0;
	}
	
	void setColor(int x, int y, int z, int color) {
		if (color == 0) {
			color = 0x00010101;
		}
		
		colors[x*SIDE_LENGTH*SIDE_LENGTH+y*SIDE_LENGTH+z] = color;
	}
	
	void remove(int x, int y, int z) {
		colors[x*SIDE_LENGTH*SIDE_LENGTH+y*SIDE_LENGTH+z] = 0;
	}
	
	public void load() {
	}
	
	public void unload() {
	}
	
	public void draw() {
	}
}
