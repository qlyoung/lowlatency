package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public static enum DifficultyName { EASY, NORMAL, HARD, HARDPLUS, TESTING };
	
	public Difficulty(DifficultyName name){
		
		this.name = name;
		
		switch (name){
		case EASY:
			ring_time_secs = 1.5f;
			break;
		default:
		case NORMAL:
		case TESTING:
			ring_time_secs = 1f;
			break;
		case HARD:
			ring_time_secs = .5f;
			break;
		case HARDPLUS:
			ring_time_secs = .25f;
			break;
		}
		
	}
	
	public final DifficultyName name;	
	public final float ring_time_secs;

}
