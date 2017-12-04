package org.esim.main;

import org.esim.electrostatics.Field;
import org.esim.engine.DisplayWindow;
import org.esim.engine.GLFWWindow;
import org.esim.engine.model.Grid;
import org.esim.engine.model.Model;
import org.joml.Vector4f;
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
		
		
	}

	@Override
	public void draw() {
		
 	grid.draw();
//	charges.draw();
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
