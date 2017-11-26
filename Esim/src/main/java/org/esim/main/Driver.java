package org.esim.main;

import org.esim.electrostatics.Charge;
import org.esim.electrostatics.Field;
import org.esim.engine.DisplayWindow;
import org.esim.engine.GLFWWindow;
import org.esim.engine.LoopTimer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class Driver {
	
	
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	private static final long NS_PER_TICK = 500000000l;
	
	private static DisplayWindow[] displays;
	
	private static Field field;
	
	private static LoopTimer timer;

	public static void main(String[] args) {
		
		//handle args
		
		//initialize everything
		init();
		
		//loop
		loop();
		
		//cleanup
		cleanup();
	}
	
	
	private static void init() {
		
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		field = new Field(new Charge(0.0f, 0.3f, 1.0f), new Charge(0.0f, -0.3f, -1.0f));
		
		displays = new DisplayWindow[2];
		displays[0] = new DispField(field, new GLFWWindow(WIDTH, HEIGHT, "2d", 0, 0));
		displays[1] = new Disp3D(field, new GLFWWindow(WIDTH, HEIGHT, "3d", 0, 0));
		
		timer = new LoopTimer();
		
		
	}
	
	private static void loop() {
		while (true) {
			if(timer.getTime() >= NS_PER_TICK) {
				update();
			
			}
			
			draw();
			
			GLFW.glfwPollEvents();
		}
	}
	
	private static void draw() {
		
		
		
		for(DisplayWindow display : displays) {
			display.drawCycle();
		}
		
		
		
	}
	
	private static void update() {
		for(DisplayWindow display : displays) {
			display.update();
		}
	}
	
	private static void cleanup() {
		for(DisplayWindow display : displays) {
			display.cleanup();
		}
		GLFW.glfwTerminate();
	}
	
	
}
