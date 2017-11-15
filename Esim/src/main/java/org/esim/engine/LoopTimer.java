package org.esim.engine;

public class LoopTimer {

	private double lastTime;
	
	private final double startTime;
	
	public LoopTimer() {
		startTime = System.nanoTime();
		lastTime = startTime;
	}
	
	public float getLoopTime() {
		double time = System.nanoTime();
		float loopTime = (float) (time - lastTime);
		lastTime = time;
		return loopTime;
	}
	
}
