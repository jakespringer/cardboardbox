package chunk;

import util.Noise;

public class SimplexNoiseChunkSupplier implements ChunkSupplier {
	private Noise noise = new Noise(System.nanoTime() / 1e9);

	@Override
	public Chunk get(int x, int y, int z) {
		int[] chunkColors = new int[Chunk.SIDE_LENGTH_PADDED_2 * Chunk.SIDE_LENGTH_PADDED_2
				* Chunk.SIDE_LENGTH_PADDED_2];

		int rBaseX = x * Chunk.SIDE_LENGTH;
		int rBaseY = y * Chunk.SIDE_LENGTH;
		int rBaseZ = z * Chunk.SIDE_LENGTH;
		if (noise.fbm(rBaseX / 1e3, rBaseZ / 1e3, 2, 2) * 20 < rBaseY+100) {
			for (int i = 0; i < Chunk.SIDE_LENGTH_PADDED_2; ++i) {
				for (int k = 0; k < Chunk.SIDE_LENGTH_PADDED_2; ++k) {
					int rx = x * Chunk.SIDE_LENGTH + i;
					int rz = z * Chunk.SIDE_LENGTH + k;
	//				double height = Math.sin(rx / 20.0) * Math.cos(rz / 20.0) * 10;
					double height = noise.fbm(rx / 1e3, rz / 1e3, 6, 2) * 40;
					for (int j = 0; j < Chunk.SIDE_LENGTH_PADDED_2 && rBaseY + j < height; ++j) {
						int ry = y * Chunk.SIDE_LENGTH + j;
						if (ry < height) {
							// chunkColors[i*Chunk.SIDE_LENGTH_PADDED_2*Chunk.SIDE_LENGTH_PADDED_2
							// + j*Chunk.SIDE_LENGTH_PADDED_2 + k] = (int)
							// (Math.abs(Math.random()) * 0x00FFFFFF);
							chunkColors[i * Chunk.SIDE_LENGTH_PADDED_2 * Chunk.SIDE_LENGTH_PADDED_2
									+ j * Chunk.SIDE_LENGTH_PADDED_2 + k] = 0x0000FF00;
						} else {
							chunkColors[i * Chunk.SIDE_LENGTH_PADDED_2 * Chunk.SIDE_LENGTH_PADDED_2
									+ j * Chunk.SIDE_LENGTH_PADDED_2 + k] = 0;
						}
					}
				}
			}
		}

		return new Chunk(chunkColors);
	}
}
