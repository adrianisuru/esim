package org.esim.engine.model;

import java.awt.Color;

import org.esim.electrostatics.Charge;
import org.esim.electrostatics.Field;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ChargeModel implements Model{

	private Field field;
	
	private int vao_id;
	private int vert_id;
	private int color_id;
	
	public ChargeModel(Field field) {
		this.field = field;
		
		vao_id = GL30.glGenVertexArrays();
		vert_id = GL15.glGenBuffers();
		color_id = GL15.glGenBuffers();
		
		GL30.glBindVertexArray(vao_id);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vert_id);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, color_id);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		update();
	}

	@Override
	public void draw() {
		GL11.glPointSize(4);
		GL30.glBindVertexArray(vao_id);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, field.getChargeNumber());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		
	}

	@Override
	public void update() {
		
		Charge[] charges = field.getCharges();
		
		float[] vertices, colors;
		
		vertices = new float[charges.length*2];
		colors = new float[charges.length*3];
		
		int i = 0, j = 0, k = 0;
		for(Charge c : charges) {
			vertices[i++] = c.getX();
			vertices[i++] = c.getY();
			if(c.getValue() > 0) {
				colors[j++] = 1f;
				colors[j++] = 0f;
				colors[j++] = 0f;
			} else {
				colors[j++] = 0f;
				colors[j++] = 0f;
				colors[j++] = 1f;
			}
		}
		
		GL30.glBindVertexArray(vao_id);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vert_id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, color_id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colors, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
