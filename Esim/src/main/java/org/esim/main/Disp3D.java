package org.esim.main;

import org.esim.electrostatics.Field;
import org.esim.engine.DisplayWindow;
import org.esim.engine.GLFWWindow;
import org.esim.engine.model.Graph;
import org.esim.engine.model.Model;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Disp3D extends DisplayWindow{
	
	public static int DEFAULT_RES = 100;
	
	Model graph;
	
	Field field;
	
	int res;
	

	public Disp3D(Field field, GLFWWindow window, int res) {
		super(window);
		
		this.field = field;
		this.res = res;
		
		graph = new Graph(field, res);
		
	
		
	}

	@Override
	public void draw() {
		//draw graph
		  graph.draw();
		
		//...
		//maybe draw some other stuff
	}

	@Override
	public void update() {
		
		if(GLFW.glfwGetWindowAttrib(window.handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_FALSE) {
			graph.update();
		}
		
	}

	@Override
	public void cleanup() {
		graph.cleanup();
	}

}
