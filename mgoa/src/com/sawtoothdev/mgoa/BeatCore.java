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

public class BeatCore implements IGameObject, Poolable {

	private final Body core;
	private final PointLight light;
	private final Sprite ring;
	
	private boolean complete = false;
	
	public BeatCore(World world, RayHandler handler){
		
		BodyDef coreDef = new BodyDef();
		coreDef.type = BodyType.StaticBody;
		FixtureDef coreFixture = new FixtureDef();
		coreFixture.shape = new CircleShape();
		coreFixture.shape.setRadius(.5f);
		core = world.createBody(coreDef);
		
		light = new PointLight(handler, 500, Color.CYAN, .5f, core.getPosition().x, core.getPosition().y);
		light.attachToBody(core, 0, 0);
		light.setXray(true);
		
		ring = new Sprite(new Texture("data/textures/circ.png"));
	}

	public void activate(){
		light.setActive(true);
	}
	
	@Override
	public void render(float delta) {
		
		if (ring.getScaleX() >= 0)
			ring.scale(-delta / Resources.difficulty.ring_time_secs);
		else
			complete = true;
				
		ring.draw(Resources.spriteBatch);
	}
	
	public void deactivate(){
		light.setActive(false);
	}
	
	@Override
	public void reset() {
		light.setColor(Color.CYAN);
		light.setDistance(.5f);
		
		ring.setScale(1);
		ring.setColor(Color.WHITE);
		
		complete = false;
	}
	
	public void setPosition(Vector2 pos, Camera camera){
		
		core.setTransform(pos, 0f);
		
		Vector2 spritePosition = Resources.projectToScreen(pos, camera);
		ring.setPosition(spritePosition.x - ring.getWidth() / 2f, spritePosition.y - ring.getHeight() / 2f);
		
	}

	public boolean isComplete(){
		return complete;
	}
}
