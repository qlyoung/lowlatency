package com.sawtoothdev.mgoa;

import com.badlogic.gdx.utils.Pool;

public class CorePool extends Pool<BeatCore> {

	
	public CorePool(){
	}
	
	@Override
	protected BeatCore newObject() {
		return new BeatCore();
	}

}
