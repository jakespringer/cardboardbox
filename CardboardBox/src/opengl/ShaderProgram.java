package opengl;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class ShaderProgram {
	private int shaderProgram;
	private HashMap<String, Integer> uniformLocations = new HashMap<>();
	
	/**
     * Constructs a shader program with both a vertex shader and a fragment
     * shader.
     *
     * @param vertexShaderSource The source code of the vertex shader.
     * @param fragmentShaderSource The source code of the fragment shader.
     */
    public ShaderProgram(String vertexShaderSource, String fragmentShaderSource) {
        // Create vertex shader
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        // TODO: Check if it compiles correctly

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        // TODO: Check if it compiles correctly

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        // TODO: Check for linking errors

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }
    
    public void bind() {
    	glUseProgram(shaderProgram);
    }
    
    public void unbind() {
    	glUseProgram(0);
    }
    
    private int getUniformLocation(String name) {
        if (!uniformLocations.containsKey(name)) {
            uniformLocations.put(name, glGetUniformLocation(shaderProgram, name));
        }
        return uniformLocations.get(name);
    }

    public void setUniformUnsafe(String name, int value) {
        int uniform = getUniformLocation(name);
        glUniform1i(uniform, value);
    }

    public void setUniformUnsafe(String name, Vector3f value) {
        int uniform = getUniformLocation(name);
        glUniform3fv(uniform, new float[]{value.x, value.y, value.z});
    }

    public void setUniformUnsafe(String name, Vector3i value) {
        int uniform = getUniformLocation(name);
        glUniform3fv(uniform, new float[]{value.x, value.y, value.z});
    }

    public void setUniformUnsafe(String name, Matrix4f mat) {
        int uniform = getUniformLocation(name);
        glUniformMatrix4fv(uniform, false, new float[]{
            mat.m00(), mat.m01(), mat.m02(), mat.m03(),
            mat.m10(), mat.m11(), mat.m12(), mat.m13(),
            mat.m20(), mat.m21(), mat.m22(), mat.m23(),
            mat.m30(), mat.m31(), mat.m32(), mat.m33()});
    }
    
    public ShaderProgramResource use() {
    	return new ShaderProgramResource(this);
    }
    
    public static class ShaderProgramResource implements AutoCloseable {
    	private ShaderProgram prg;
    	
    	private ShaderProgramResource(ShaderProgram prg) {
    		this.prg = prg;
    		prg.bind();
    	}
    	
        public void setUniform(String name, int value) {
            prg.setUniformUnsafe(name, value);
        }

        public void setUniform(String name, Vector3f value) {
        	prg.setUniformUnsafe(name, value);
        }

        public void setUniform(String name, Vector3i value) {
        	prg.setUniformUnsafe(name, value);
        }

        public void setUniform(String name, Matrix4f mat) {
        	prg.setUniformUnsafe(name, mat);
        }
    	
		@Override
		public void close() {
			prg.unbind();
		}    	
    }
}
