package com.sawtoothdev.mgoa;

public class Difficulty {
	
	public static enum DifficultyName { EASY, NORMAL, HARD, HARDPLUS };
	
	public Difficulty(DifficultyName name){
		
		this.name = name;
		
		switch (name){
		case EASY:
			player_velocity = 8;
			beat_velocity = 4;
			break;
		case HARD:
			player_velocity = 9;
			beat_velocity = 6.0f;
			break;
		case HARDPLUS:
			player_velocity = 9;
			beat_velocity = 6.5f;
			break;
		default:
		case NORMAL:
			player_velocity = 8;
			beat_velocity = 8f;
			break;
				
		}
		
	}
	
	public final DifficultyName name;	
	public final float player_velocity;
	public final float beat_velocity;

}
