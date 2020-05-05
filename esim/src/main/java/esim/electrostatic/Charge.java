package esim.electrostatic;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

public class Charge {
	
	public static final float K = (float) (9*Math.pow(10, 9));
	public static final float E0 = (float) (Math.pow(10, -9)/(36*Math.PI));
	
	public Vector2f pos;
	public float value;

	public Charge(float posx, float posy, float value){
		this(new Vector2f(posx, posy), value);
	}
	
	public Charge(Vector2f pos, float value) {
		super();
		this.pos = pos;
		this.value = value;
	}

	public static Vector2f eField(List<Charge> charges, Vector2f pos){
		
		Vector2f sum = new Vector2f(0);
		float a, b;
		double m;
		for(Charge c : charges){
			a = c.pos.x-pos.x;
			b = c.pos.y-pos.y;
			m = (-K*c.value)/((a*a + b*b)*Math.sqrt(a*a + b*b));
			sum.x += a*m;
			sum.y += b*m;
		}
		
		return sum;
	}
	
	public static float ePotential(List<Charge> charges, Vector2f pos){
		float sum = 0;
		float a, b;
		for(Charge c : charges){
			a = c.pos.x-pos.x;
			b = c.pos.y-pos.y;
			sum += K*c.value/Math.sqrt(a*a + b*b);
		}
		
		return sum;
	}
	
}
