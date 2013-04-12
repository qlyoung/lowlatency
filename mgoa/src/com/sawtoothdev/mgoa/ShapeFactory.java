package com.sawtoothdev.mgoa;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;

public class ShapeFactory {

	public static ChainShape makeCircle(int points, float radius){
		
		//make a circle manually
		ChainShape manualCircle = new ChainShape();
		
		Vector2[] circlePoints = new Vector2[points];
		float theta = 0f;
		
		for (int i = 0; i < points; i++){
			
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
