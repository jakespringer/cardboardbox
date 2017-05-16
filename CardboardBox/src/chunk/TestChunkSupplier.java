package chunk;

public class TestChunkSupplier implements ChunkSupplier {

	@Override
	public Chunk get(int x, int y, int z) {
		int[] chunkColors = new int[287496];
		for (int i0 = 0; i0 < chunkColors.length; ++i0) {
			double rand = Math.random();
			double threshold = 0.995;
			if (rand > threshold) {
				int split = 287496 / 3;
				if (i0 < split * 1)
					chunkColors[i0] = 0x00FF0000;
				else if (i0 < split * 2)
					chunkColors[i0] = 0x000000FF;
				else
					chunkColors[i0] = 0x0000FF00;
			} else {
				chunkColors[i0] = 0;
			}
		}
		
		Chunk chunk = new Chunk(chunkColors);
		return chunk;
	}

}
