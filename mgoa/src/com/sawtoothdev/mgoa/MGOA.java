package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.mgoa.Difficulty.DifficultyName;

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
		Resources.difficulty = new Difficulty(DifficultyName.HARD);
		Resources.random = new Random();
		Resources.spriteBatch = new SpriteBatch();
		Resources.worldDimensions = new Vector2(10, 6);
		Resources.camera = new OrthographicCamera();
		Resources.camera.setToOrtho(false, Resources.worldDimensions.x, Resources.worldDimensions.y);
		Resources.camera.position.set(0, 0, 0);
		
		// load an internal song for now
		FileHandle song = Gdx.files.internal("data/audio/music.mp3");
		song = Gdx.files.absolute(song.file().getAbsolutePath());
		Gdx.app.log("abs path", song.file().getAbsolutePath());

		// and launch!
		this.setScreen(new LoadScreen(song));

	}
	

}
