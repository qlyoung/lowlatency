package com.sawtoothdev.mgoa;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;

public class ShapeFactory {

	public static ChainShape makeCircle(int points, float radius, int gap){
		
		//make a circle manually
		
		ChainShape manualCircle = new ChainShape();
		
		Vector2[] circlePoints = new Vector2[points - gap + 1];
		float theta = 0f;
		
		for (int i = 0; i < points - gap + 1; i++){
			
			theta = (float) (Math.PI * (i / (points / 2f)));
			
			float x = (float) (0 + radius * Math.cos(theta));
			float y = (float) (0 + radius * Math.sin(theta));
			
			Vector2 point = new Vector2(x, y);
			circlePoints[i] = point;
		}
		
		manualCircle.createChain(circlePoints);
		
		return manualCircle;
	}
	
}
