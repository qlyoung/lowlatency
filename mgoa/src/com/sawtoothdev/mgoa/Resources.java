package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

	// globals and heavyweights that we only need one of
	
	public static Game game;
	public static Difficulty difficulty;
	public static Random random;
	public static SpriteBatch spriteBatch;
	public static OrthographicCamera camera;
	public static Vector2 worldDimensions;
	public static BitmapFont font;
	
	
	// a few useful methods
	
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
