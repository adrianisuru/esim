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
	
	Graph graph;
	
	Field field;
	
	public int res;
	

	public Disp3D(Field field, GLFWWindow window, int res) {
		super(window);
		
		this.field = field;
		this.res = res;
		
		graph = new Graph(field, res);
		
		
		
	}
	
	@Override
	public void updateSize(int w, int h){
		super.updateSize(w, h);
		
	}

	@Override
	public void draw() {
		//draw graph
		 
		
		//...
		//maybe draw some other stuff
	}

	@Override
	public void update() {
		
		if(GLFW.glfwGetWindowAttrib(window.handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_FALSE) {
			
		}
		
	}

	@Override
	public void cleanup() {

	}

}
