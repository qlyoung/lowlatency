package com.sawtoothdev.mgoa.objects;

public class Difficulty {
	
	public final long ringTimeMs;	
	public final long minBeatSpace;
	public final String name;
	public final int scoreMultiplier;
	
	public Difficulty(long ringTimeMs, long minBeatSpace, String name, int scoreMultiplier){
		this.name = name;
		this.ringTimeMs = ringTimeMs;
		this.minBeatSpace = minBeatSpace;
		this.scoreMultiplier = scoreMultiplier;
	}
}
