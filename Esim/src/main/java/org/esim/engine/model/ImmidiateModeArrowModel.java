package org.esim.engine.model;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.esim.electrostatics.Field;
import org.esim.util.Utils;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class ImmidiateModeArrowModel implements Model{

	private Field field;
	
	private float[] vertices;
	
	private int width, height;
	
	private Vector4f color;
	
	public ImmidiateModeArrowModel(Field field, int width, int height, Vector4f color) {
		this.field = field;
		this.width = width;
		this.height = height;
		this.color = color;
		this.update();
	}
	
	
	
	@Override
	public void draw() {
	
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor4f(color.x, color.y, color.z, color.w);
		for(int i = 0; i < vertices.length;) {
			GL11.glVertex3f(vertices[i], vertices[i+1], 0);
			//System.out.println(Utils.pointAsString(vertices[i], vertices[i+1]));
			i += 2;
		}
		GL11.glEnd();
	}

	@Override
	public void update() {
		vertices = new float[(width + 1)*(height + 1)*4];
		
		float w = width/2.0f;
		float h = height/2.0f;
		
		int k = 0;
		Vector2f temp = new Vector2f(0);
		
		for(float x = -w; x <= w; x++){
			for(float y = -h; y <= h; y++){
				temp.set(x/w, y/h);
				temp = field.eField(temp).normalize();
				vertices[k++]= x/w;
				vertices[k++]= y/h;
				vertices[k++]= (x/w) + temp.x/(width/2.0f);
				vertices[k++]= (y/h) + temp.y/(height/2.0f);
				
			}
		}
		
		
	}

	@Override
	public void cleanup() {
		
	}

}
