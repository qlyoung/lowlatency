package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public static enum DifficultyName { EASY, NORMAL, HARD, ORIGINAL };
	
	public final DifficultyName name;	
	public final long ringTimeMs;
	
	public Difficulty(DifficultyName name){
		
		this.name = name;
		
		switch (name){
		case EASY:
			ringTimeMs = 800;
			break;
		default:
		case NORMAL:
			ringTimeMs = 650;
			break;
		case HARD:
			ringTimeMs = 600;
			break;
		case ORIGINAL:
			ringTimeMs = 500;
		}
		
	}
	
}
