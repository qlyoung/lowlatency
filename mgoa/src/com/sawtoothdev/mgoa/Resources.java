package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Heavyweights and things we only need one of
 * 
 * @author albatross
 *
 */

public class Resources {

	// flow control
	public static Game game;
	public static final MenuScreen menuScreen = new MenuScreen();
	public static final ChooseSongScreen chooseSongScreen = new ChooseSongScreen();
	public static final LoadScreen loadScreen = new LoadScreen();	
	
	// gfx
	public static OrthographicCamera camera = new OrthographicCamera(10, 6);
	public static SpriteBatch worldBatch = new SpriteBatch();
	public static SpriteBatch screenBatch = new SpriteBatch();
	
	// audio
	public static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/title.mp3"));
	public static FileHandle currentSong;
	
	// settings
	public static Difficulty difficulty = new Difficulty(Difficulty.DifficultyName.HARD);
	
	// miscellaneous
	public static Random random = new Random();
	
	// projection/unprojection convenience methods
	public static Vector2 projectToScreen(Vector2 worldCoords){

		Vector3 temp = new Vector3(worldCoords.x, worldCoords.y, 0);
		camera.project(temp);

		worldCoords = new Vector2(temp.x, temp.y);
		return worldCoords;

	}
	public static Vector2 projectToWorld(Vector2 screenCoords){
		Vector3 temp = new Vector3(screenCoords.x, screenCoords.y, 0);
		camera.unproject(temp);

		screenCoords = new Vector2(temp.x, temp.y);
		return screenCoords;
	}
}
