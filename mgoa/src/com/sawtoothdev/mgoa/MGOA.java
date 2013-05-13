package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;

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
		
		Resources.camera = new OrthographicCamera();
		Resources.camera.setToOrtho(false, Resources.worldDimensions.x, Resources.worldDimensions.y);
		Resources.camera.position.set(0, 0, 0);

		// and launch
		this.setScreen(Resources.menuScreen);

	}
	

}
