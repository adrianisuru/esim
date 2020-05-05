package org.esim.engine.model;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Abstract class for making models
 * 
 * @author adrianisuru
 *
 */
public interface Model {

	/**
	 * Draws this model.
	 */
	public void draw();

	/**
	 * Updates this model.
	 */
	public void update();

	/**
	 * Does any cleanup associated with this model.
	 */
	public void cleanup();
}