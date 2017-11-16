package org.esim.engine;

public class LoopTimer { //TODO document this

	private double lastTime;
	
	private final double startTime;
	
	private long loops;
	
	public LoopTimer() {
		startTime = System.nanoTime();
		lastTime = startTime;
		loops = 0;
	}
	
	public float getTime() {
		return (float) (System.nanoTime() - lastTime);
	}
	
	public void loopTime() {
		lastTime = System.nanoTime();
		loops++;
	}
	
	public float getLoopTime() {
		double time = System.nanoTime();
		float loopTime = getTime();
		lastTime = time;
		loops++;
		return loopTime;
	}
	
	public float getStartTime() {
		return (float) startTime;
	}
	
	public float getLifeTime() {
		return (float) (System.nanoTime() - startTime);
	}
	
	public long getLifeLoops() {
		return loops;
	}
	
	public float getAverageLoopTime() {
		return (float) ((System.nanoTime() - startTime) / loops);
	}
}
