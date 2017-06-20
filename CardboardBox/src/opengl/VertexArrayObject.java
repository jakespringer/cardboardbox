package opengl;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {
	private int handle;
	
	public VertexArrayObject() {
		handle = glGenVertexArrays();
	}
	
	public void bind() {
		glBindVertexArray(handle);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}
	
	public void destroy() {
		glDeleteVertexArrays(handle);
	}
	
	public VertexArrayObjectResource use() {
		return new VertexArrayObjectResource(this);
	}
	
	public static class VertexArrayObjectResource implements AutoCloseable {
		private VertexArrayObject obj;
		
		private VertexArrayObjectResource(VertexArrayObject obj) {
			this.obj = obj;
			obj.bind();
		}
		
		@Override
		public void close() {
			obj.unbind();
		}
	}
}
