package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.MGOA;

public class PointsHUD implements IDrawable, IUpdateable {

	ArrayList<PointMessage> points = new ArrayList<PointMessage>();
	
	private class PointMessage {
		
		public final String text;
		public final Vector2 target;
		public Vector2 position;
		
		public PointMessage(int value, Vector2 position, Vector2 target){
			this.text = "+" + value;
			this.position = position;
			this.target = target;
		}
		
		public float getSlopeToTarget(){
			return (target.y - position.y) / (target.x - position.x);
		}
	}
	
	@Override
	public void update(float delta) {
		
		for (int i = 0; i < points.size(); i++){
			PointMessage p = points.get(i);
			// if we're within five pixels of our target
			if (Math.abs(p.position.x - p.target.x) < 5 &&
				Math.abs(p.position.y - p.target.y) < 5)
				points.remove(p);
			else {
				float xshift = delta * 200;
				float yshift = xshift * p.getSlopeToTarget();
				
				p.position.x += p.position.x < p.target.x ? xshift : -xshift;
				p.position.y += p.position.y < p.target.y ? -yshift : yshift;
			}
		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		for (PointMessage p : points)
			MGOA.ui.uiFnt.draw(batch, p.text, p.position.x, p.position.y);
	}
	
	public void spawnPoints(int value, Vector2 position, Vector2 target){
		points.add(new PointMessage(value, position, target));
	}

}
