package chunk;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.javatuples.Pair;
import org.joml.Vector3f;
import org.joml.Vector3i;

import game.Scene;

public class World {
	private static final int TAXICAB_MAX_DISTANCE = 10;

	private Map<Vector3i, ChunkRenderer> chunks = new HashMap<>();
	private Deque<Pair<Vector3i, Chunk>> unprocessedChunks = new ConcurrentLinkedDeque<>();
	private ChunkSupplier chunkSupplier;
	
	public World(ChunkSupplier chunkSupplier) {
		this.chunkSupplier = chunkSupplier;
	}

	public void render() {
		chunks.forEach((v, r) -> {
			if (r == null) return;
			
			Vector3f translation = new Vector3f(v.x, v.y, v.z).add(-0.5f, -0.5f, -0.5f).mul(Chunk.SIDE_LENGTH);
			r.render(translation);
		});
	}

	public void update() {
		Vector3f position = Scene.getCamera().position;
		int cx = (int) (-position.x / Chunk.SIDE_LENGTH);
		int cy = (int) (-position.y / Chunk.SIDE_LENGTH);
		int cz = (int) (-position.z / Chunk.SIDE_LENGTH);

		Set<Entry<Vector3i, ChunkRenderer>> chunksSet = chunks.entrySet();
		for (int i = 0; i < chunksSet.size(); ++i) {
			chunksSet.removeIf(e -> {
				if (e == null) return false;
				
				Vector3i b = e.getKey();
				int taxicabDistance = Math.abs(b.x - cx) + Math.abs(b.y - cy) + Math.abs(b.z - cz);
				if (taxicabDistance > TAXICAB_MAX_DISTANCE) {
					// side effect!
					e.getValue().destroy();
					return true;
				} else {
					return false;
				}
			});
		}

		Pair<Vector3i, Chunk> unprocessedChunk;
		while ((unprocessedChunk = unprocessedChunks.poll()) != null) {
			ChunkRenderer r = new ChunkRenderer(unprocessedChunk.getValue1());
			if (chunks.containsKey(unprocessedChunk.getValue0())) {
				// TODO: should never happen, but it does (thread safety bug)
				System.err.println("Warning: Contains key already!");
				
				// avoid memory leaks
				chunks.get(unprocessedChunk.getValue0()).destroy();
			}
			chunks.put(unprocessedChunk.getValue0(), r);
		}
	}
	
	public void compute() {
		Vector3f position = Scene.getCamera().position;
		int cx = (int) (-position.x / Chunk.SIDE_LENGTH);
		int cy = (int) (-position.y / Chunk.SIDE_LENGTH);
		int cz = (int) (-position.z / Chunk.SIDE_LENGTH);

		for (int i = -TAXICAB_MAX_DISTANCE; i <= TAXICAB_MAX_DISTANCE; ++i) {
			for (int j = -2; j <= 2; ++j) {
				for (int k = -TAXICAB_MAX_DISTANCE; k <= TAXICAB_MAX_DISTANCE; ++k) {
					int taxicabDistance = Math.abs(i) + Math.abs(j) + Math.abs(k);
					if (taxicabDistance <= TAXICAB_MAX_DISTANCE) {
						Vector3i vec = new Vector3i(i + cx, j + cy, k + cz);
						if (!chunks.containsKey(vec) && !unprocessedChunks.stream().anyMatch(t -> t.getValue0().equals(vec))) {
							unprocessedChunks.add(new Pair<>(vec, chunkSupplier.get(vec)));
						}
					}
				}
			}
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}
}
