package com.sawtoothdev.mgoa;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.sawtoothdev.audioanalysis.Beat;

public class BeatCore implements IGameObject, Poolable {

	public static enum Accuracy { STELLAR, PERFECT, EXCELLENT, GOOD, ALMOST, MISS, INACTIVE };
	
	private final float LIGHT_DISTANCE = .5f;
	
	private final Body core;
	private final PointLight light;
	private final Sprite ring;
	
	private Beat beat;
	
	private boolean complete = false;
	
	
	public BeatCore(World world, RayHandler handler){
		
		BodyDef coreDef = new BodyDef();
		coreDef.type = BodyType.StaticBody;
		FixtureDef coreFixture = new FixtureDef();
		coreFixture.shape = new CircleShape();
		coreFixture.shape.setRadius(.5f);
		core = world.createBody(coreDef);
		
		light = new PointLight(handler, 500, Color.CYAN, LIGHT_DISTANCE, core.getPosition().x, core.getPosition().y);
		light.attachToBody(core, 0, 0);
		//light.setXray(true);
		
		ring = new Sprite(new Texture("data/textures/circ.png"));
	}

	// lifecycle
	public void setup(Beat beat){
		this.beat = beat;
	}
	
	public void activate(){
		light.setActive(true);
	}
	
	@Override
	public void render(float delta) {
		
		if (ring.getScaleX() >= 0){
			if (ring.getScaleX() < .3f)
				light.setColor(Color.RED);
			ring.scale(-delta  / (Resources.difficulty.ringTimeMs / 1000));
		}
		else
			complete = true;
		
		if (light.getDistance() > LIGHT_DISTANCE)
			light.setDistance(light.getDistance() - delta * 2);
				
		ring.draw(Resources.spriteBatch);
	}
	
	public void deactivate(){
		light.setActive(false);
	}
	
	@Override
	public void reset() {
		light.setColor(Color.CYAN);
		
		ring.setScale(1);
		ring.setColor(Color.WHITE);
		
		complete = false;
	}
	
	
	// modifiers
	public void setPosition(Vector2 pos, Camera camera){
		
		core.setTransform(pos, 0f);
		
		Vector2 spritePosition = Resources.projectToScreen(pos, camera);
		ring.setPosition(spritePosition.x - ring.getWidth() / 2f, spritePosition.y - ring.getHeight() / 2f);
		
	}

	public void pulse(float distance){
		light.setDistance(light.getDistance() + distance);
	}

	public Accuracy onHit(long songTimeMs){
		long diff = songTimeMs - beat.timeMs;
		
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
}
