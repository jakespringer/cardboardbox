package chunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joml.Vector3i;

public class Chunk {
	private static final int SIDE_LENGTH = 64;
	private static final int SIDE_LENGTH_PADDED_2 = SIDE_LENGTH + 2;
	private static final Vector3i VEC_XY_FRONT = new Vector3i(0, 0, 1);
	private static final Vector3i VEC_XY_BACK = new Vector3i(0, 0, -1);
	private static final Vector3i VEC_XZ_FRONT = new Vector3i(0, -1, 0);
	private static final Vector3i VEC_XZ_BACK = new Vector3i(0, 1, 0);
	private static final Vector3i VEC_YZ_FRONT = new Vector3i(-1, 0, 0);
	private static final Vector3i VEC_YZ_BACK = new Vector3i(1, 0, 0);
	
	private int[] colors = new int[SIDE_LENGTH_PADDED_2 * SIDE_LENGTH_PADDED_2 * SIDE_LENGTH_PADDED_2];
	private List<Vector3i> xyQuadVertices = new ArrayList<>();
	private List<Vector3i> xzQuadVertices = new ArrayList<>();
	private List<Vector3i> yzQuadVertices = new ArrayList<>();
	private List<Integer> xyQuadColors = new ArrayList<>();
	private List<Integer> xzQuadColors = new ArrayList<>();
	private List<Integer> yzQuadColors = new ArrayList<>();
	
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

    private void setColor(int x, int y, int z, int color) {
        colors[(x + 1) * SIDE_LENGTH_PADDED_2 * SIDE_LENGTH_PADDED_2 + (y + 1) * SIDE_LENGTH_PADDED_2 + (z + 1)] = color;
    }
    
    private void setColor(Vector3i vec, int color) {
    	setColor(vec.x, vec.y, vec.z, color);
    }
    
    public List<Vector3i> getXyQuadVertices() {
    	return Collections.unmodifiableList(xyQuadVertices);
    }
    
    public List<Vector3i> getXzQuadVertices() {
    	return Collections.unmodifiableList(xzQuadVertices);
    }
    
    public List<Vector3i> getYzQuadVertices() {
    	return Collections.unmodifiableList(yzQuadVertices);
    }
    
    public List<Integer> getXyQuadColors() {
    	return Collections.unmodifiableList(xyQuadColors);
    }
    
    public List<Integer> getXzQuadColors() {
    	return Collections.unmodifiableList(xzQuadColors);
    }
    
    public List<Integer> getYzQuadColors() {
    	return Collections.unmodifiableList(yzQuadColors);
    }
    
    private void computeVertices() {
    	Vector3i cur = new Vector3i(0, 0, 0);
    	Vector3i buf = new Vector3i(); // for utility
		for (cur.x=0; cur.x<SIDE_LENGTH; cur.add(1, 0, 0)) {
			for (cur.y=0; cur.y<SIDE_LENGTH; cur.add(0, 1, 0)) {
				for (cur.z=0; cur.z<SIDE_LENGTH; cur.add(0, 0, 1)) {
	    			if (isSolid(cur)) {
	    				if (!isSolid(cur.add(VEC_XY_FRONT, buf))) {
	    					xyQuadVertices.add(cur.add(0, 0, 1, new Vector3i()));
	    					xyQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_XY_BACK, buf))) {
	    					xyQuadVertices.add(cur.add(0, 0, 0, new Vector3i()));
	    					xyQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_XZ_FRONT, buf))) {
	    					xzQuadVertices.add(cur.add(0, 0, 0, new Vector3i()));
	    					xzQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_XZ_BACK, buf))) {
	    					xzQuadVertices.add(cur.add(0, 1, 0, new Vector3i()));
	    					xzQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_YZ_FRONT, buf))) {
	    					yzQuadVertices.add(cur.add(0, 0, 0, new Vector3i()));
	    					yzQuadColors.add(getColor(cur));
	    				}
	    				
	    				if (!isSolid(cur.add(VEC_YZ_BACK, buf))) {
	    					yzQuadVertices.add(cur.add(1, 0, 0, new Vector3i()));
	    					yzQuadColors.add(getColor(cur));
	    				}
	    			}
	    		}
    		}
		}
    }
}
