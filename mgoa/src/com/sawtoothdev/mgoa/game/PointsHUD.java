package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.MGOA;

public class PointsHUD implements IDrawable, IUpdateable {

	class PointMessage implements Poolable {
		
		public String text;
		public Vector2 target = new Vector2();
		public Vector2 position = new Vector2();
		
		public void set(int value, Vector2 position, Vector2 target){
			this.text = "+" + value;
			this.position.set(position);
			this.target.set(target);
		}
		
		@Override
		public void reset() {
			text = null;
			target.set(0, 0);
			position.set(0, 0);
		}
		
		public float getSlopeToTarget(){
			return (target.y - position.y) / (target.x - position.x);
		}
	}
	
	Pool<PointMessage> pointPool = new Pool<PointMessage>(){

		@Override
		protected PointMessage newObject() {
			return new PointMessage();
		}
		
	};
	ArrayList<PointMessage> activePoints = new ArrayList<PointsHUD.PointMessage>();
	
	@Override
	public void update(float delta) {
		
		for (int i = 0; i < activePoints.size(); i++){
			PointMessage p = activePoints.get(i);
			// if we're within five pixels of our target
			if (Math.abs(p.position.x - p.target.x) < 5 &&
				Math.abs(p.position.y - p.target.y) < 5){
				
				pointPool.free(p);
				activePoints.remove(p);				
			}
			else {
				float xshift = delta * 400;
				float yshift = xshift * p.getSlopeToTarget();
				
				p.position.x += p.position.x < p.target.x ? xshift : -xshift;
				p.position.y += p.position.y < p.target.y ? -yshift : yshift;
			}
		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		for (PointMessage p : activePoints)
			MGOA.ui.uiFnt.draw(batch, p.text, p.position.x, p.position.y);
	}
	
	public void spawnPoints(int value, Vector2 position, Vector2 target){
		PointMessage pm = pointPool.obtain();
		pm.set(value, position, target);
		activePoints.add(pm);
	}

}
