package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * useful functions used all through the codebase
 * DRY
 * @author snowdrift
 *
 */
public class Utilities {

	public static Vector2 screenToWorld(Vector2 screenCoords, Camera worldCam) {
		Vector3 v3 = new Vector3(screenCoords.x, screenCoords.y, 0);
		worldCam.unproject(v3);
		Vector2 v2 = new Vector2(v3.x, v3.y);
		return v2;
	}

	public static Vector2 worldToScreen(Vector2 worldCoords, Camera worldCam) {
		Vector3 v3 = new Vector3(worldCoords.x, worldCoords.y, 0);
		worldCam.project(v3);
		Vector2 v2 = new Vector2(v3.x, v3.y);
		return v2;
	}
	
	public static Vector2 getTouchInWorld(Camera worldcam){
		Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		return screenToWorld(screenTouch, worldcam);
	}
	
	public static Color getRandomColor(){
		float r = (float) Math.random();
		float g = (float) Math.random();
		float b = (float) Math.random();
		return new Color(r, g, b, 1);
	}
}
