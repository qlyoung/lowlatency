package com.sawtoothdev.mgoa;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Utilities {

	private final Utilities instance;
	
	private Utilities(){
		instance = this;
	}
	
	public Utilities getInstance(){
		if (instance != null)
			return instance;
		else
			return new Utilities();
	}
	
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
}
