package engine;


import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements Activatable {

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
        glUseProgram(shaderProgram);
    }

    @Override
    public void deactivate() {
        glUseProgram(0);
    }
}
