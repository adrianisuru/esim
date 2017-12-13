package org.esim.engine;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public class Shader {

	private final int id;
	
	private HashMap<String, Integer> shaders;
	
	private HashMap<String, Integer> uniforms;
	
	public Shader() throws Exception {
		id = glCreateProgram();
        if (id == 0) {
            throw new Exception("Could not create Shader");
        }
        
        shaders = new HashMap<String, Integer>();
        
        uniforms = new HashMap<String, Integer>();
	}
	
	
	public int createShader(String name, int type, String code) throws Exception {
        int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + type);
        }

        glShaderSource(shaderId, code);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(id, shaderId);
        
        shaders.put(name, shaderId);

        return shaderId;
    }
	
    public void link() throws Exception {
        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(id, 1024));
        }

        for(int i : shaders.values()) {
        	if(i != 0) {
        		glDetachShader(id, i);
        	}
        }
        
        
        glValidateProgram(id);
        if (glGetProgrami(id, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(id, 1024));
        }
    }
    
    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = GL20.glGetUniformLocation(id, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }
    
    public void setUniformMatrix4fv(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
        	FloatBuffer temp = stack.mallocFloat(16);
            value.get(temp);
            GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, temp);
        }
    }
    
    public void enableLayoutLoc(String name, int loc) {
    	GL20.glBindAttribLocation(id, loc, name);
    }

    public void bind() {
        glUseProgram(id);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (id != 0) {
            glDeleteProgram(id);
        }
    }
	
}
