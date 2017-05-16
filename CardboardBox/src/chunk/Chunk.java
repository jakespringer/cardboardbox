package chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joml.Vector3i;

public class Chunk {
	public static final int SIDE_LENGTH = 64;
	public static final int SIDE_LENGTH_PADDED_2 = SIDE_LENGTH + 2;
	private static final Vector3i VEC_XY_FRONT = new Vector3i(0, 0, 1);
	private static final Vector3i VEC_XY_BACK = new Vector3i(0, 0, -1);
	private static final Vector3i VEC_XZ_FRONT = new Vector3i(0, -1, 0);
	private static final Vector3i VEC_XZ_BACK = new Vector3i(0, 1, 0);
	private static final Vector3i VEC_YZ_FRONT = new Vector3i(-1, 0, 0);
	private static final Vector3i VEC_YZ_BACK = new Vector3i(1, 0, 0);
	
	private int[] colors = new int[SIDE_LENGTH_PADDED_2 * SIDE_LENGTH_PADDED_2 * SIDE_LENGTH_PADDED_2];
	private List<Vector3i> xyFrontQuadVertices = new ArrayList<>();
	private List<Vector3i> xzFrontQuadVertices = new ArrayList<>();
	private List<Vector3i> yzFrontQuadVertices = new ArrayList<>();
	private List<Vector3i> xyBackQuadVertices = new ArrayList<>();
	private List<Vector3i> xzBackQuadVertices = new ArrayList<>();
	private List<Vector3i> yzBackQuadVertices = new ArrayList<>();
	private List<Integer> xyFrontQuadColors = new ArrayList<>();
	private List<Integer> xzFrontQuadColors = new ArrayList<>();
	private List<Integer> yzFrontQuadColors = new ArrayList<>();
	private List<Integer> xyBackQuadColors = new ArrayList<>();
	private List<Integer> xzBackQuadColors = new ArrayList<>();
	private List<Integer> yzBackQuadColors = new ArrayList<>();
	
	public Chunk(int[] colors) {
		if (colors.length != this.colors.length) {
			throw new IllegalArgumentException("Must pass a color array of the right size");
		}
		
		System.arraycopy(colors, 0, this.colors, 0, colors.length);
		computeVertices();
	}
	
    public int getColor(int x, int y, int z) {
        return colors[(x + 1) * SIDE_LENGTH_PADDED_2 * SIDE_LENGTH_PADDED_2 + (y + 1) * SIDE_LENGTH_PADDED_2 + (z + 1)];
    }
    
    public int getColor(Vector3i vec) {
    	return getColor(vec.x, vec.y, vec.z);
    }

    private boolean isSolid(int x, int y, int z) {
        return getColor(x, y, z) != 0;
    }
    
    private boolean isSolid(Vector3i vec) {
    	return isSolid(vec.x, vec.y, vec.z);
    }

//    private void setColor(int x, int y, int z, int color) {
//        colors[(x + 1) * SIDE_LENGTH_PADDED_2 * SIDE_LENGTH_PADDED_2 + (y + 1) * SIDE_LENGTH_PADDED_2 + (z + 1)] = color;
//    }
//    
//    private void setColor(Vector3i vec, int color) {
//    	setColor(vec.x, vec.y, vec.z, color);
//    }
    
    public List<Vector3i> getXyFrontQuadVertices() {
    	return Collections.unmodifiableList(xyFrontQuadVertices);
    }
    
    public List<Vector3i> getXzFrontQuadVertices() {
    	return Collections.unmodifiableList(xzFrontQuadVertices);
    }
    
    public List<Vector3i> getYzFrontQuadVertices() {
    	return Collections.unmodifiableList(yzFrontQuadVertices);
    }
    
    public List<Integer> getXyFrontQuadColors() {
    	return Collections.unmodifiableList(xyFrontQuadColors);
    }
    
    public List<Integer> getXzFrontQuadColors() {
    	return Collections.unmodifiableList(xzFrontQuadColors);
    }
    
    public List<Integer> getYzFrontQuadColors() {
    	return Collections.unmodifiableList(yzFrontQuadColors);
    }
    
    public List<Vector3i> getXyBackQuadVertices() {
    	return Collections.unmodifiableList(xyBackQuadVertices);
    }
    
    public List<Vector3i> getXzBackQuadVertices() {
    	return Collections.unmodifiableList(xzBackQuadVertices);
    }
    
    public List<Vector3i> getYzBackQuadVertices() {
    	return Collections.unmodifiableList(yzBackQuadVertices);
    }
    
    public List<Integer> getXyBackQuadColors() {
    	return Collections.unmodifiableList(xyBackQuadColors);
    }
    
    public List<Integer> getXzBackQuadColors() {
    	return Collections.unmodifiableList(xzBackQuadColors);
    }
    
    public List<Integer> getYzBackQuadColors() {
    	return Collections.unmodifiableList(yzBackQuadColors);
    }
    
    private void computeVertices() {
    	Vector3i cur = new Vector3i(0, 0, 0);
    	Vector3i buf = new Vector3i(); // for utility
		for (cur.x=0; cur.x<SIDE_LENGTH; cur.add(1, 0, 0)) {
			for (cur.y=0; cur.y<SIDE_LENGTH; cur.add(0, 1, 0)) {
				for (cur.z=0; cur.z<SIDE_LENGTH; cur.add(0, 0, 1)) {
	    			if (isSolid(cur)) {
	    				if (!isSolid(cur.add(VEC_XY_FRONT, buf))) {
	    					xyFrontQuadVertices.add(cur.add(0, 0, 1, new Vector3i()));
	    					xyFrontQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_XY_BACK, buf))) {
	    					xyBackQuadVertices.add(cur.add(0, 0, 0, new Vector3i()));
	    					xyBackQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_XZ_FRONT, buf))) {
	    					xzFrontQuadVertices.add(cur.add(0, 0, 0, new Vector3i()));
	    					xzFrontQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_XZ_BACK, buf))) {
	    					xzBackQuadVertices.add(cur.add(0, 1, 0, new Vector3i()));
	    					xzBackQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_YZ_FRONT, buf))) {
	    					yzFrontQuadVertices.add(cur.add(0, 0, 0, new Vector3i()));
	    					yzFrontQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_YZ_BACK, buf))) {
	    					yzBackQuadVertices.add(cur.add(1, 0, 0, new Vector3i()));
	    					yzBackQuadColors.add(getColor(cur));
	    				}
	    			}
	    		}
    		}
		}
    }
}
