package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
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
	
	// gfx
	public static SpriteBatch defaultSpriteBatch = new SpriteBatch();
	
	// audio
	public static MusicPlayer menuMusic;
	
	// settings
	public static Preferences settings;
	
	// miscellaneous
	public static Random random = new Random();
	
	
	// projection/unprojection convenience methods
	public static Vector2 projectToScreen(Vector2 worldCoords, Camera worldCamera){

		Vector3 temp = new Vector3(worldCoords.x, worldCoords.y, 0);
		worldCamera.project(temp);

		worldCoords = new Vector2(temp.x, temp.y);
		return worldCoords;

	}
	public static Vector2 projectToWorld(Vector2 screenCoords, Camera worldCamera){
		Vector3 temp = new Vector3(screenCoords.x, screenCoords.y, 0);
		worldCamera.unproject(temp);

		screenCoords = new Vector2(temp.x, temp.y);
		return screenCoords;
	}
}
