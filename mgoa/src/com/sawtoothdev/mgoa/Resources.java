package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Some global resources, grouped in one place for convenience
 * @author albatross
 *
 */

public class Resources {

	// globals and heavyweights that we only need one of
	
	public static Game game;
	public static Difficulty difficulty;
	public static Random random = new Random();
	public static SpriteBatch spriteBatch = new SpriteBatch();
	public static OrthographicCamera camera = new OrthographicCamera();
	public static Vector2 worldDimensions = new Vector2(10, 6);
	
	
	// CONVENIENCE METHODS
	
	// rotates a given position vector to the given angle around the given centre
	public static Vector2 rotate(float angle, Vector2 currentPos, Vector2 centre)
	{
	    double distance = Math.sqrt(Math.pow(currentPos.x - centre.x, 2) + Math.pow(currentPos.y - centre.y, 2));
	    return new Vector2( (float)(distance * Math.cos(angle)), (float)(distance * Math.sin(angle)) ).add(centre);
	}
	// projects coordinates from world space to screen space
	public static Vector2 projectToScreen(Vector2 worldCoords){
		
		Vector3 temp = new Vector3(worldCoords.x, worldCoords.y, 0);
		camera.project(temp);
		
		worldCoords = new Vector2(temp.x, temp.y);
		return worldCoords;
		
	}
	// projects coordinates from screen space to world space
	public static Vector2 projectToWorld(Vector2 screenCoords){
		Vector3 temp = new Vector3(screenCoords.x, screenCoords.y, 0);
		camera.unproject(temp);
		
		screenCoords = new Vector2(temp.x, temp.y);
		return screenCoords;
	}
}
