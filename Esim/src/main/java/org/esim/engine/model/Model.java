package org.esim.engine.model;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public abstract class Model {

	protected final int vao_id;
	
	protected int indicesCount;

	protected Model() {
		vao_id = GL15.glGenBuffers();

	}

	public abstract void draw();

	public abstract void update();
	
	
}