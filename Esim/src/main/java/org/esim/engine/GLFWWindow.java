package org.esim.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

/**
 * Class representing a GLFW Window.
 * @author adrianisuru
 *
 */
public class GLFWWindow {
	
	/**
	 * This window's glfw handle. 
	 * Usage similar to a pointer but it doesnt actually describe a memory location.
	 */
	final long handle;
	
	/**
	 * The opengl capabilities of this window.
	 */
	private final GLCapabilities capabilities;
	
	/**
	 * Creates a new window using GLFW.glfwCreateWindow().
	 * @see org.lwjgl.glfw.GLFW#glfwCreateWindow(int, int, java.lang.CharSequence, long, long)
	 */
	public GLFWWindow(int width,
            int height,
            java.lang.CharSequence title,
            long monitor,
            long share) {
		//For now //TODO make this better
		handle = GLFW.glfwCreateWindow(width, height, title, monitor, share);
		
		capabilities = GL.createCapabilities();
		
	}
	
	/**
	 * Prepares this window for drawing.
	 * Call this method before drawing on this window.
	 */
	public void focus() {
		GL.setCapabilities(capabilities);
		GLFW.glfwMakeContextCurrent(handle);
	}
	
	
}
