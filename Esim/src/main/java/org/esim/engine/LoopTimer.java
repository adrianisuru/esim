package org.esim.engine;

public class LoopTimer { //TODO document this

	/**
	 * Value of System.nanoTime() when last loop occurred.
	 */
	private long lastTime;
	
	/**
	 * Value of System.nanoTime() at the creation of this timer.
	 */
	private final long startTime;
	
	/**
	 * Amount of loops since the creation of this timer
	 */
	private long loops;
	
	/**
	 * Creates a new loop timer.
	 */
	public LoopTimer() {
		startTime = System.nanoTime();
		lastTime = startTime;
		loops = 0;
	}
	
	/**
	 * Gets the time since the last loop in ms.
	 * @return the time since last loop.
	 */
	public long getTime() {
		return (System.nanoTime() - lastTime);
	}
	
	public void loopTime() {
		lastTime = System.nanoTime();
		loops++;
	}
	
	public float getLoopTime() {
		long time = System.nanoTime();
		long loopTime = getTime();
		lastTime = time;
		loops++;
		return loopTime;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public long getLifeTime() {
		return (System.nanoTime() - startTime);
	}
	
	public long getLifeLoops() {
		return loops;
	}
	
	public float getAverageLoopTime() {
		return (float) ((System.nanoTime() - startTime) / loops);
	}
}
