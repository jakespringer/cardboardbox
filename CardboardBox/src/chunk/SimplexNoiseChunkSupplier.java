package chunk;

import java.util.HashMap;

import util.Noise;
import util.VectorKey;

public class SimplexNoiseChunkSupplier implements ChunkSupplier {
	private Noise noise;
	private HashMap<VectorKey, Chunk> cache = new HashMap<>();
	
	public SimplexNoiseChunkSupplier(double seed) {
		noise = new Noise(seed);
	}
	
	public SimplexNoiseChunkSupplier() {
		this((double) System.currentTimeMillis());
	}

	@Override
	public Chunk get(int x, int y, int z) {
		VectorKey key = new VectorKey(x, y, z);
		if (cache.containsKey(key)) {
			return cache.get(key);
		} else {
			Chunk chunk = new Chunk();
			for (int i=0; i<Chunk.SIDE_LENGTH; ++i) {
				for (int j=0; j<Chunk.SIDE_LENGTH; ++j) {
					int height = (int) Math.floor(noise.fbm(x*Chunk.SIDE_LENGTH+i, z*Chunk.SIDE_LENGTH+j, 4, 100));
					if ((height / Chunk.SIDE_LENGTH) == y) {
						chunk.setColor(i, height % Chunk.SIDE_LENGTH, j, 0x00AA00);
					}
				}
			}
			cache.put(key, chunk);
			return chunk;
		}
	}
}
