package esim.electrostatic;

import java.util.ArrayList;

import org.joml.Vector2f;

public class Equipotential {

	private final float voltage;
	private final int res;
	private final Vector2f[] points;
	
	public Equipotential(float voltage, ArrayList<Charge> charges, int res, float err){
		this.voltage = voltage;
		this.res = res;
		ArrayList<Vector2f> temp = new ArrayList<Vector2f>();
		float x, y;
		Vector2f pos;
		for(int i = 0; i < res; i++){
			for(int j = 0; j < res; j++){
				x = (i*2.0f/res -1);
				y = (-j*2.0f/res +1);
				pos = new Vector2f(x,y);
				//System.out.println(Math.abs((voltage - Charge.ePotential(charges, pos))/voltage));
				if(Math.abs((voltage - Charge.ePotential(charges, pos))) <= err){
					//System.out.println("sicc");
					temp.add(pos);
				}
			}
		}
		points = temp.toArray(new Vector2f[temp.size()]);
	}

	public float getVoltage() {
		return voltage;
	}

	public int getRes() {
		return res;
	}

	public Vector2f[] getPoints() {
		return points;
	}
}
