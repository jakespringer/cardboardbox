package opengl;

import engine.Activatable;
import java.util.HashMap;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements Activatable {

    private static int activeProgram;

    private final int shaderProgram;

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

    @Override
    public void activate() {
        if (activeProgram != shaderProgram) {
            glUseProgram(shaderProgram);
            activeProgram = shaderProgram;
        }
    }

    @Override
    public void deactivate() {
        if (activeProgram != 0) {
            glUseProgram(0);
            activeProgram = 0;
        }
    }

    private final HashMap<String, Integer> uniformLocations = new HashMap();

    private int getUniformLocation(String name) {
        if (!uniformLocations.containsKey(name)) {
            uniformLocations.put(name, glGetUniformLocation(shaderProgram, name));
        }
        return uniformLocations.get(name);
    }

    public void setUniform(String name, int value) {
        activate();
        int uniform = getUniformLocation(name);
        glUniform1i(uniform, value);
    }

    public void setUniform(String name, Vector3f value) {
        activate();
        int uniform = getUniformLocation(name);
        glUniform3fv(uniform, new float[]{value.x, value.y, value.z});
    }

    public void setUniform(String name, Vector3i value) {
        activate();
        int uniform = getUniformLocation(name);
        glUniform3fv(uniform, new float[]{value.x, value.y, value.z});
    }

    public void setUniform(String name, Matrix4f mat) {
        activate();
        int uniform = getUniformLocation(name);
        glUniformMatrix4fv(uniform, false, new float[]{
            mat.m00(), mat.m01(), mat.m02(), mat.m03(),
            mat.m10(), mat.m11(), mat.m12(), mat.m13(),
            mat.m20(), mat.m21(), mat.m22(), mat.m23(),
            mat.m30(), mat.m31(), mat.m32(), mat.m33()});
    }
}
