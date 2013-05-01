package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public static enum DifficultyName { EASY, NORMAL, HARD, HARDPLUS, TESTING };
	
	public final DifficultyName name;	
	public final long ringTimeMs;
	
	public Difficulty(DifficultyName name){
		
		this.name = name;
		
		switch (name){
		case EASY:
			ringTimeMs = 1000;
			break;
		default:
		case NORMAL:
		case TESTING:
			ringTimeMs = 750;
			break;
		case HARD:
			ringTimeMs = 700;
			break;
		case HARDPLUS:
			ringTimeMs = 250;
			break;
		}
		
	}
	
}
