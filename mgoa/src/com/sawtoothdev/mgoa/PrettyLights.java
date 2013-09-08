package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sawtoothdev.audioanalysis.Beat;

public class PrettyLights implements IDrawableGameObject {

	private World world = new World(new Vector2(), false);
	private OrthographicCamera camera;
	
	private RayHandler rayHandler;
	
	private ArrayList<Light> lights = new ArrayList<Light>();
	
	public PrettyLights(OrthographicCamera camera){
		this.camera = camera;
		
		BodyDef orb = new BodyDef();
		orb.position.set(new Vector2(0, 0));
		orb.type = BodyType.DynamicBody;
		
		Body orbBody = world.createBody(orb);
		
		CircleShape circle = new CircleShape();
		FixtureDef circfix = new FixtureDef();
		circfix.shape = circle;
		circfix.friction = 0f;
		circfix.restitution = 1f;
		orbBody.createFixture(circfix);
		
		orbBody.setLinearVelocity(new Vector2(.5f, .3f));
		
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(this.camera.combined);
		
		lights.add(new PointLight(rayHandler, 128, Color.BLUE, .5f, 0, 0));
		lights.get(0).attachToBody(orbBody, 0, 0);
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		world.step(1/45f, 6, 2);

		for (Light l : lights)
			if (l.getDistance() > 1)
				l.setDistance((float) (l.getDistance() - (delta * 5)));
	}

	@Override
	public void draw(SpriteBatch batch) {
		rayHandler.updateAndRender();

	}

	public void react(Beat b){
		for (Light l : lights)
			l.setDistance(l.getDistance() + b.energy * 5);
	}
}
