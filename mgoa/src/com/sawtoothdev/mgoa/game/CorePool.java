package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.utils.Pool;
import com.sawtoothdev.mgoa.Difficulty;

public class CorePool extends Pool<BeatCore> {

	private Difficulty difficulty;
	
	public CorePool(Difficulty difficulty){
		this.difficulty = difficulty;
	}
	
	@Override
	protected BeatCore newObject() {
		return new BeatCore(difficulty);
	}

}
