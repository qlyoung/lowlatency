package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public long ringTimeMs;
	
	public static Difficulty EASY = new Difficulty(800);
	public static Difficulty NORMAL = new Difficulty(650);
	public static Difficulty HARD = new Difficulty(600);
	public static Difficulty ORIGINAL = new Difficulty(500);
	
	public Difficulty(long ringTimeMs){
		this.ringTimeMs = ringTimeMs;
	}
}
