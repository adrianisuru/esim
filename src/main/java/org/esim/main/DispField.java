package org.esim.main;

import org.esim.electrostatics.Field;
import org.esim.engine.DisplayWindow;
import org.esim.engine.GLFWWindow;
import org.esim.engine.model.Grid;
import org.esim.engine.model.ImmidiateModeArrowModel;
import org.esim.engine.model.ImmidiateModeChargeModel;
import org.esim.engine.model.Model;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;



public class DispField extends DisplayWindow{
	
	
	private final Field field; 
	
	private Model grid;
	
	private Model fieldArrows;
	
	private Model charges;
	
	private int width, height;
	
	public boolean edit_charges;
	
	/**
	 * Whether or not the current charge should follow the mouse pointer.
	 * <p>(used for moving charges)
	 */
	public boolean following;

	public DispField(Field field, GLFWWindow window, int width, int height) {
		super(window);
		this.field = field;
		this.width = width;
		this.height = height;
		
		grid = new Grid(width, height, new Vector4f(1,0,1,1));
		charges = new ImmidiateModeChargeModel(this.field);
		fieldArrows = new ImmidiateModeArrowModel(this.field, this.width, this.height, new Vector4f(1,1,1,1));
		GLFW.glfwSetMouseButtonCallback(window.handle, new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				mouseInvoke(window, button, action, mods);
			}
		});
	}
	

	private void mouseInvoke(long window, int button, int action, int mods) {
		if(window != this.window.handle) return;
	}

	private void foc() {
		this.window.focus();
	}
	
	
	@Override
	public void draw() {
		
 	grid.draw();
	charges.draw();
	fieldArrows.draw();
		
		
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
		grid.cleanup();
		charges.cleanup();
		fieldArrows.cleanup();
	}

}
