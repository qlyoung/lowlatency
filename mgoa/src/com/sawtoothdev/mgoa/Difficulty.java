package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public final long ringTimeMs;	
	public final long minBeatSpace;
	public final String name;
	
	public Difficulty(long ringTimeMs, long minBeatSpace, String name){
		this.name = name;
		this.ringTimeMs = ringTimeMs;
		this.minBeatSpace = minBeatSpace;
	}
}
