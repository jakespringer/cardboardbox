package entity;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.util.function.Consumer;

import org.joml.Vector3f;

import game.Scene;
import opengl.BufferObject;
import opengl.BufferObject.BufferObjectResource;
import opengl.BufferObject.Target;
import opengl.BufferObject.UsageHint;
import opengl.ShaderProgram;
import opengl.ShaderProgram.ShaderProgramResource;
import opengl.VertexArrayObject;
import opengl.VertexArrayObject.VertexArrayObjectResource;
import util.Resources;

public class SimpleEntity {
		
	private static VertexArrayObject vao;
	private static BufferObject vbo;
	private static BufferObject ebo;
	private static ShaderProgram program;
	
	private Vector3f position;
	
	public SimpleEntity(Vector3f position) {
		if (vao == null) {
			 vao = new VertexArrayObject();
			 vbo = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, new float[] {
					 0, 0, 0, // bottom back left
					 0, 0, 1, // bottom front left
					 1, 0, 0, // bottom back right
					 1, 0, 1, // bottom front right
					 0, 2, 0, // top back left
					 0, 2, 1, // top front left
					 1, 2, 0, // top back right
					 1, 2, 1, // top front right
			 });
			 ebo = new BufferObject(Target.ELEMENT_ARRAY_BUFFER, UsageHint.STATIC_DRAW, new int[] {
					 0, 1, 3, // bottom
					 0, 3, 2,
					 
					 4, 5, 7, // top
					 4, 7, 6,
					 
					 1, 3, 7, // front
					 1, 7, 5,
					 
					 0, 2, 6, // back
					 0, 6, 4,
					 
					 0, 1, 5, // left
					 0, 5, 4,
					 
					 2, 3, 7, // right
					 2, 7, 6
			 });
			 program = new ShaderProgram(Resources.get("glsl/simpleentity.vert"), Resources.get("glsl/simpleentity.frag"));
		}
		
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	
	public void setPosition(Vector3f newPosition) {
		position = new Vector3f(newPosition);
	}
	
	public void updatePosition(Consumer<Vector3f> vecUpdater) {
		vecUpdater.accept(position);
	}
	
	public Vector3f getBackingPosition() {
		return position;
	}
	
	public void render() {
		try (VertexArrayObjectResource vaor = vao.use();
      			 ShaderProgramResource spr = program.use()) {
           	spr.setUniform("projectionMatrix", Scene.getProjectionMatrix());
           	spr.setUniform("modelViewMatrix", Scene.getCamera().getModelViewMatrix(position));
           	
      			glEnableVertexAttribArray(0);
      			try (BufferObjectResource bor = vbo.use()) {
      				glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
      			}
      			
      			try (BufferObjectResource bor = ebo.use()) {
      				glDrawElements(GL_TRIANGLES, 6*3*2, GL_UNSIGNED_INT, 0);
      			}
      			      			
      			glDisableVertexAttribArray(0);
      		}
	}
	
	public void update() {
		
	}
	
	public void destroy() {
		vao.destroy();
		vbo.destroy();
		ebo.destroy();
	}
}
