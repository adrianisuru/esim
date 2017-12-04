package org.esim.engine.model;

import static org.lwjgl.opengl.GL11.GL_LINES;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Class used for drawing a grid.
 * @author adrianisuru
 *
 */
public class Grid implements Model{

	
	private int width;
	
	private int height;
	
	private Vector4f color;
	
	private final int vboid;
	
	private final int vaoid;
	
	private float[] vertices;
	
	public Grid(int width, int height, Vector4f color) {
		
		updateSize(width, height);
		updateColor(color);
		
		vaoid = GL30.glGenVertexArrays();
		vboid = GL15.glGenBuffers();
		
		GL30.glBindVertexArray(vaoid);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(vaoid);
		
		this.update();

	}
	
	
	public void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void updateColor(Vector4f color) {
		this.color = color;
	}
	
	@Override
	public void draw() {
		GL11.glColor4f(color.x, color.y, color.z, color.w);
		GL30.glBindVertexArray(vaoid);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_LINES, 0, (width+height)*2);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	
	
	@Override
	public void update() {
		vertices = new float[(width+height)*4];
		
		float w = width/2.0f;
		float h = height/2.0f;
		
		int k = 0;
		for(float x = -w; x < w; x++){
			vertices[k++]= x/w;
			vertices[k++]= -1;
			vertices[k++]= x/w;
			vertices[k++]= 1;
		}
		for(float y = -h; y < h; y++){
			vertices[k++]= -1;
			vertices[k++]= y/h;
			vertices[k++]= 1;
			vertices[k++]= y/h;
		}
		
		GL30.glBindVertexArray(vaoid);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(vaoid);
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
