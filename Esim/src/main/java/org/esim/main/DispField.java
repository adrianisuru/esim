package org.esim.main;

import org.esim.electrostatics.Field;
import org.esim.engine.DisplayWindow;
import org.esim.engine.GLFWWindow;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class DispField extends DisplayWindow{
	
	final int size = 3;
	
	
	int vaoId, vboId;
	
	float[] vertices = {
	        // Left bottom triangle
	        -0.5f, 0.5f, 0f,
	        -0.5f, -0.5f, 0f,
	        0.5f, -0.5f, 0f,
	        // Right top triangle
	        0.5f, -0.5f, 0f,
	        0.5f, 0.5f, 0f,
	        -0.5f, 0.5f, 0f
	};

	public DispField(Field field, GLFWWindow window) {
		super(window);
		
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		// Do something with it
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		// Do something with it
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(0, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void draw() {
		
		// Bind to the VAO that has all the information about the quad vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length/size);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		
	}
	
	@Override
	public void cleanup() {
		// Disable the VBO index from the VAO attributes list
		GL20.glDisableVertexAttribArray(0);
		 
		// Delete the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboId);
		 
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
	}

}
