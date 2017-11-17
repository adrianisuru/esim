package org.esim.engine;

import org.lwjgl.opengl.GL30;

public class Model {

	private final int vao_id;
	
	
	
	
	public Model() {
		vao_id = GL30.glGenVertexArrays();
	}
	
	
}
