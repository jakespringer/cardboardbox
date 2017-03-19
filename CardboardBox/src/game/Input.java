package game;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
	private static HashMap<String, Integer> keybindings = new HashMap<>();
	private static boolean[] keys = new boolean[65536];
	
	static {
		keybindings.put("exit", GLFW_KEY_ESCAPE);
		keybindings.put("move_forward", GLFW_KEY_W);
		keybindings.put("move_left", GLFW_KEY_A);
		keybindings.put("move_backward", GLFW_KEY_S);
		keybindings.put("move_right", GLFW_KEY_D);
	}
	
	static void handleInput(long window, int key, int scancode, int action, int mods) {
		keys[key] = action != GLFW_RELEASE;
	}
	
	public static boolean isPressed(String binding) {
		return keys[keybindings.get(binding)];
	}
}
