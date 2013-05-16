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
 * Some global resources, grouped in one place for convenience
 * 
 * @author albatross
 *
 */

public class Resources {

	// general globals
	public static Game game;
	public static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/title.mp3"));
	public static FileHandle currentSong;
	
	// global settings
	public static Difficulty difficulty = new Difficulty(Difficulty.DifficultyName.HARD);
	public static Vector2 worldDimensions = new Vector2(10, 6);
	//public static Vector2 scaleFactor = new Vector2(Gdx.graphics.getWidth() / 800f, Gdx.graphics.getHeight() / 480f);
	
	// heavyweights we only need one of
	public static Random random = new Random();
	public static SpriteBatch spriteBatch = new SpriteBatch();
	public static OrthographicCamera camera = new OrthographicCamera(10, 6);
	
	// flow control
	public static final MenuScreen menuScreen = new MenuScreen();
	public static final ChooseSongScreen chooseSongScreen = new ChooseSongScreen();
	public static final LoadScreen loadScreen = new LoadScreen();
	
	
	
	// projects a Vector2 from world space to screen space
	public static Vector2 projectToScreen(Vector2 worldCoords){
		
		Vector3 temp = new Vector3(worldCoords.x, worldCoords.y, 0);
		camera.project(temp);
		
		worldCoords = new Vector2(temp.x, temp.y);
		return worldCoords;
		
	}
	// projects a Vector2 from screen space to world space
	public static Vector2 projectToWorld(Vector2 screenCoords){
		Vector3 temp = new Vector3(screenCoords.x, screenCoords.y, 0);
		camera.unproject(temp);
		
		screenCoords = new Vector2(temp.x, temp.y);
		return screenCoords;
	}
	

}
