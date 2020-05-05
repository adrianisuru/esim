package esim.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import esim.electrostatic.Charge;
import esim.engine.ShaderProgram;
import esim.engine.Window;
import esim.engine.WindowLogicGL;
import static esim.main.EsimDriver.charges;

public class Esim3D implements WindowLogicGL{

	private static final String TITLE = "3D Graph";
	
	private static final String VERTEX_SHADER = 
			"#version 330 core\n" +
			"uniform mat4 pm;\n" +
			"uniform mat4 wm;\n" +
			"layout (location=0) in vec3 in_Position;\n" +
			"layout (location=1) in vec3 in_Color;\n" +
			"out vec4 pass_Color;\n" +
			"void main()\n" +
			"{\n" +
			    "gl_Position = pm * wm * vec4(in_Position, 1.0);\n" +
			    "pass_Color = vec4(in_Color, 1.0);\n" +
			"}";
	
	private static final String FRAGMENT_SHADER = 
			"#version 330 core\n" +
			"in vec4 pass_Color;\n" +
			"out vec4 out_Color;\n" +
			"void main()\n" +
			"{\n" +
				"out_Color = pass_Color;\n" +
			"}";
	
	private Window window;
	private int width, height;
	
	//private ShaderProgram shaderProgram;
	FloatBuffer verticesBuffer;
	FloatBuffer colorsBuffer;
	ByteBuffer indicesBuffer;
	int vaoId; 
	int vboId_pos;
	int vboId_color;
	int vboId_indices;
	int vsId;
	int fsId;
	int pId;
	Matrix4f projMatrix;
	Matrix4f worldMatrix;
	
	private final Map<String, Integer> uniforms;
	
	private GLFWWindowSizeCallback windowCallback;
	private GLFWFramebufferSizeCallback bufferCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWMouseButtonCallback buttonCallback;
	private GLFWCursorPosCallback posCallback;
	
	float beta;
	float alpha;
	float gamma;
	float alpha_temp;
	float beta_temp;
	float gamma_temp;
	boolean tilting;
	
	float xtrans;
	float ytrans;
	float ztrans;
	float xtrans_temp;
	float ytrans_temp;
	float ztrans_temp;
	boolean panning;
	
	float zoom;
	
	
	private int res;
	private int zres;
	private float[] field_pos;
	private float[] field_color;
	private byte[] field_indices;
	
	private float maxcolor;
	private float mincolor;
	
	float cursorx, cursory;
	float cursorx_temp, cursory_temp;
	
	
	
	public Esim3D(int width, int height, int res){
		uniforms = new HashMap<String, Integer>();
		this.width = width;
		this.height = height;
		this.res = res;
		window = new Window(TITLE, width, height, false);
	}
	
	@Override
	public void init(){
		beta = (float)Math.PI;
		alpha = (float)(5*Math.PI/8);
		gamma = (float)Math.PI;
		
		xtrans = 0;
		ytrans = 0;
		ztrans = -1;
		
		zoom = 1.0f;
		
		zres = 20;
		
		tilting = false;
		
		maxcolor = Charge.K*10;
		mincolor = -maxcolor;
		
		window.init();
		
		
		
		updateFieldRes(res);
		
		projMatrix = new Matrix4f();
		worldMatrix = new Matrix4f();

		
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
		glfwSetWindowSizeCallback(window.getWindowHandle(), windowCallback);
		
		final float fovY = 45.0f;
		final float front = 0.1f;
		final float back = 128.0f;
		final double deg2rad = Math.PI / 180;
		bufferCallback = new GLFWFramebufferSizeCallback(){
			@Override
			public void invoke(long windowHandle, int width, int height) {
				float ratio = (height > 0)? width/(float)height : 1;

				glViewport(0, 0, width, height);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();

				double tangent = Math.tan(fovY*deg2rad/2);
				double heightf = front*tangent;
				double widthf = heightf*ratio;
				glFrustum(-widthf, widthf, -heightf, heightf, front, back);
			}
		};
		//glfwSetFramebufferSizeCallback(window.getWindowHandle(), bufferCallback);
		
		final float rotstep = 0.5f;
		final float transstep = 0.01f;
		keyCallback = new GLFWKeyCallback(){
			@Override
			public void invoke(long windowHandle, int key, int scancode, int action, int mods) {
				if(action == GLFW_PRESS || action == GLFW_REPEAT){
					switch(key){
						case GLFW_KEY_ESCAPE:
							glfwSetWindowShouldClose(window.getWindowHandle(), true);
							break;
						case GLFW_KEY_SPACE: //make this do something
							break;
						case GLFW_KEY_A:
							beta += rotstep;
							break;
						case GLFW_KEY_D:
							beta += -rotstep;
							break;
						case GLFW_KEY_W:
							alpha += -rotstep;
							break;
						case GLFW_KEY_S:
							alpha += rotstep;
							break;
						case GLFW_KEY_E:
							gamma += rotstep;
							break;
						case GLFW_KEY_Q:
							gamma -= rotstep;
							break;
						case GLFW_KEY_UP:
							ytrans += transstep;
							break;
						case GLFW_KEY_DOWN:
							ytrans += -transstep;
							break;
						case GLFW_KEY_RIGHT:
							xtrans += transstep;
							break;
						case GLFW_KEY_LEFT:
							xtrans += -transstep;
							break;
						//maybe add more
						default:
							break;
					}
				}	
			}
		};
		glfwSetKeyCallback(window.getWindowHandle(), keyCallback);
		
		float step = 0.01f;
		scrollCallback = new GLFWScrollCallback(){
			@Override
			public void invoke(long windowHandle, double xoffset, double yoffset) {
				zoom += step*yoffset;
				if(zoom <= 0) zoom = 0;
				//System.out.println(zoom);
			}
		};
		glfwSetScrollCallback(window.getWindowHandle(), scrollCallback);
		
		
		
		posCallback = new GLFWCursorPosCallback(){
			@Override
			public void invoke(long windowHandle, double xpos, double ypos) {
				if(window.getWindowHandle() == windowHandle){
		    		cursorx = (float) (xpos*2/width -1);
		    		cursory = (float) (-ypos*2/height +1);
		    	}
			}
		};
		glfwSetCursorPosCallback(window.getWindowHandle(), posCallback);
		
		buttonCallback = new GLFWMouseButtonCallback(){

			@Override
			public void invoke(long windowHandle, int button, int action, int mods) {
				if(window.getWindowHandle() == windowHandle){
					if(button == GLFW_MOUSE_BUTTON_1){
						if(action == GLFW_PRESS){
							tilting = true;
							cursorx_temp = cursorx;
							cursory_temp = cursory;
							beta_temp = beta;
							alpha_temp = alpha;
							gamma_temp = gamma;
						} else if(action == GLFW_RELEASE){
							tilting = false;
						}
					} else if(button == GLFW_MOUSE_BUTTON_2){
						if(action == GLFW_PRESS){
							panning = true;
							cursorx_temp = cursorx;
							cursory_temp = cursory;
							xtrans_temp = xtrans;
							ytrans_temp = ytrans;
							ztrans_temp = ztrans;
						} else if(action == GLFW_RELEASE){
							panning = false;
						}
					}
				}
				
			}
		};
		glfwSetMouseButtonCallback(window.getWindowHandle(), buttonCallback);
		
//		try {
//			shaderProgram = new ShaderProgram();
//			shaderProgram.createVertexShader(VERTEX_SHADER);
//			shaderProgram.createFragmentShader(FRAGMENT_SHADER);
//			shaderProgram.link();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		verticesBuffer = MemoryUtil.memAllocFloat(field_pos.length);
		colorsBuffer = MemoryUtil.memAllocFloat(field_pos.length);
		indicesBuffer = MemoryUtil.memAlloc(field_pos.length);
		
		vaoId = GL30.glGenVertexArrays();
		
		vboId_pos = GL15.glGenBuffers();
		GL30.glBindVertexArray(vaoId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId_pos);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		
		vboId_color = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId_color);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		//System.out.println("memes");
		
		initShaders();
	}
	
	
	private void initShaders(){
		int errorCheckValue;
		

		vsId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vsId, VERTEX_SHADER);
        GL20.glCompileShader(vsId);
        
        fsId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fsId, FRAGMENT_SHADER);
        GL20.glCompileShader(fsId);
        
        pId = GL20.glCreateProgram();
        GL20.glAttachShader(pId, vsId);
        GL20.glAttachShader(pId, fsId);   
        

        
        GL20.glLinkProgram(pId);
        GL20.glValidateProgram(pId);

        
        
       // System.out.println(glGetProgramInfoLog(pId, 1024));
        
       //
        //System.out.println(pmLoc);
        //if(pmLoc == 0) System.out.println("!couldnt find uniform!");
        


        

//      GL20.glUniformMatrix4fv(pmLoc, false, temp); //this causes the error***********************

        while((errorCheckValue = GL11.glGetError()) != GL11.GL_NO_ERROR){
        //	System.out.println(errorCheckValue);
        }
	}
	

	private void genProjectionMatrix() {
		
		//MAKE THIS
		
		projMatrix.identity();
		//projMatrix.perspective((float)(Math.PI/4.0f), width/(float)height, 0.1f, 128.0f);
		float fovY = 45.0f;
		float front = 0.2f;
		float back = 128.0f;
		final double deg2rad = Math.PI / 180;
		
		float ratio = width/(float)height;
		
		double tangent = Math.tan(fovY*deg2rad/2);
		float heightf = (float) (front*tangent);
		float widthf = (float) (front*tangent*ratio);
		projMatrix.setFrustum(-widthf, widthf, -heightf, heightf, front, back);
	}
	
	private void genWorldMatrix(){
		worldMatrix.identity().translate(xtrans, ytrans, ztrans).rotateX(alpha).rotateY(beta).rotateZ(gamma).scale(zoom);
	}

	@Override
	public void draw() {
		
		glEnable(GL_BLEND);
		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_DEPTH_TEST);

		GL30.glBindVertexArray(vaoId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId_pos);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_DYNAMIC_DRAW);
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId_color);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorsBuffer, GL15.GL_DYNAMIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);

		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0,0,0,1);
		
//		glMatrixMode(GL_MODELVIEW);
//		glLoadIdentity();
//		glTranslatef(xtrans,ytrans,ztrans);
//		glScalef(zoom, zoom, zoom);
//		glRotatef(alpha, 1, 0, 0);
//		glRotatef(beta, 0, 1, 0);
//		glRotatef(gamma, 0, 0, 1);
		
		

		
		//drawOrigin();
		draw3D();
		
	}
	
	
	private void draw3D(){
//		verticesBuffer = MemoryUtil.memAllocFloat(field_pos.length);
//		vaoId = glGenVertexArrays();
//		glBindVertexArray(vaoId);
//		vboId = glGenBuffers();
//		glBindBuffer(GL_ARRAY_BUFFER, vboId);
//		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
//		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//		glBindBuffer(GL_ARRAY_BUFFER, 0);
//		glBindVertexArray(0);
//		MemoryUtil.memFree(verticesBuffer);
//		
//		shaderProgram.bind();
//		glBindVertexArray(vaoId);
//		glEnableVertexAttribArray(0);
		
		float pointSize = 1;
		glPointSize(pointSize);
		glColor4f(1f,1f,1f,0.7f);
		
		//glDrawArrays(GL_POINTS, 0, 3);
		
//		glDisableVertexAttribArray(0);
//		glBindVertexArray(0);
//		shaderProgram.unbind();
//		
		GL20.glUseProgram(pId);
		
		int pmLoc = GL20.glGetUniformLocation(pId, "pm");
		
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer temp = stack.mallocFloat(16);
            genProjectionMatrix();
            projMatrix.get(temp);
            GL20.glUniformMatrix4fv(pmLoc, false, temp);
        }
		
        int wmLoc = GL20.glGetUniformLocation(pId, "wm");
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer temp = stack.mallocFloat(16);
            genWorldMatrix();
            worldMatrix.get(temp);
            GL20.glUniformMatrix4fv(wmLoc, false, temp);
        }
        
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, field_pos.length/3);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL20.glUseProgram(0);
		
//		glBegin(GL_POINTS);
//		{
//			for(int i = 0; i < field_pos.length; i+=3){
//				glColor3f(field_color[i+0], field_color[i+1], field_color[i+2]);
//				glVertex3f(field_pos[i+0]*zoom, field_pos[i+1]*zoom, field_pos[i+2]*zoom);
//			}
//		} glEnd();
		
	}

	
	private void drawOrigin(){
		float lineWidth = 1;
		float transparency = 1f;
		float scale = 0.3f;
		
		glLineWidth(lineWidth);
		glBegin(GL_LINES);
		{
			glColor4f(1, 0, 0, transparency);
			glVertex3f(0, 0, 0);
			glColor4f(1, 0, 0, transparency);
			glVertex3f(scale*zoom, 0, 0);
			
			glColor4f(0, 1, 0, transparency);
			glVertex3f(0, 0, 0);
			glColor4f(0, 1, 0, transparency);
			glVertex3f(0, scale*zoom, 0);
			
			glColor4f(0, 0, 1, transparency);
			glVertex3f(0, 0, 0);
			glColor4f(0, 0, 1, transparency);
			glVertex3f(0, 0, scale*zoom);
			
		} glEnd();
		
	}
	
	@Override
	public void update() {
		//System.out.println("alpha: " + alpha + " beta: " + beta + " gamma: " + gamma);
		if(glfwGetWindowAttrib(window.getWindowHandle(), GLFW_FOCUSED) == GLFW_FALSE){
			updateField();
			updateBuffers();
		}
		//updateBuffers();
		if(tilting) updateTilt();
		//if(panning) updatePan(); //FIX PANNING****************************************************************
	}
	
	public void updateFieldRes(int res){
		this.res = res;
		field_pos = new float[(res+1)*(res+1)*3];
		field_color = new float[(res+1)*(res+1)*3];
		field_indices = new byte[field_pos.length];
		//MemoryUtil.memRealloc(verticesBuffer, field_pos.length);
		//MemoryUtil.memRealloc(colorsBuffer, field_color.length);
		//MemoryUtil.memRealloc(indicesBuffer, field_indices.length);
	}
	
	private void genIndexOrder(){
		for(int i = 0; i < field_indices.length; i++){
			
		}
	}
	
	private void updateField(){
		float w = res*0.5f;
		float h = w;
		int i, j;
		int index = 0;
		i = 0;
		int colorval;
		Vector2f hold;
		Vector3f color;
		float c;
		for(float x = -w; x <= w; x++){
			j = 0;
			for(float y = -h; y <= h; y++){
				index = i*res+j;
				hold = new Vector2f(x/w, y/h);
				synchronized(charges){
					c = Charge.ePotential(charges, hold);
				}
				field_pos[index+0] = (x/w);
				field_pos[index+1] = (y/h);
				field_pos[index+2] = (c/(Charge.K*zres));
				color = EsimDriver.getColor(c, mincolor, maxcolor, false);
				field_color[index+0] = color.x;
				field_color[index+1] = color.y;
				field_color[index+2] = color.z;
				j+=3;
			}
			i+=3;
		}

	}
	
	private void updateBuffers(){
		verticesBuffer.clear();
		verticesBuffer.put(field_pos);
		verticesBuffer.flip();
		colorsBuffer.clear();
		colorsBuffer.put(field_color);
		colorsBuffer.flip();
	}
	
	private void updateTilt(){
		gamma = (float) (gamma_temp - Math.atan(cursorx_temp - cursorx));
		alpha = (float) (alpha_temp + Math.atan(cursory_temp - cursory));
	}

	private void updatePan(){ //FIX THIS!!!!***************************************************************
		xtrans = -(cursorx_temp - cursorx);
		ytrans = -(cursory_temp - cursory);
	}
	
	@Override
	public Window getWindow() {
		return window;
	}

	@Override
	public void dispose() {
        // Delete the shaders
        GL20.glUseProgram(0);
        GL20.glDetachShader(pId, vsId);
        GL20.glDetachShader(pId, fsId);
         
        GL20.glDeleteShader(vsId);
        GL20.glDeleteShader(fsId);
        GL20.glDeleteProgram(pId);
        
		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		 
		// Delete the VBOs
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId_pos);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId_color);
		
		//Delete shader (this might not be that good actually)
		//shaderProgram.cleanup();
		 
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
		
    }
}
