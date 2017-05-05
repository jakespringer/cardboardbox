package opengl;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_COPY;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_COPY;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_READ;
import static org.lwjgl.opengl.GL15.GL_STREAM_COPY;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_READ;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL21.GL_PIXEL_PACK_BUFFER;
import static org.lwjgl.opengl.GL21.GL_PIXEL_UNPACK_BUFFER;
import static org.lwjgl.opengl.GL30.GL_TRANSFORM_FEEDBACK_BUFFER;
import static org.lwjgl.opengl.GL31.GL_COPY_READ_BUFFER;
import static org.lwjgl.opengl.GL31.GL_COPY_WRITE_BUFFER;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL40.GL_DRAW_INDIRECT_BUFFER;
import static org.lwjgl.opengl.GL42.GL_ATOMIC_COUNTER_BUFFER;
import static org.lwjgl.opengl.GL43.GL_DISPATCH_INDIRECT_BUFFER;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static org.lwjgl.opengl.GL44.GL_QUERY_BUFFER;

public class BufferObject {
	public static enum Target {
		ARRAY_BUFFER(GL_ARRAY_BUFFER),
		ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER),
		COPY_READ_BUFFER(GL_COPY_READ_BUFFER),
		COPY_WRITE_BUFFER(GL_COPY_WRITE_BUFFER),
		PIXEL_UNPACK_BUFFER(GL_PIXEL_UNPACK_BUFFER),
		PIXEL_PACK_BUFFER(GL_PIXEL_PACK_BUFFER),
		QUERY_BUFFER(GL_QUERY_BUFFER),
		TEXTURE_BUFFER(GL_TEXTURE_BUFFER),
		TRANSFORM_FEEDBACK_BUFFER(GL_TRANSFORM_FEEDBACK_BUFFER),
		UNIFORM_BUFFER(GL_UNIFORM_BUFFER),
		DRAW_INDIRECT_BUFFER(GL_DRAW_INDIRECT_BUFFER),
		ATOMIC_COUNTER_BUFFER(GL_ATOMIC_COUNTER_BUFFER),
		DISPATCH_INDIRECT_BUFFER(GL_DISPATCH_INDIRECT_BUFFER),
		SHADER_STORAGE_BUFFER(GL_SHADER_STORAGE_BUFFER);
		
		private final int value;
		private Target(int value) {
			this.value = value;
		}
	}
	
	public static enum UsageHint {
		STATIC_DRAW(GL_STATIC_DRAW),
		STATIC_READ(GL_STATIC_READ),
		STATIC_COPY(GL_STATIC_COPY),
		DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
		DYNAMIC_READ(GL_DYNAMIC_READ),
		DYNAMIC_COPY(GL_DYNAMIC_COPY),
		STREAM_DRAW(GL_STREAM_DRAW),
		STREAM_READ(GL_STREAM_READ),
		STREAM_COPY(GL_STREAM_COPY);
		
		private final int value;
		private UsageHint(int value) {
			this.value = value;
		}
	}
	
	private Target target;
	private UsageHint usageHint;
	private int bufferObjectHandle;
	
	public BufferObject(Target target, UsageHint usageHint) {
		this.target = target;
		this.usageHint = usageHint;
		bufferObjectHandle = glGenBuffers();
	}
	
	public BufferObject(Target target, UsageHint usageHint, float[] data) {
		this(target, usageHint);
		try (BufferObjectResource r = this.use()){
			r.putData(data);
		}
	}
	
	public BufferObject(Target target, UsageHint usageHint, double[] data) {
		this(target, usageHint);
		try (BufferObjectResource r = this.use()){
			r.putData(data);
		}
	}
	
	public BufferObject(Target target, UsageHint usageHint, int[] data) {
		this(target, usageHint);
		try (BufferObjectResource r = this.use()){
			r.putData(data);
		}
	}
	
	public BufferObject(Target target, UsageHint usageHint, short[] data) {
		this(target, usageHint);
		try (BufferObjectResource r = this.use()){
			r.putData(data);
		}
	}
	
	public void bind() {
		glBindBuffer(target.value, bufferObjectHandle);
	}
	
	public void unbind() {
		glBindBuffer(target.value, 0);
	}
	
	public void destroy() {
		glDeleteBuffers(bufferObjectHandle);
	}
	
	public void putDataUnsafe(float[] data) {
		glBufferData(target.value, data, usageHint.value);
	}
	
	public void putDataUnsafe(double[] data) {
		glBufferData(target.value, data, usageHint.value);
	}
	
	public void putDataUnsafe(int[] data) {
		glBufferData(target.value, data, usageHint.value);
	}
	
	public void putDataUnsafe(short[] data) {
		glBufferData(target.value, data, usageHint.value);
	}
	
	public BufferObjectResource use() {
		return new BufferObjectResource(this);
	}
	
	public static class BufferObjectResource implements AutoCloseable {
		private BufferObject obj;
		
		private BufferObjectResource(BufferObject obj) {
			this.obj = obj;
			obj.bind();
		}
		
		public void putData(float[] data) {
			obj.putDataUnsafe(data);
		}
		
		public void putData(double[] data) {
			obj.putDataUnsafe(data);
		}
		
		public void putData(int[] data) {
			obj.putDataUnsafe(data);
		}
		
		public void putData(short[] data) {
			obj.putDataUnsafe(data);
		}
		
		@Override
		public void close() {
			obj.unbind();
		}
	}
}
