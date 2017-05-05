package chunk;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector3i;

import game.Camera;
import game.Scene;
import game.Window;
import opengl.BufferObject;
import opengl.BufferObject.BufferObjectResource;
import opengl.BufferObject.Target;
import opengl.BufferObject.UsageHint;
import opengl.ShaderProgram;
import opengl.ShaderProgram.ShaderProgramResource;
import opengl.VertexArrayObject;
import opengl.VertexArrayObject.VertexArrayObjectResource;
import util.Resources;

public class ChunkRenderer {
	private static final float[] XYXZYZ_QUAD_VERTICES = new float[] {
			// xy axis
			0, 0, 0,
			1, 0, 0,
			0, 1, 0,
			1, 1, 0,
			
			// xz axis
			0, 0, 0,
			1, 0, 0,
			0, 0, 1,
			1, 0, 1,
			
			// yz axis
			0, 0, 0,
			0, 0, 1,
			0, 1, 0,
			0, 1, 1,
	};
	private static BufferObject xyxzyzQuadVerticesBufferObject;
	private static ShaderProgram chunkProgram;
	
	private VertexArrayObject xyVao;
	private VertexArrayObject xzVao;
	private VertexArrayObject yzVao;
	private BufferObject xyQuadsBufferObject;
	private BufferObject xzQuadsBufferObject;
	private BufferObject yzQuadsBufferObject;
	private BufferObject xyColorsBufferObject;
	private BufferObject xzColorsBufferObject;
	private BufferObject yzColorsBufferObject;
	private int xyQuadsCount;
	private int xzQuadsCount;
	private int yzQuadsCount;
	
	private final VertexArrayObject[] vaos;
	private final BufferObject[] vbos;
	private final BufferObject[] cbos;
	private final int[] quadCounts;
	
	public ChunkRenderer(Chunk chunk) {
		if (xyxzyzQuadVerticesBufferObject == null) {
			xyxzyzQuadVerticesBufferObject = new BufferObject(BufferObject.Target.ARRAY_BUFFER, BufferObject.UsageHint.STATIC_DRAW, XYXZYZ_QUAD_VERTICES);
			chunkProgram = new ShaderProgram(Resources.get("glsl/chunk.vert"), Resources.get("glsl/chunk.frag"));
		}
				
		List<Vector3i> xyVerts = chunk.getXyQuadVertices();
		List<Integer> xyColors = chunk.getXyQuadColors();
		float[] xyData = new float[xyVerts.size()*3];
		int[] xyColorData = new int[xyColors.size()];
		for (int i=0; i<xyVerts.size(); ++i) {
			Vector3i c = xyVerts.get(i);
			xyData[i*3 + 0] = c.x;
			xyData[i*3 + 1] = c.y;
			xyData[i*3 + 2] = c.z;
			xyColorData[i] = xyColors.get(i);
		}
		
		List<Vector3i> xzVerts = chunk.getXzQuadVertices();
		List<Integer> xzColors = chunk.getXzQuadColors();
		float[] xzData = new float[xzVerts.size()*3];
		int[] xzColorData = new int[xzColors.size()];
		for (int i=0; i<xzVerts.size(); ++i) {
			Vector3i c = xzVerts.get(i);
			xzData[i*3 + 0] = c.x;
			xzData[i*3 + 1] = c.y;
			xzData[i*3 + 2] = c.z;
			xzColorData[i] = xzColors.get(i);
		}
		
		List<Vector3i> yzVerts = chunk.getYzQuadVertices();
		List<Integer> yzColors = chunk.getYzQuadColors();
		float[] yzData = new float[yzVerts.size()*3];
		int[] yzColorData = new int[yzColors.size()];
		for (int i=0; i<yzVerts.size(); ++i) {
			Vector3i c = yzVerts.get(i);
			yzData[i*3 + 0] = c.x;
			yzData[i*3 + 1] = c.y;
			yzData[i*3 + 2] = c.z;
			yzColorData[i] = yzColors.get(i);
		}
		
		xyVao = new VertexArrayObject();
		xzVao = new VertexArrayObject();
		yzVao = new VertexArrayObject();
		xyQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xyData);
		xzQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xzData);
		yzQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, yzData);
		xyColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xyColorData);
		xzColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xzColorData);
		yzColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, yzColorData);
		xyQuadsCount = xyData.length / 3;
		xzQuadsCount = xzData.length / 3;
		yzQuadsCount = yzData.length / 3;
				
		vbos = new BufferObject[] { xyQuadsBufferObject, xzQuadsBufferObject, yzQuadsBufferObject };
		cbos = new BufferObject[] { xyColorsBufferObject, xzColorsBufferObject, yzColorsBufferObject }; 
		vaos = new VertexArrayObject[] { xyVao, xzVao, yzVao };
		quadCounts = new int[] { xyQuadsCount, xzQuadsCount, yzQuadsCount };
	}
	
	public void render(Vector3f translation) {
		for (int i=0; i<3; ++i) {
			VertexArrayObject vao = vaos[i];
			BufferObject vbo = vbos[i];
			BufferObject cbo = cbos[i];
			int quadCount = quadCounts[i];
			
			try (VertexArrayObjectResource vaor = vao.use();
				 ShaderProgramResource spr = chunkProgram.use()) {
								
				spr.setUniform("viewMatrix", Scene.getCamera().getModelViewMatrix(translation));
				spr.setUniform("projectionMatrix", Camera.getProjectionMatrix(1.0f, (float) Window.getWidth(), (float) Window.getHeight(), 0.1f, 100.0f));
								
				glEnableVertexAttribArray(0);
				try (BufferObjectResource bor = xyxzyzQuadVerticesBufferObject.use()) {
					glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, i*12*4);
				}
				
				glEnableVertexAttribArray(1);
				try (BufferObjectResource bor = vbo.use()) {
					glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
				}
				
				glEnableVertexAttribArray(2);
				try (BufferObjectResource bor = cbo.use()) {
					glVertexAttribPointer(2, 4, GL_UNSIGNED_BYTE, true, 0, 0);
				}
								
				glVertexAttribDivisor(0, 0);
				glVertexAttribDivisor(1, 1);
				glVertexAttribDivisor(2, 1);
				
				glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 4, quadCount);
								
				glDisableVertexAttribArray(0);
				glDisableVertexAttribArray(1);
			}
		}
	}
	
	public void destroy() {
		for (BufferObject bo : vbos) {
			bo.destroy();
		}
		
		for (BufferObject bo : cbos) {
			bo.destroy();
		}
		
		for (VertexArrayObject ao : vaos) {
			ao.destroy();
		}
	}
}
