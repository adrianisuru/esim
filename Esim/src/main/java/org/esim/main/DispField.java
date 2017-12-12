package org.esim.main;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_2;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_3;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.opengl.GL11.glViewport;

import org.esim.electrostatics.Field;
import org.esim.engine.DisplayWindow;
import org.esim.engine.GLFWWindow;
import org.esim.engine.model.ChargeModel;
import org.esim.engine.model.Grid;
import org.esim.engine.model.ImmidiateModeChargeModel;
import org.esim.engine.model.Model;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class DispField extends DisplayWindow{
	
	
	private final Field field; 
	
	private Model grid;
	
	private Model fieldArrows;
	
	private Model charges;
	

	public DispField(Field field, GLFWWindow window) {
		super(window);
		this.field = field;
		
		grid = new Grid(10, 10, new Vector4f(1,1,1,1));
		charges = new ImmidiateModeChargeModel(this.field);
		

	}

	private void foc() {
		this.window.focus();
	}
	
	@Override
	public void draw() {
		
 	grid.draw();
	charges.draw();
//	fieldArrows.draw();
		
		
	}

	@Override
	public void update() {

		//do some stuff
		
		/*...........*/
		
		//update models
		
		//grid.update(); //only update grid upon resize 
		//charges.update(); //update charges upon movement
		//fieldArrows.update(); //only update field arrows upon resize
	}
	
	@Override
	public void cleanup() {

	}

}
