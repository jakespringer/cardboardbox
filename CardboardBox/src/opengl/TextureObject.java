package opengl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_1D_ARRAY;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_RECTANGLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY;
import static org.lwjgl.opengl.GL40.GL_TEXTURE_CUBE_MAP_ARRAY;

public class TextureObject {
	public static enum Target {
		TEXTURE_1D(GL_TEXTURE_1D), 
		TEXTURE_1D_ARRAY(GL_TEXTURE_1D_ARRAY), 
		TEXTURE_2D(GL_TEXTURE_2D), 
		TEXTURE_2D_ARRAY(GL_TEXTURE_2D_ARRAY), 
		TEXTURE_2D_MULTISAMPLE(GL_TEXTURE_2D_MULTISAMPLE), 
		TEXTURE_2D_MULTISAMPLE_ARRAY(GL_TEXTURE_2D_MULTISAMPLE_ARRAY), 
		TEXTURE_3D(GL_TEXTURE_3D), 
		TEXTURE_CUBE_MAP(GL_TEXTURE_CUBE_MAP), 
		TEXTURE_CUBE_MAP_ARRAY(GL_TEXTURE_CUBE_MAP_ARRAY), 
		TEXTURE_RECTANGLE(GL_TEXTURE_RECTANGLE);
		
		private final int value;
		private Target(int value) {
			this.value = value;
		}
	}
	
	private int textureObjectHandle;
	private Target target;
	
	public TextureObject(Target target) {
		this.textureObjectHandle = glGenTextures();
		this.target = target;
		try (TextureObjectResource r = use()) {
			glTexParameteri(target.value, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	        glTexParameteri(target.value, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		}
	}
	
	public TextureObject(Target target, float[] colors, int width, int height) {
		this(target);
		try (TextureObjectResource r = use()) {
			r.putColors(colors, width, height);
		}
	}
	
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
        glBindTexture(target.value, textureObjectHandle);
	}
	
	public void unbind() {
		glBindTexture(target.value, 0);
	}
	
	public void putColorsUnsafe(float[] colors, int width, int height) {
		if (!target.equals(Target.TEXTURE_2D)) {
			throw new UnsupportedOperationException("Only a target of TEXTURE_2D is supported currently");
		}
		
		if (colors.length % 3 != 0) {
			throw new IllegalArgumentException("Length of the color array must be divisible by 3");
		}
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_FLOAT, colors);
	}
	
	public TextureObjectResource use() {
		return new TextureObjectResource(this);
	}
	
	public static class TextureObjectResource implements AutoCloseable {
		private TextureObject obj;
		
		private TextureObjectResource(TextureObject obj) {
			this.obj = obj;
			obj.bind();
		}
		
		public void putColors(float[] colors, int width, int height) {
			obj.putColorsUnsafe(colors, width, height);
		}
		
		@Override
		public void close() {
			obj.unbind();
		}
	}
}
