package org.esim.engine.model;


import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.nio.IntBuffer;

import org.esim.electrostatics.Charge;
import org.esim.electrostatics.Field;
import org.esim.engine.Shader;
import org.esim.util.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

/**
 * A charge model that uses immediate mode instead of vbos.
 * @author adrianisuru
 *
 */
public class ImmidiateModeChargeModel implements Model{

	private Field field;
	
	private int vao_id;
	private int vert_id;
	private int color_id;
	private int indices_id;
	
	private Shader shader;
	
	private int[] indices;
	private IntBuffer ibuff;
	float[] vertices, colors;
	
	public ImmidiateModeChargeModel(Field field) {
		this.field = field;
		

		
		try {
			shader = new Shader();
			shader.createShader("vertex", GL20.GL_VERTEX_SHADER, Utils.loadResource("/charges_vertex.glsl"));
			shader.createShader("fragment", GL20.GL_FRAGMENT_SHADER, Utils.loadResource("/charges_fragment.glsl"));
			
			shader.link();
			shader.enableLayoutLoc("inPosition", 0);
			shader.enableLayoutLoc("inColor", 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		update();
	}

	@Override
	public void draw() {
//		glEnable(GL_LINE_SMOOTH);
		glEnable(GL_POINT_SMOOTH);
		glEnable(GL_BLEND);
//		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glBegin(GL11.GL_POINTS);
//		GL11.glPointSize(5);
//		for(int i = 0; i < field.getChargeNumber(); i++) {
//			GL11.glColor3f(colors[i*3], colors[i*3+1], colors[i*3+2]);
//			GL11.glVertex3f(vertices[i*2], vertices[i*2+1], 0.0f);
//			
//			System.out.println(
//					"[" + vertices[i*2] + ", " + vertices[i*2+1] + "]"
//				+	"[" + colors[i*3] + ", " + colors[i*3+1] + ", " + colors[i*3+2] + "]"
//					);
//		}
//		GL11.glEnd();
		
		float chargeSize = 10;
			glPointSize(chargeSize); //draw charges
			glBegin(GL_POINTS);
			{
				for(Charge c : field.getCharges()){
					if(c.getValue() < 0) glColor3f(0, 0, 1);
					else if(c.getValue() > 0) glColor3f(1, 0, 0);
					else continue;
					glVertex3f(c.getX(), c.getY(), 0);
				}
			} glEnd();
		
		
	}

	@Override
	public void update() {
		
		Charge[] charges = field.getCharges();
		
		indices = new int[charges.length];
		
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
			indices[k] = k++;
		}
		
	}

	@Override
	public void cleanup() {
		shader.cleanup();
		
	}
	
	
	
}
