package esim.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import esim.engine.Window;
import esim.engine.WindowLogicGL;
import esim.electrostatic.Charge;
import static esim.main.EsimDriver.charges;

public class EsimField implements WindowLogicGL{

	private static final String TITLE = "E-field";
	
	private Window window;
	private int width, height;
	
	private int gridWidth, gridHeight;
	
	private boolean follow;
	private float cursorx, cursory;
	private GLFWMouseButtonCallback buttonCallback;
	private GLFWCursorPosCallback cursorCallback;
	private GLFWWindowSizeCallback windowCallback;
	
	boolean showFieldArrows;
	boolean placing;
	//private boolean showEquipotential = true;
	
	private Vector2f[] field_pos;
	private Vector2f[] field_val;
	private Vector3f[] field_color;
	
	private int cci; //current charge index
	private Vector2f holdPos;
	private boolean hold;
	
	private int res;
	private float err;
	
	private float maxcolor;
	private float mincolor;
	
	public EsimField(int width, int height, int gridWidth, int gridHeight){
		follow = false;
		placing = false;
		this.width = width;
		this.height = height;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		window = new Window(TITLE, width, height, false);
	}
	
	@Override
	public void init(){
		res = 4000;
		err = 100f;
		//resetEq();
		
		maxcolor = Charge.K*10;
		mincolor = -maxcolor;
		
		updateGridSize(gridWidth, gridHeight);
		window.init();
		
		windowCallback = new GLFWWindowSizeCallback(){
			@Override
			public void invoke(long windowHandle, int w, int h){
				if(!window.isInitialized()) return;
				window.focus();
				glViewport(0, 0, w, h);
				width = w;
				height = h;
			}
		};
		
		cursorCallback = new GLFWCursorPosCallback() {
		    @Override
		    public void invoke(long windowHandle, double xpos, double ypos) {
		    	if(window.getWindowHandle() == windowHandle){
		    		cursorx = (float) (xpos*2/width -1);
		    		cursory = (float) (-ypos*2/height +1);
		    	}
		    }
		};
		
		buttonCallback = new GLFWMouseButtonCallback() {
		    @Override
		    public void invoke(long windowHandle, int button, int action, int mods) {
		    	if(window.getWindowHandle() == windowHandle){
		    		Vector2f mpos = new Vector2f(cursorx, cursory);
		    		if(button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS){
		    			System.out.println(Charge.ePotential(charges, mpos));
		    			double average = (width + height)/(double)2;
		    			if(placing){
		    				charges.add(new Charge(mpos, 1));
		    			} else{
		    				for(int i = 0; i < charges.size(); i++){
			    				if(mpos.distance(charges.get(i).pos) < chargeSize/average){
			    					cci = i;
			    					holdPos = charges.get(cci).pos;
			    					follow = true;
			    				}
			    			}
		    			}
		    		} else if(button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE){
		    			follow = false;
		    		} else if(button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS){
		    			if(placing){
		    				charges.add(new Charge(mpos, -1));
		    			}
		    		} else if(button == GLFW_MOUSE_BUTTON_3 && action == GLFW_PRESS){
		    			double average = (width + height)/(double)2;
		    			if(placing){
		    				for(int i = 0; i < charges.size(); i++){
			    				if(mpos.distance(charges.get(i).pos) < chargeSize/average){
			    					charges.remove(i);
			    					cci = 0;
			    				}
			    			}
		    			}
		    		}
		    	}
		    }
		};
		
		glfwSetWindowSizeCallback(window.getWindowHandle(), windowCallback);
		glfwSetCursorPosCallback(window.getWindowHandle(), cursorCallback);
		glfwSetMouseButtonCallback(window.getWindowHandle(), buttonCallback);
		
		
	}
	
	@Override
	public void draw() {
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		
		
		glColor4f(1f, 1f, 1f, 0.5f);
		draw2DGrid();
		
		drawFieldArrows();
		
		drawCharges();
		
		//if(showEquipotential) drawEquipotentials();
		
	}
	
	float chargeSize = 10;
	private void drawCharges(){
		glPointSize(chargeSize); //draw charges
		glBegin(GL_POINTS);
		{
			for(Charge c : charges){
				if(c.value < 0) glColor3f(0, 0, 1);
				else if(c.value > 0) glColor3f(1, 0, 0);
				else continue;
				glVertex3f(c.pos.x, c.pos.y, 0);
			}
		} glEnd();
	}

	private void drawFieldArrows(){
		
		glPointSize(3f);
		glBegin(GL_POINTS);
		{
			for(int i = 0; i <= gridWidth; i++){
				for(int j = 0; j <= gridHeight; j++){
					
					glVertex3f(field_val[i*gridHeight+j].x, field_val[i*gridHeight+j].y, 0);
				}
			}
		} glEnd();
		glBegin(GL_LINES);
		{
			for(int i = 0; i <= gridWidth; i++){
				for(int j = 0; j <= gridHeight; j++){
					glColor4f(field_color[i*gridHeight+j].x, field_color[i*gridHeight+j].y, field_color[i*gridHeight+j].z, 1);
					glVertex3f(field_val[i*gridHeight+j].x, field_val[i*gridHeight+j].y, 0);
					glVertex3f(field_pos[i*gridHeight+j].x, field_pos[i*gridHeight+j].y, 0);
				}
			}
		} glEnd();
	}
	


	private void draw2DGrid(){
		float w = gridWidth/2.0f;
		float h = gridHeight/2.0f;
		glBegin(GL_LINES);
		for(float i = -w; i <= w; i++){
			glVertex3f(i/w, -1, 0);
			glVertex3f(i/w, 1, 0);
		}
		for(float j = -h; j <= h; j++){
			glVertex3f(-1, j/h, 0);
			glVertex3f(1, j/h, 0);
		}
		glEnd();
	}
	
	@Override
	public void update(){
		if(follow){
			updateCharges();
		} updateField();
	}
	
	private void updateCharges(){
		charges.get(cci).pos = new Vector2f(cursorx, cursory);
		if(Math.abs(cursorx) > 1 || Math.abs(cursory) > 1){
			follow = false;
			charges.get(cci).pos = holdPos;
		}
	}
	
	private void updateField(){
		Vector2f hold;
		float magnitude = 0;
		float u = 2.0f/(gridWidth+gridHeight);
		float colorval;
		int index;
		for(int i = 0; i <= gridWidth; i++){
			for(int j = 0; j <= gridHeight; j++){
				index = i*gridHeight+j;
				synchronized(charges){
					hold = Charge.eField(charges, field_pos[index]);
					magnitude = hold.length();
					colorval = Charge.ePotential(charges, field_pos[index]);
				}
				field_color[index] = EsimDriver.getColor(colorval, mincolor, maxcolor, true);
				field_val[index].x = (float) (field_pos[index].x + u*hold.x/magnitude);
				field_val[index].y = (float) (field_pos[index].y + u*hold.y/magnitude);
			}
		}
	}
	
	public void updateGridSize(int gridWidth, int gridHeight){
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		
		int length = (gridWidth+1)*(gridHeight+1);
		field_pos = new Vector2f[length];
		field_val = new Vector2f[length];
		field_color = new Vector3f[length];
		
		for(int i = 0; i < field_pos.length; i++){
			field_pos[i] = new Vector2f(0);
		}
		for(int i = 0; i < field_val.length; i++){
			field_val[i] = new Vector2f(0);
		}
		for(int i = 0; i < field_val.length; i++){
			field_color[i] = new Vector3f(0);
		}
		
		float w = gridWidth/2.0f;
		float h = gridHeight/2.0f;
		int i, j;
		int index = 0;
		i = 0;
		for(float x = -w; x <= w; x++){
			j = 0;
			for(float y = -h; y <= h; y++){
				index = i*gridHeight+j;
				//System.out.println("[" + (index) + "(" + i + "," + j + ")" + "]" + "[" + (x/w) + "," + (y/h) + "]");
				field_pos[index].x = x/w;
				field_pos[index].y = y/h;
				//System.out.println(field_pos[i*gridHeight+j].toString());
				j++;
			}
			i++;
		}
	}
	
	
	@Override
	public Window getWindow(){
		return window;
	}
	

	
	public void dispose(){
		
	}
}
