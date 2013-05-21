package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;

/**
 * Main game class, top level.
 * Responsible for initialization and screen switching.
 * 
 * @author albatross
 *
 */

public class MGOA extends Game {

	@Override
	public void create() {
		
		// a bit of global setup
		Resources.game = this;
		
		// and launch
		this.setScreen(Resources.menuScreen);

	}

}