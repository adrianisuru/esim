package org.esim.main;

import org.esim.electrostatics.Field;
import org.esim.engine.DisplayWindow;
import org.esim.engine.GLFWWindow;
import org.esim.engine.model.Model;
import org.lwjgl.opengl.GL11;

public class Disp3D extends DisplayWindow{
	
	Model graph;
	float[] vertices;
	
	Field field;
	
	int res;
	

	public Disp3D(Field field, GLFWWindow window, int res) {
		super(window);
		
		this.field = field;
		this.res = res;
		
		vertices = new float[res*res];
		
		
		//initialize graph
		//graph = new ...
		
	}

	@Override
	public void draw() {
		//draw graph
		//graph.draw();
		
		//...
		//maybe draw some other stuff
	}

	@Override
	public void update() {
		
		//update graph
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
