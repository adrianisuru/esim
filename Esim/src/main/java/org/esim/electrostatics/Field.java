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

	/**Coulomb's Constant*/
	public static final float K = (float) (9*Math.pow(10, 9));
	
	/**Epsilon Naught, The permitivity of free space*/
	public static final float E0 = (float) (Math.pow(10, -9)/(36*Math.PI));
	
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
	 * Index of the current charge.
	 * (Used for removal and editing of charges).
	 */
	private int currentChargeIndex;
	
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

	
	/**
	 * Gets the width.
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Adds charges to this field.
	 * @param charges the charges to add.
	 */
	public void addCharges(Charge...charges) {
		for(Charge c : charges) {
			this.charges.add(c);
		}
		currentChargeIndex = this.charges.size() - 1;
	}
	
	/**
	 * Removes the charge at the index.
	 * @param index the index to remove the charge from
	 */
	public void removeChargeAt(int index) {
		charges.remove(index);
	}
	
	/**
	 * Removes the current charge.
	 */
	public void removeCurrentCharge() {
		removeChargeAt(currentChargeIndex);
	}
	
	public void selectCharge(Charge c) {
		currentChargeIndex = charges.indexOf(c);
	}
	
	/**
	 * Gets the charge at the index.
	 * @param index index of the charge
	 * @return the charge
	 */
	public Charge getChargeAt(int index) {
		return charges.get(index);
	}
	
	/**
	 * Gets the current charge.
	 * @return the current charge.
	 */
	public Charge getCurrentCharge() {
		return charges.get(currentChargeIndex);
	}
	
	/**
	 * Gets all charges in an array.
	 * @return charges in an array
	 */
	public Charge[] getCharges() {
		return charges.toArray(new Charge[charges.size()]);
	}
	
	/**
	 * Gets the number of charges present in this field.
	 * @return the number of charges
	 */
	public int getChargeNumber() {
		return charges.size();
	}
	
	/**
	 * Gives the electric field vector at the given position.
	 * @param pos the position to calculate the electric field
	 * @return the electric field vector
	 */
	public Vector2f eField(Vector2f pos) {
		Vector2f sum = new Vector2f(0);
		Vector2f temp = new Vector2f(0);
		
		float len = 0;
		for(Charge c : charges) {
			//Calculate vector from charge position to (x,y)
			temp.set(c.x, c.y);
			temp.mul(-1);
			temp.add(pos);
			
			len = temp.length(); // get distance to charge
			
			//sum up electric field vectors from each charge
			sum.add(temp.mul(K*c.value/(len*len*len)));
			
		}
		
		return sum;
	}
	
	/**
	 * Calculates the electric potential at a point.
	 * @param pos position to calculate the potential at
	 * @return the electric potential
	 */
	public float ePotential(Vector2f pos) {
		float sum = 0;
		Vector2f temp = new Vector2f(pos);
		
		for(Charge c : charges){
			temp.set(c.x, c.y);
			temp.mul(-1);
			temp.add(pos);
			
			sum += K*c.value/temp.length();
		}
		
		return sum;
	}
}
