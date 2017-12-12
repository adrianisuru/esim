package org.esim.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;

public abstract class DisplayWindow {

	public final GLFWWindow window;
	
	GLFWWindowSizeCallback sizeCallback;
	
	public DisplayWindow(GLFWWindow window) {
		this.window = window;
	}
	
	
	protected final void predraw() {
		window.focus();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public final void setWindowSizeCallback(GLFWWindowSizeCallback cbfun) {
		sizeCallback = cbfun;
		GLFW.glfwSetWindowSizeCallback(window.handle, cbfun);
	}
	
	protected abstract void draw();
	
	protected final void swapBuffers() {
		window.swapBuffers();
	}
	
	public final void drawCycle() {
		predraw();
		draw();
		swapBuffers();
	}
	
	public abstract void update();
	
	public abstract void cleanup();
	
}
