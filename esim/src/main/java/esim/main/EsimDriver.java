package esim.main;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import esim.electrostatic.Charge;
import esim.engine.Timer;
import esim.engine.WindowLogicGL;

public class EsimDriver {
	
	public static final String TITLE_CONTROL = "Electric Field Simulator";
	
	public static final int DEFAULT_SCREEN_WIDTH = 800;
	public static final int DEFAULT_SCREEN_HEIGHT = 640;
	
	public static final int TARGET_FPS = 60;
	
	public static final int TARGET_UPS = 30;

	private static boolean running;
	
	private static GLFWErrorCallback errorCallback;
	private static WindowAdapter watchClose;
	
	static Timer timer;
	
	private static EsimControl control;
	private static EsimField field;
	private static Esim3D plot3d;

	static volatile Vector<Charge> charges;
	
	private static Thread t1, t2;
	
	public static void main(String[] args) {
		init();
		loop();
		cleanup();
		System.exit(0);
	}

	
	public static void init(){
	    // Setup an error callback. The default implementation
        // will print the error message in System.err.
		
	
		errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        watchClose = new WindowAdapter(){
        	public void windowClosing(WindowEvent we){
        		//add "are you sure?" dialog
        		running = false;
        	}
        };
        
        charges = new Vector<Charge>();
        charges.add(new Charge(0f, 0.3f, 1f)); //just adding some charges
		charges.add(new Charge(0f, -0.3f, -1f));
//		charges.add(new Charge(0.3f, 0f, 1f));
//		charges.add(new Charge(-0.3f, 0f, -1f));
//		charges.add(new Charge(0.3f, -0.3f, 1f));
//		charges.add(new Charge(-0.3f, 0.3f, -1f));
//		charges.add(new Charge(0.3f, 0.3f, 1f));
//		charges.add(new Charge(-0.3f, -0.3f, -1f));
        
        timer = new Timer();
		timer.init();
        
        control = new EsimControl(TITLE_CONTROL);
        control.addWindowListener(watchClose);
        
        field = new EsimField(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT, 50, 50);
        field.init();
        
		field.getWindow().setVisible(true);
		
		t1 = new Thread(new Runnable(){
			@Override
			public void run() {
					float elapsedTime;
					float accumulator = 0;
					float interval = 1f / TARGET_UPS;
					while(running){
						elapsedTime = timer.getElapsedTime();
						accumulator += elapsedTime;
						while(accumulator >= interval){
							field.update();
							accumulator -= interval;
						}

					}
			}
		}, "Field");
		
		
		plot3d = new Esim3D(DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT, 400);
		plot3d.init();
        plot3d.getWindow().setVisible(true);
        
        t2 = new Thread(new Runnable(){
			@Override
			public void run() {
					float elapsedTime;
					float accumulator = 0;
					float interval = 1f / TARGET_UPS;
					while(running){
						elapsedTime = timer.getElapsedTime();
						accumulator += elapsedTime;
						while(accumulator >= interval){
							plot3d.update();
							accumulator -= interval;
						}
					}
			}
		}, "plot3d");
        
        running = true;
	}
	
	private static void loop() {
		float elapsedTime;
		float accumulator = 0;
		float interval = 1f / TARGET_UPS;
		
		t1.start();
		t2.start();
		
		while(running){
			elapsedTime = timer.getElapsedTime();
			accumulator += elapsedTime;
			
			//do any drawing to all windows
			field.getWindow().focus();
			field.draw();
			plot3d.getWindow().focus();
			plot3d.draw();
			
			
			field.getWindow().swapBuffers();
			plot3d.getWindow().swapBuffers();
			///////////////////////////////
			
			glfwPollEvents(); //poll events
		}
	}
	
	private static void cleanup() {
		System.out.println("Cleaning Up!");
		//actually write this
		plot3d.dispose();
	}
	
	public static Vector3f getColor(float val, float min, float max, boolean strict){
		strict = false; //REMOVE THIS**********************************************************************
		Vector3f color = new Vector3f();
		//float[][] colors = {{0,0,1}, {1,0,1}, {0,1,1}, {1,1,1}, {0,1,0}, {1,1,0}, {1,0,0}};
		float[][] colors = {{0,0,1}, {0,1,0}, {1,1,0}, {1,0,0}};
		val = (val - min)/(max - min);
		int idx1;
		int idx2;
		float frac = 0;
		if(val <= 0) idx1 = idx2 = 0;
		else if(val >= 1) idx1 = idx2 = colors.length-1;
		else{
			val = val*(colors.length-1);
			idx1 = (int) val;
			idx2 = idx1+1;
			frac = val - idx1;
		}
		if(strict){
			color.x = (frac < 0.5f)? colors[idx1][0] : colors[idx2][0];
			color.y = (frac < 0.5f)? colors[idx1][1] : colors[idx2][1];
			color.z = (frac < 0.5f)? colors[idx1][2] : colors[idx2][2];
		} else{
			color.x = (colors[idx2][0] - colors[idx1][0])*frac + colors[idx1][0];
			color.y = (colors[idx2][1] - colors[idx1][1])*frac + colors[idx1][1];
			color.z = (colors[idx2][2] - colors[idx1][2])*frac + colors[idx1][2];
		}
		return color;
		
	}
	
	static void setPlacing(boolean placing){
		field.placing = placing;
	}

}
