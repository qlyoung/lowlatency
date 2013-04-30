package com.sawtoothdev.mgoa;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sawtoothdev.audioanalysis.Beat;

public class BeatCore implements IGameObject, Poolable {

	public static enum Accuracy { STELLAR, PERFECT, EXCELLENT, GOOD, ALMOST, MISS, INACTIVE };
	
	private final Sprite ring;
	private Vector2 position;
	
	private Beat beat;
	
	private boolean complete = false;
	
	
	public BeatCore(World world, RayHandler handler){		
		ring = new Sprite(new Texture("data/textures/circ.png"));
	}

	// lifecycle
	public void setup(Beat beat){
		this.beat = beat;
	}
	
	@Override
	public void render(float delta) {
		
		if (ring.getScaleX() >= 0)
			ring.scale(-delta  / (Resources.difficulty.ringTimeMs / 1000));
		else
			complete = true;
		
		
				
		ring.draw(Resources.spriteBatch);
	}
	
	@Override
	public void reset() {
		
		ring.setScale(1);
		ring.setColor(Color.WHITE);
		
		complete = false;
	}
	
	
	// modifiers
	public void setPosition(Vector2 worldPos, Camera camera){

		this.position = worldPos;
		
		Vector2 spritePosition = Resources.projectToScreen(worldPos, camera);
		ring.setPosition(spritePosition.x - ring.getWidth() / 2f, spritePosition.y - ring.getHeight() / 2f);
	}

	public Accuracy onHit(long songTimeMs){
		long diff = songTimeMs - beat.timeMs;
		
		Gdx.app.log("diff", String.valueOf(diff));
		
		if (diff < -300)
			return Accuracy.INACTIVE;
		else if (diff < -210)
			return Accuracy.ALMOST;
		else if (diff < -150)
			return Accuracy.GOOD;
		else if (diff < -90)
			return Accuracy.EXCELLENT;
		else if (diff < -30)
			return Accuracy.PERFECT;
		else if (diff < 40)
			return Accuracy.STELLAR;
		else
			return Accuracy.MISS;
		
	}
	
	// readers
	public boolean isComplete(){
		return complete;
	}

	public Rectangle getBoundingRectangle(){
		
		return new Rectangle(position.x - .25f, position.y - .25f, .5f, .5f);
		
	}
}
