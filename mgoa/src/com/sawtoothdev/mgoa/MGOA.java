package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Main game class, top level.
 * Controls screen switching and not much else.
 * 
 * @author albatross
 *
 */

public class MGOA extends Game {

	@Override
	public void create() {
		
		Resources.game = this;
		
		// set clear color to blue
		Gdx.gl.glClearColor(0, 0, 1, 0);
		
		FileHandle song = Gdx.files.internal("data/audio/aki.mp3");

		{// debugging, remove for final release
			song = Gdx.files.absolute(song.file().getAbsolutePath());
			Gdx.app.log("abs path", song.file().getAbsolutePath());
		}

		this.setScreen(new LoadScreen(song));
		
	}

}
