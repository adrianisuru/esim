package org.esim.engine;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

public abstract class DisplayWindow {

	GLFWWindow window;
	
	GLFWWindowSizeCallback sizeCallback;
	
	public DisplayWindow(GLFWWindow window) {
		this.window = window;
	}
	
	
	public void predraw() {
		window.focus();
	}
	
	public abstract void draw();
	
	public abstract void update();
	
	
}
