package org.esim.engine.model;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;

import java.util.Arrays;

import org.esim.electrostatics.Field;
import org.esim.engine.Shader;
import org.esim.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

@Deprecated
public class Graph implements Model{

	private Shader shader;
	
	private float[] vertices;
	
	private float[] colors; // maybe add this later
	
	private int[] indices;
	
	private int 
		vao,
		vboVertices,
		vboColors,
		vboIndices;
	
	private int res;
	
	private Field field;
	
	private int heightscale = 1;
	
	private float
		alpha,
		beta,
		gamma,
		xtrans,
		ytrans,
		ztrans,
		zoom;
	
	private boolean 
		tilting;
	
	private Matrix4f 
		projMatrix, 
		worldMatrix;
	
	private int
		windowWidth,
		windowHeight;
	
	
	public Graph(Field field, int res) {
		this.field = field;
		this.res = res;
		
		vao = GL30.glGenVertexArrays();
		
		vertices = new float[res*res*3];
		genIndexOrder();

		
		GL30.glBindVertexArray(vao);
		
		vboVertices = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVertices);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

		vboIndices = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(1, 1, GL11.GL_INT, false, 1, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		
		try {
			shader = new Shader();
			shader.createShader("vertex", GL20.GL_VERTEX_SHADER, Utils.loadResource("/_3d_vertex.glsl"));
			shader.createShader("fragment", GL20.GL_FRAGMENT_SHADER, Utils.loadResource("/_3d_fragment.glsl"));
			shader.link();
			shader.createUniform("pm");
			shader.createUniform("wm");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		projMatrix = new Matrix4f();
		worldMatrix = new Matrix4f();
		
		update();
	}
	
	private void genProjectionMatrix(int width, int height) {
		
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
	/**
	 * Call genProjectionMatrix first
	 */
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
		
		GL30.glBindVertexArray(vao);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVertices);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_DYNAMIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		shader.bind();
		
		shader.setUniformMatrix4fv("pm", projMatrix);
		shader.setUniformMatrix4fv("wm", worldMatrix);
		
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboIndices);
		GL11.glDrawElements(GL11.GL_LINE_STRIP, indices.length, GL11.GL_UNSIGNED_INT, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		//System.out.println("boi");
		shader.unbind();
	}

	@Override
	public void update() {
		if(vertices == null || vertices.length != res*res*3) {
			vertices = new float[res*res*3];
			genIndexOrder();
		}
		
		updateField();
		
	
		
		genProjectionMatrix(this.windowWidth, this.windowHeight);
		genWorldMatrix();
		
		
		
	}
	
	public void updateWindowSize(int w, int h) {
		this.windowWidth = w;
		this.windowHeight = h;
	}
	
	private void updateField() {

		float w = res/2.0f;
		float h = w;
		int i, j;
		int index;
		i = 0;
//		int colorval;
		Vector2f hold = new Vector2f(0);
//		Vector3f color;
		float c;
		for(float x = -w; x < w; x++){
			j = 0;
			for(float y = -h; y < h; y++){
				index = i*res+j;
				hold.set(x/w, y/h);
				c = field.ePotential(hold);
					
				vertices[index+0] = (x/w);
				vertices[index+1] = (y/h);
				vertices[index+2] = 0*(c/(Field.K*heightscale));
//				color = genColor(c, mincolor, maxcolor, false); // something like this
//				colors[index+0] = color.x;
//				colors[index+1] = color.y;
//				colors[index+2] = color.z;
				j+=3;
			}
			i+=3;
		}
	}
	
	private void genIndexOrder(){
		indices = new int[(res)*(res)*2];
		int index = 0;
		int parity = res % 2;
		int i = 0;
		int j = res * parity;
		while(index < res*res){
			if((i % 2) == parity){
				while(j < res){
					indices[index++] = i*res+j;
					j++;
				}
			} else{
				while(j > 0){
					j--;
					indices[index++] = i*res+j;
				}
			}
			i++;
		}
		while(index < res*res*2){
			if((j % 2) == 0){
				while(i > 0){
					i--;
					indices[index++] = i*res+j;
				}
			} else{
				while(i < res){
					indices[index++] = i*res+j;
					i++;
				}
			}
			j++;
		}
		
		//System.out.println(Utils.pointAsString(indices));
	}
	

	@Override
	public void cleanup() {
		shader.cleanup();
	}

	
	
}
