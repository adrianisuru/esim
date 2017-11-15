package org.esim.electrostatics;

import java.util.ArrayList;
import java.util.Arrays;

import org.joml.Vector2f;

/**
 * Class representing an electric field.
 * Size is in meters. <b>Charges placed outside the bounds of this field still affect it.</b>
 * @author adrianisuru
 *
 */
public class Field {

	/**
	 * List of charges in this field. 
	 * <b>May contain charges outside the bounds of this field</b>
	 */
	private ArrayList<Charge> charges;
	
	/**
	 * The width of this field.
	 */
	private int width;
	
	/**
	 * The height of this field.
	 */
	private int height;
	
	/**
	 * Creates a new field object given dimensions and initial charges.
	 * @param width the width of this field
	 * @param height the height of this field
	 * @param charges initial charges to populate this field
	 */
	public Field(int width, int height, Charge...charges) {
		this.width = width;
		this.height = height;
		this.charges = new ArrayList<Charge>(Arrays.asList(charges));
		
		
	}
	
	/**
	 * Creates a new field object 1x1 meter given initial charges.
	 * @param charges initial charges to populate this field
	 */
	public Field(Charge...charges) {
		this(1, 1, charges);
	}
	
	/**
	 * Creates a new field object 1x1 meter with no charges.
	 */
	public Field() {
		this(1, 1);
	}
	
	
	
	public static Vector2f eField() {
		return null;
	}
	
	public static float ePotential() {
		return 0.0f;
	}
}
