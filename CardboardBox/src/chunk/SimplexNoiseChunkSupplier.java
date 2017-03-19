package chunk;

import java.util.HashMap;

import org.joml.Vector3ic;

import util.Noise;
import util.VectorKey;

public class SimplexNoiseChunkSupplier implements ChunkSupplier {
	private Noise noise;
	private HashMap<Vector3ic, Chunk> cache = new HashMap<>();
	
	public SimplexNoiseChunkSupplier(double seed) {
		noise = new Noise(seed);
	}
	
	public SimplexNoiseChunkSupplier() {
		this((double) System.currentTimeMillis());
	}

	@Override
	public Chunk get(int x, int y, int z) {
		cache.containsKey(new VectorKey(x, y, z));
		
		Chunk chunk = new Chunk();
		
		for (int i=0; i<Chunk.SIDE_LENGTH; ++i) {
			for (int j=0; j<Chunk.SIDE_LENGTH; ++j) {
				int height = (int) Math.floor(noise.fbm(x*Chunk.SIDE_LENGTH+i, z*Chunk.SIDE_LENGTH+j, 4, 100));
				if ((height / Chunk.SIDE_LENGTH) == y) {
					chunk.setColor(i, height % Chunk.SIDE_LENGTH, j, 0x00AA00);
				}
			}
		}
		
		return chunk;
	}
}
