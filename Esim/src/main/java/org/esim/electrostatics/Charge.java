package org.esim.electrostatics;

import org.joml.Vector2f;

/**
 * Class representing an electric charge defined by 
 * xy position (in meters) and charge (in coulombs).
 * <b>boi boi boi
 * @author adrianisuru
 *
 */
public class Charge {

	/**The x position of this charge.*/
	float x;
	
	/**The y position of this charge.*/
	float y;
	
	/**The value of this charge in coulombs.*/
	float value;
	
	/**
	 * Creates a new charge given an (x, y) coordinate and a value.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param value the value in coulombs
	 */
	public Charge(float x, float y, float value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	/**
	 * Creates a new charge given an (x, y) coordinate.
	 * The initial value of this charge is set to +1C.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Charge(float x, float y) {
		this(x, y, 1);
	}
	
	/**
	 * Copy constructor for charge.
	 * @param charge the charge to copy
	 */
	public Charge(Charge charge) {
		this(charge.x, charge.y, charge.value);
	}
	
	/**
	 * The x coordinate of this charge.
	 * @return the x coordinate
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * The y coordinate of this charge.
	 * @return the y coordinate
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * The value of this charge in coulombs.
	 * @return the value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Give distance between this charge and pos
	 * @param pos position to calculate the distance to
	 * @return the distance from this to pos
	 */
	public float getDistance(Vector2f pos) {
		Vector2f temp = new Vector2f(0);
		//Calculate vector from charge position to (x,y)
		temp.set(x, y);
		temp.mul(-1);
		temp.add(pos);
		
		return temp.length(); // get distance to charge
	}
	
	/** 
	 * Generates a hash code using the x, y, and value fields.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(value);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	/**
	 * Equality based on x, y, and value fields.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Charge)) {
			return false;
		}
		Charge other = (Charge) obj;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value)) {
			return false;
		}
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
			return false;
		}
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
			return false;
		}
		return true;
	}

	/**
	 * To String method based on x, y, and value.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Charge [x=" + x + ", y=" + y + ", value=" + value + "]";
	}
	
	
	
}
