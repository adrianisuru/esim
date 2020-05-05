package esim.engine;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public class Window {

	private String title;

	private long windowHandle;

	private int width;

	private int height;

	private boolean initialized;

	private boolean fullscreen;

	private GLFWWindowSizeCallback windowSizeCallback;
	
	private GLCapabilities capabilities;

	public Window(String title, int width, int height, boolean fullscreen) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.windowHandle = NULL;
		this.initialized = false;
	}

	@SuppressWarnings("finally")
	public boolean init(){
		try{
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		
		long pMonitor = glfwGetPrimaryMonitor();
		
		//create the window
		windowHandle = glfwCreateWindow(width, height, title, fullscreen? pMonitor : NULL, NULL);
		if(windowHandle == NULL) throw new RuntimeException("Failed to create the GLFW window");
		
		windowSizeCallback = new GLFWWindowSizeCallback(){
			@Override
            public void invoke(long wh, int w, int h) {
				if(initialized && windowHandle == wh){
					glViewport(0, 0, w, h);
					width = w;
					height = h;
				}
			}
		};
		glfwSetWindowSizeCallback(windowHandle, windowSizeCallback);
		
		if(!fullscreen){
			GLFWVidMode vidmode = glfwGetVideoMode(pMonitor);
			glfwSetWindowPos(windowHandle, (vidmode.width() - width)/2, (vidmode.height() - height)/2);
		}
		
        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        capabilities = GL.createCapabilities();
        
        initialized = true;
        return initialized;
		}finally{
			return initialized;
		}
		
	}
	
	public void focus(){
		glfwMakeContextCurrent(windowHandle);
		GL.setCapabilities(capabilities);
	}
	
	public void setVisible(boolean visible){
		if(visible) glfwShowWindow(windowHandle);
		else glfwHideWindow(windowHandle);
	}
	
	public long getWindowHandle(){
		return windowHandle;
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	public void swapBuffers(){
		glfwSwapBuffers(windowHandle);
	}
	
	public void dispose(){
		GLFW.glfwDestroyWindow(windowHandle);
	}
}
