package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public static enum DifficultyName { EASY, NORMAL, HARD, HARDPLUS, TESTING };
	
	public Difficulty(DifficultyName name){
		
		this.name = name;
		
		switch (name){
		case EASY:
			ringTimeMs = 1500;
			break;
		default:
		case NORMAL:
		case TESTING:
			ringTimeMs = 1000;
			break;
		case HARD:
			ringTimeMs = 700;
			break;
		case HARDPLUS:
			ringTimeMs = 250;
			break;
		}
		
	}
	
	public final DifficultyName name;	
	public final long ringTimeMs;

}
