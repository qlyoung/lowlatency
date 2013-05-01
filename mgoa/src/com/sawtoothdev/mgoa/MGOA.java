package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.mgoa.Difficulty.DifficultyName;

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
		Resources.difficulty = new Difficulty(DifficultyName.HARD);
		
		FileHandle song = Gdx.files.internal("data/audio/music.mp3");


		{// debugging, remove for final release
			song = Gdx.files.absolute(song.file().getAbsolutePath());
			Gdx.app.log("abs path", song.file().getAbsolutePath());
		}

		this.setScreen(new LoadScreen(song));
		
	}

}
