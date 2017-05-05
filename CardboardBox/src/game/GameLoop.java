package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import chunk.Chunk;
import chunk.ChunkRenderer;
import opengl.BufferObject;
import opengl.BufferObject.BufferObjectResource;
import opengl.BufferObject.Target;
import opengl.BufferObject.UsageHint;
import opengl.ShaderProgram;
import opengl.ShaderProgram.ShaderProgramResource;
import opengl.TextureObject;
import opengl.TextureObject.TextureObjectResource;
import opengl.VertexArrayObject;
import opengl.VertexArrayObject.VertexArrayObjectResource;
import util.Resources;

public class GameLoop {
	private static long currentNanoTime;
	private static long previousNanoTime;
	private static long deltaNanoTime;
	private static double dt;
	
	private static double[] xBuffer = new double[1], yBuffer = new double[1];
	private static double xCursorPrevious;
	private static double yCursorPrevious;
	
	public static void run() {
		// for debugging
				
		glfwGetCursorPos(Window.getWindowHandle(), xBuffer, yBuffer);
		xCursorPrevious = xBuffer[0];
		yCursorPrevious = yBuffer[0];
		
		VertexArrayObject vao = new VertexArrayObject();
		BufferObject vbo = new BufferObject(Target.ARRAY_BUFFER, UsageHint.STATIC_DRAW, new float[] {
				-1.0f,-1.0f,-1.0f,
			    -1.0f,-1.0f, 1.0f,
			    -1.0f, 1.0f, 1.0f,
			    1.0f, 1.0f,-1.0f,
			    -1.0f,-1.0f,-1.0f,
			    -1.0f, 1.0f,-1.0f,
			    1.0f,-1.0f, 1.0f,
			    -1.0f,-1.0f,-1.0f,
			    1.0f,-1.0f,-1.0f,
			    1.0f, 1.0f,-1.0f,
			    1.0f,-1.0f,-1.0f,
			    -1.0f,-1.0f,-1.0f,
			    -1.0f,-1.0f,-1.0f,
			    -1.0f, 1.0f, 1.0f,
			    -1.0f, 1.0f,-1.0f,
			    1.0f,-1.0f, 1.0f,
			    -1.0f,-1.0f, 1.0f,
			    -1.0f,-1.0f,-1.0f,
			    -1.0f, 1.0f, 1.0f,
			    -1.0f,-1.0f, 1.0f,
			    1.0f,-1.0f, 1.0f,
			    1.0f, 1.0f, 1.0f,
			    1.0f,-1.0f,-1.0f,
			    1.0f, 1.0f,-1.0f,
			    1.0f,-1.0f,-1.0f,
			    1.0f, 1.0f, 1.0f,
			    1.0f,-1.0f, 1.0f,
			    1.0f, 1.0f, 1.0f,
			    1.0f, 1.0f,-1.0f,
			    -1.0f, 1.0f,-1.0f,
			    1.0f, 1.0f, 1.0f,
			    -1.0f, 1.0f,-1.0f,
			    -1.0f, 1.0f, 1.0f,
			    1.0f, 1.0f, 1.0f,
			    -1.0f, 1.0f, 1.0f,
			    1.0f,-1.0f, 1.0f
		});
		BufferObject ebo = new BufferObject(Target.ELEMENT_ARRAY_BUFFER, UsageHint.STATIC_DRAW, new int[] {
				1, 2, 3,
				1, 2, 3,
		});
		TextureObject to = new TextureObject(TextureObject.Target.TEXTURE_2D, new float[] {
				1.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f
		}, 1, 2);
		ShaderProgram program = new ShaderProgram(Resources.get("glsl/simple.vert"), Resources.get("glsl/simple.frag"));
		
		try (VertexArrayObjectResource vaor = vao.use();
			 BufferObjectResource bor = vbo.use()) {
	    	glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
	        glEnableVertexAttribArray(0);
		}
		
		int[] chunkColors = new int[287496];
		for (int i=0; i<chunkColors.length; ++i) {
			double rand = Math.random();
			double threshold = 0.995;
			if (rand > threshold) {
//				chunkColors[i] = (int) ((rand - threshold) * (1.0 / (1.0 - threshold)) * (0x00FFFFFF));
				int split = 287496 / 3;
				if (i < split*1) chunkColors[i] = 0x00FF0000;
				else if (i < split*2) chunkColors[i] = 0x000000FF;
				else chunkColors[i] = 0x0000FF00;
			} else {
				chunkColors[i] = 0;
			}
		}
		
		Chunk chunk = new Chunk(chunkColors);
		ChunkRenderer chunkRenderer = new ChunkRenderer(chunk);
		
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);
		
		previousNanoTime = System.nanoTime();
		long handle = Window.getWindowHandle();
		while (!glfwWindowShouldClose(handle)) {
			currentNanoTime = System.nanoTime();
			deltaNanoTime = currentNanoTime - previousNanoTime;
			dt = (double) deltaNanoTime / 1e9;
			previousNanoTime = currentNanoTime;
			Window.updateFps(1 / dt);
			
            glfwPollEvents();
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
            
            updateCameraPosition();
            
//            try (VertexArrayObjectResource vaor = vao.use();
//            	 ShaderProgramResource spr = program.use();
//                 BufferObjectResource bor = ebo.use();
//                 TextureObjectResource tor = to.use()) {
//            	
//            	spr.setUniform("projectionMatrix", Camera.getProjectionMatrix(1.f, Window.getWidth(), Window.getHeight(), 0.1f, 100.f));
//            	spr.setUniform("modelViewMatrix", Scene.getCamera().getModelViewMatrix(new Vector3f(1, 1, -5)));
//            	
//            	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
////            	glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
//            	glDrawArrays(GL_TRIANGLES, 0, 35*3);
//            }
            
            chunkRenderer.render();

            glfwSwapBuffers(handle);
        }
		
		vbo.destroy();
		ebo.destroy();
		vao.destroy();
		chunkRenderer.destroy();
		
		Window.cleanup();
	}
	
	public static void _run() {
		previousNanoTime = System.nanoTime();
		long handle = Window.getWindowHandle();
		while (!glfwWindowShouldClose(handle)) {
			currentNanoTime = System.nanoTime();
			deltaNanoTime = currentNanoTime - previousNanoTime;
			dt = (double) deltaNanoTime / 1e9;
			previousNanoTime = currentNanoTime;
			Window.updateFps(1 / dt);
			
            glfwPollEvents();
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 

            glfwSwapBuffers(handle);
        }
		
		Window.cleanup();
	}
	
	public static void updateCameraPosition() {
		Camera camera = Scene.getCamera();
		if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_ESCAPE) != 0) {
            glfwSetWindowShouldClose(Window.getWindowHandle(), true);
        }
        float cameraSpeed = (float) (5.0 * dt);
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_W) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
//            horizontalForward.y = 0;
            camera.position.add(horizontalForward);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_S) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(-cameraSpeed, new Vector3f());
//            horizontalForward.y = 0;
            camera.position.add(horizontalForward);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_A) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
            horizontalForward.y = 0;
            horizontalForward.rotateAbout((float) Math.PI / 2, 0, 1, 0);
            camera.position.add(horizontalForward);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_D) != 0) {
            Vector3fc forward = camera.facing();
            Vector3f horizontalForward = forward.mul(cameraSpeed, new Vector3f());
            horizontalForward.y = 0;
            horizontalForward.rotateAbout((float) -Math.PI / 2, 0, 1, 0);
            camera.position.add(horizontalForward);
        }
        
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_SPACE) != 0) {
            camera.position.add(0, -cameraSpeed, 0);
        }
        if (glfwGetKey(Window.getWindowHandle(), GLFW_KEY_LEFT_SHIFT) != 0) {
            camera.position.add(0, cameraSpeed, 0);
        }
        
        glfwGetCursorPos(Window.getWindowHandle(), xBuffer, yBuffer);
        
        double dx = xBuffer[0] - xCursorPrevious;
        double dy = yBuffer[0] - yCursorPrevious;
		xCursorPrevious = xBuffer[0];
		yCursorPrevious = yBuffer[0];

		camera.horAngle += dx / 500;
        camera.vertAngle += dy / 500;
        
        if (camera.vertAngle > 1.5) {
            camera.vertAngle = 1.5f;
        }
        
        if (camera.vertAngle < -1.5) {
            camera.vertAngle = -1.5f;
        }
	}
}
