package com.sawtoothdev.mgoa;

import java.util.ArrayList;
import java.util.Iterator;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sawtoothdev.audioanalysis.Beat;

public class PrettyLights implements IDrawableGameObject {

	private World world = new World(new Vector2(0, 0), false);
	private OrthographicCamera camera;
	
	private RayHandler rayHandler;
	private ArrayList<Light> lights = new ArrayList<Light>();
	
	public PrettyLights(OrthographicCamera camera){
		this.camera = camera;
		
		makeWall(5, 0, .25f, 6);
		makeWall(-5, 0, .25f, 6);
		makeWall(0, 3, 10, .25f);
		makeWall(0, -3, 10, .25f);
		
		
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(this.camera.combined);
		
		for(int i = 0; i < 6; i++)
			makeOrb(Color.CYAN, Resources.random.nextFloat() + .5f);
		for(int i = 0; i < 5; i++)
			makeOrb(Color.BLUE, Resources.random.nextFloat() + .5f);
		
	}
	
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		world.step(1/45f, 6, 2);

		for (Light l : lights)
			if (l.getDistance() > 1)
				l.setDistance((float) (l.getDistance() - (delta * 6)));
	}

	@Override
	public void draw(SpriteBatch batch) {
		rayHandler.updateAndRender();

	}

	public void react(Beat b){
		
		for (Light l : lights) {
			if ((l.getDistance() + b.energy * 4) > 3)
				l.setDistance(3);				
			else
				l.setDistance(l.getDistance() + b.energy * 4);
		}
		
		Iterator<Body> bodies = world.getBodies();
		
		while (bodies.hasNext()){
			float xImpulse = Resources.random.nextFloat() * (10 * b.energy);
			float yImpulse = Resources.random.nextFloat() * (10 * b.energy);
			
			xImpulse = Resources.random.nextBoolean() ? xImpulse : -xImpulse;
			yImpulse = Resources.random.nextBoolean() ? -yImpulse : yImpulse;
			
			bodies.next().setLinearVelocity(xImpulse, yImpulse);			
		}
	}
	
	public void makeOrb(Color color, float distance){
		
		BodyDef orbDef = new BodyDef();
		/*
		orbDef.position.set(new Vector2(
				Resources.random.nextInt(20) - 10,
				Resources.random.nextInt(12) - 6));
				*/
		
		orbDef.position.set(new Vector2());
		orbDef.type = BodyType.DynamicBody;
		
		Body orbBody = world.createBody(orbDef);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(.5f);
		FixtureDef circfix = new FixtureDef();
		circfix.shape = circle;
		circfix.friction = 0f;
		//circfix.restitution = 1f;
		circfix.density = 0f;
		
		orbBody.createFixture(circfix);
		
		orbBody.setLinearVelocity(new Vector2(
				Resources.random.nextFloat() - .5f,
				Resources.random.nextFloat() - .5f));
		
		PointLight plight = new PointLight(rayHandler, 128, color, distance, 0, 0);
		plight.attachToBody(orbBody, 0, 0);
		plight.setSoft(true);
		plight.setSoftnessLenght(0);
		plight.setXray(true);
		lights.add(plight);
	}

	private void makeWall(float x, float y, float width, float height){
		BodyDef wallDef = new BodyDef();
		wallDef.type = BodyType.StaticBody;
		wallDef.position.set(new Vector2(x, y));
		
		Body wall = world.createBody(wallDef);
		
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(width / 2f, height / 2f);
		
		FixtureDef wallFixture = new FixtureDef();
		wallFixture.shape = wallShape;
		wallFixture.friction = 0f;
		wallFixture.restitution = 0f;
		
		wall.createFixture(wallFixture);
	}
}
