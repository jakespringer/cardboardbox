package chunk;

import org.joml.Vector3i;

public interface ChunkSupplier {
	public Chunk get(int x, int y, int z);
	
	public default Chunk get(Vector3i vec) {
		return get(vec.x, vec.y, vec.z);
	}
}
