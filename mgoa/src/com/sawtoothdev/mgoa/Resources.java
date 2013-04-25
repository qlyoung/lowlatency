package com.sawtoothdev.mgoa;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Some global resources, grouped in one place for convenience
 * @author albatross
 *
 */

public class Resources {

	public static Game game;
	public static Difficulty difficulty;
	public static Random random = new Random();
	public static SpriteBatch spriteBatch = new SpriteBatch();
	
	// rotates a given position vector to the given angle around the given centre
	public static Vector2 rotate(float angle, Vector2 currentPos, Vector2 centre)
	{
	    double distance = Math.sqrt(Math.pow(currentPos.x - centre.x, 2) + Math.pow(currentPos.y - centre.y, 2));
	    return new Vector2( (float)(distance * Math.cos(angle)), (float)(distance * Math.sin(angle)) ).add(centre);
	}
	
}
