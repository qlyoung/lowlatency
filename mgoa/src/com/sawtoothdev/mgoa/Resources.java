package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Heavyweights, globals and misfit objects
 * 
 * @author albatross
 */

public class Resources {

	// info
	public static final String VERSION = "pre-alpha";
	public static final boolean DEBUG = true;

	// flow control
	public static Game game;

	// gfx
	public static final SpriteBatch defaultSpriteBatch = new SpriteBatch();
	public static final OrthographicCamera screenCam = new OrthographicCamera(),
			worldCam = new OrthographicCamera(10, 6);

	// ui
	public static final BitmapFont uiFnt = new BitmapFont(
			Gdx.files.internal("data/fonts/naipol.fnt"), false);

	// audio
	public static Music menuMusic;

	// settings
	public static Preferences settings;

	// miscellaneous
	public static final Random random = new Random();
	public static final BitmapFont debugFont = new BitmapFont();
	public static final BitmapFont defaultFont = new BitmapFont();

	// projection/unprojection convenience methods
	public static Vector2 projectToScreen(Vector2 worldCoords) {

		Vector3 temp = new Vector3(worldCoords.x, worldCoords.y, 0);
		worldCam.project(temp);

		worldCoords = new Vector2(temp.x, temp.y);
		return worldCoords;

	}

	public static Vector2 projectToWorld(Vector2 screenCoords) {
		Vector3 temp = new Vector3(screenCoords.x, screenCoords.y, 0);
		worldCam.unproject(temp);

		screenCoords = new Vector2(temp.x, temp.y);
		return screenCoords;
	}
}
