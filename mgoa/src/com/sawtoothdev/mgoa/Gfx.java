package com.sawtoothdev.mgoa;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sawtoothdev.mgoa.objects.PrettyLights;

public class Gfx {
	
	public final SpriteBatch sysSB;
	public final OrthographicCamera screenCam, worldCam;
	public PrettyLights lights;
	
	public Gfx(){
		sysSB = new SpriteBatch();
		screenCam = new OrthographicCamera();
		worldCam = new OrthographicCamera(10, 6);
		lights = new PrettyLights(4, PrettyLights.Mode.IDLE, worldCam);
		
		screenCam.setToOrtho(false);
	}
	
	public Vector2 projectToScreen(Vector2 worldCoordinates) {

		Vector3 temp = new Vector3(worldCoordinates.x, worldCoordinates.y, 0);
		worldCam.project(temp);

		worldCoordinates = new Vector2(temp.x, temp.y);
		return worldCoordinates;

	}
	public Vector2 projectToWorld(Vector2 screenCoordinates) {
		Vector3 temp = new Vector3(screenCoordinates.x, screenCoordinates.y, 0);
		worldCam.unproject(temp);

		screenCoordinates = new Vector2(temp.x, temp.y);
		return screenCoordinates;
	}
}
