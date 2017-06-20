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
			// xy front axis
			0, 0, 0,
			1, 0, 0,
			0, 1, 0,
			1, 1, 0,
			
			// xz front axis
			0, 0, 0,
			1, 0, 0,
			0, 0, 1,
			1, 0, 1,
			
			// yz front axis
			0, 0, 0,
			0, 0, 1,
			0, 1, 0,
			0, 1, 1,
			
			// xy back axis
			0, 0, 0,
			1, 0, 0,
			0, 1, 0,
			1, 1, 0,
			
			// xz back axis
			0, 0, 0,
			1, 0, 0,
			0, 0, 1,
			1, 0, 1,
			
			// yz back axis
			0, 0, 0,
			0, 0, 1,
			0, 1, 0,
			0, 1, 1,
	};
	
	private static final float[] XYXZYZ_QUAD_NORMALS = new float[] {
			// xy front axis
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			
			// xz front axis
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0,
			
			// yz front axis
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			-1, 0, 0,
			
			// xy back axis
			0, 0, -1,
			0, 0, -1,
			0, 0, -1,
			0, 0, -1,
			
			// xz back axis
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			
			// yz back axis
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
			1, 0, 0,
	};
	private static BufferObject xyxzyzQuadVerticesBufferObject;
	private static BufferObject xyxzyzQuadNormalsBufferObject;
	private static ShaderProgram chunkProgram;
	
	private VertexArrayObject xyFrontVao;
	private VertexArrayObject xzFrontVao;
	private VertexArrayObject yzFrontVao;
	private VertexArrayObject xyBackVao;
	private VertexArrayObject xzBackVao;
	private VertexArrayObject yzBackVao;
	private BufferObject xyFrontQuadsBufferObject;
	private BufferObject xzFrontQuadsBufferObject;
	private BufferObject yzFrontQuadsBufferObject;
	private BufferObject xyBackQuadsBufferObject;
	private BufferObject xzBackQuadsBufferObject;
	private BufferObject yzBackQuadsBufferObject;
	private BufferObject xyFrontColorsBufferObject;
	private BufferObject xzFrontColorsBufferObject;
	private BufferObject yzFrontColorsBufferObject;
	private BufferObject xyBackColorsBufferObject;
	private BufferObject xzBackColorsBufferObject;
	private BufferObject yzBackColorsBufferObject;
	private int xyFrontQuadsCount;
	private int xzFrontQuadsCount;
	private int yzFrontQuadsCount;
	private int xyBackQuadsCount;
	private int xzBackQuadsCount;
	private int yzBackQuadsCount;
	
	private final VertexArrayObject[] vaos;
	private final BufferObject[] vbos;
	private final BufferObject[] cbos;
	private final int[] quadCounts;
	
	public ChunkRenderer(Chunk chunk) {
		if (xyxzyzQuadVerticesBufferObject == null) {
			xyxzyzQuadVerticesBufferObject = new BufferObject(BufferObject.Target.ARRAY_BUFFER, BufferObject.UsageHint.STATIC_DRAW, XYXZYZ_QUAD_VERTICES);
			xyxzyzQuadNormalsBufferObject = new BufferObject(BufferObject.Target.ARRAY_BUFFER, BufferObject.UsageHint.STATIC_DRAW, XYXZYZ_QUAD_NORMALS);
			chunkProgram = new ShaderProgram(Resources.get("glsl/chunk.vert"), Resources.get("glsl/chunk.frag"));
		}
				
		List<Vector3i> xyFrontVerts = chunk.getXyFrontQuadVertices();
		List<Integer> xyFrontColors = chunk.getXyFrontQuadColors();
		float[] xyFrontData = new float[xyFrontVerts.size()*3];
		int[] xyFrontColorData = new int[xyFrontColors.size()];
		for (int i=0; i<xyFrontVerts.size(); ++i) {
			Vector3i c = xyFrontVerts.get(i);
			xyFrontData[i*3 + 0] = c.x;
			xyFrontData[i*3 + 1] = c.y;
			xyFrontData[i*3 + 2] = c.z;
			xyFrontColorData[i] = xyFrontColors.get(i);
		}
		
		List<Vector3i> xzFrontVerts = chunk.getXzFrontQuadVertices();
		List<Integer> xzFrontColors = chunk.getXzFrontQuadColors();
		float[] xzFrontData = new float[xzFrontVerts.size()*3];
		int[] xzFrontColorData = new int[xzFrontColors.size()];
		for (int i=0; i<xzFrontVerts.size(); ++i) {
			Vector3i c = xzFrontVerts.get(i);
			xzFrontData[i*3 + 0] = c.x;
			xzFrontData[i*3 + 1] = c.y;
			xzFrontData[i*3 + 2] = c.z;
			xzFrontColorData[i] = xzFrontColors.get(i);
		}
		
		List<Vector3i> yzFrontVerts = chunk.getYzFrontQuadVertices();
		List<Integer> yzFrontColors = chunk.getYzFrontQuadColors();
		float[] yzFrontData = new float[yzFrontVerts.size()*3];
		int[] yzFrontColorData = new int[yzFrontColors.size()];
		for (int i=0; i<yzFrontVerts.size(); ++i) {
			Vector3i c = yzFrontVerts.get(i);
			yzFrontData[i*3 + 0] = c.x;
			yzFrontData[i*3 + 1] = c.y;
			yzFrontData[i*3 + 2] = c.z;
			yzFrontColorData[i] = yzFrontColors.get(i);
		}
		
		List<Vector3i> xyBackVerts = chunk.getXyBackQuadVertices();
		List<Integer> xyBackColors = chunk.getXyBackQuadColors();
		float[] xyBackData = new float[xyBackVerts.size()*3];
		int[] xyBackColorData = new int[xyBackColors.size()];
		for (int i=0; i<xyBackVerts.size(); ++i) {
			Vector3i c = xyBackVerts.get(i);
			xyBackData[i*3 + 0] = c.x;
			xyBackData[i*3 + 1] = c.y;
			xyBackData[i*3 + 2] = c.z;
			xyBackColorData[i] = xyBackColors.get(i);
		}
		
		List<Vector3i> xzBackVerts = chunk.getXzBackQuadVertices();
		List<Integer> xzBackColors = chunk.getXzBackQuadColors();
		float[] xzBackData = new float[xzBackVerts.size()*3];
		int[] xzBackColorData = new int[xzBackColors.size()];
		for (int i=0; i<xzBackVerts.size(); ++i) {
			Vector3i c = xzBackVerts.get(i);
			xzBackData[i*3 + 0] = c.x;
			xzBackData[i*3 + 1] = c.y;
			xzBackData[i*3 + 2] = c.z;
			xzBackColorData[i] = xzBackColors.get(i);
		}
		
		List<Vector3i> yzBackVerts = chunk.getYzBackQuadVertices();
		List<Integer> yzBackColors = chunk.getYzBackQuadColors();
		float[] yzBackData = new float[yzBackVerts.size()*3];
		int[] yzBackColorData = new int[yzBackColors.size()];
		for (int i=0; i<yzBackVerts.size(); ++i) {
			Vector3i c = yzBackVerts.get(i);
			yzBackData[i*3 + 0] = c.x;
			yzBackData[i*3 + 1] = c.y;
			yzBackData[i*3 + 2] = c.z;
			yzBackColorData[i] = yzBackColors.get(i);
		}
		
		xyFrontVao = new VertexArrayObject();
		xzFrontVao = new VertexArrayObject();
		yzFrontVao = new VertexArrayObject();
		xyBackVao = new VertexArrayObject();
		xzBackVao = new VertexArrayObject();
		yzBackVao = new VertexArrayObject();
		xyFrontQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xyFrontData);
		xzFrontQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xzFrontData);
		yzFrontQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, yzFrontData);
		xyBackQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xyBackData);
		xzBackQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xzBackData);
		yzBackQuadsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, yzBackData);
		xyFrontColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xyFrontColorData);
		xzFrontColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xzFrontColorData);
		yzFrontColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, yzFrontColorData);
		xyBackColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xyBackColorData);
		xzBackColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, xzBackColorData);
		yzBackColorsBufferObject = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, yzBackColorData);
		xyFrontQuadsCount = xyFrontData.length / 3;
		xzFrontQuadsCount = xzFrontData.length / 3;
		yzFrontQuadsCount = yzFrontData.length / 3;
		xyBackQuadsCount = xyBackData.length / 3;
		xzBackQuadsCount = xzBackData.length / 3;
		yzBackQuadsCount = yzBackData.length / 3;
				
		vbos = new BufferObject[] { xyFrontQuadsBufferObject, xzFrontQuadsBufferObject, yzFrontQuadsBufferObject, xyBackQuadsBufferObject, xzBackQuadsBufferObject, yzBackQuadsBufferObject };
		cbos = new BufferObject[] { xyFrontColorsBufferObject, xzFrontColorsBufferObject, yzFrontColorsBufferObject, xyBackColorsBufferObject, xzBackColorsBufferObject, yzBackColorsBufferObject }; 
		vaos = new VertexArrayObject[] { xyFrontVao, xzFrontVao, yzFrontVao, xyBackVao, xzBackVao, yzBackVao };
		quadCounts = new int[] { xyFrontQuadsCount, xzFrontQuadsCount, yzFrontQuadsCount, xyBackQuadsCount, xzBackQuadsCount, yzBackQuadsCount };
	}
	
	public void render(Vector3f translation) {
		for (int i=0; i<6; ++i) {
			VertexArrayObject vao = vaos[i];
			BufferObject vbo = vbos[i];
			BufferObject cbo = cbos[i];
			int quadCount = quadCounts[i];
			
			try (VertexArrayObjectResource vaor = vao.use();
				 ShaderProgramResource spr = chunkProgram.use()) {

				spr.setUniform("modelViewMatrix", Scene.getCamera().getModelViewMatrix(translation));
				spr.setUniform("projectionMatrix", Camera.getProjectionMatrix(1.0f, (float) Window.getWidth(), (float) Window.getHeight(), 0.1f, 512.0f));
								
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
				
				glEnableVertexAttribArray(3);
				try (BufferObjectResource bor = xyxzyzQuadNormalsBufferObject.use()) {
					glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, i*12*4);
				}
								
				glVertexAttribDivisor(0, 0);
				glVertexAttribDivisor(1, 1);
				glVertexAttribDivisor(2, 1);
				glVertexAttribDivisor(3, 0);
				
				glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 4, quadCount);
								
				glDisableVertexAttribArray(0);
				glDisableVertexAttribArray(1);
				glDisableVertexAttribArray(2);
				glDisableVertexAttribArray(3);
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
