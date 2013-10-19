package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public static Difficulty EASY = new Difficulty(800, 250);
	public static Difficulty NORMAL = new Difficulty(650, 150);
	public static Difficulty HARD = new Difficulty(600, 100);
	public static Difficulty ORIGINAL = new Difficulty(500, 0);
	
	
	public final long ringTimeMs;	
	public final long minBeatSpace;
	
	public Difficulty(long ringTimeMs, long minBeatSpace){
		this.ringTimeMs = ringTimeMs;
		this.minBeatSpace = minBeatSpace;
	}
}
