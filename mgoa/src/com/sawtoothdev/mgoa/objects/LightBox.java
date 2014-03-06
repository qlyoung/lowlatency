package com.sawtoothdev.mgoa.objects;

import java.util.Iterator;
import java.util.Random;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Disposable;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;

/**
 * Eye candy for a more...synchronized age.
 * @author snowdrift
 *
 */

public class LightBox implements IUpdateable, IDrawable, Disposable {

	public enum Mode {IDLE, REACT};
	
	World world = new World(new Vector2(0, 0), false);
	OrthographicCamera cam = new OrthographicCamera(10, 6);
	RayHandler rayHandler;
	Random random;
	Mode mode;
	float idleTimer;
	
	private final float
		LIGHT_DISTANCE_CAP = 3,
		LIGHT_MIN_DISTANCE = 1,
		LIGHT_SHRINK_RATE = 6,
		LIGHT_LINEAR_DAMPING = .5f,
		LIGHT_BODY_RADIUS = .1f,
		IDLE_TIMER_MIN = 8,
		IDLE_TIMER_MAX = 10;
	

	public LightBox(Mode mode) {
		this.mode = mode;
		this.random = new Random();

		spawnWall(5, 0, .25f, 6);
		spawnWall(-5, 0, .25f, 6);
		spawnWall(0, 3, 10, .25f);
		spawnWall(0, -3, 10, .25f);

		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(cam.combined);
		
		idleTimer = IDLE_TIMER_MIN;
	}

	@Override
	public void update(float delta) {

		world.step(1 / 45f, 6, 2);

		switch (mode){
		case IDLE:
			idleTimer -= delta;
			if (idleTimer <= 0 || Gdx.input.justTouched()){
				jerkLights(2);
				idleTimer = ((IDLE_TIMER_MAX - IDLE_TIMER_MIN) * random.nextFloat() + IDLE_TIMER_MIN);
			}
			break;
		case REACT:
			for (Light l : rayHandler.lightList) {
				if (l.getDistance() > LIGHT_MIN_DISTANCE){
					float newDistance = l.getDistance() - (delta * LIGHT_SHRINK_RATE);
					l.setDistance(newDistance);
				}
			}
			break;
		}

	}
	@Override
	public void draw(SpriteBatch batch) {
		rayHandler.updateAndRender();
	}

	private void spawnWall(float x, float y, float width, float height) {
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
	public void addLight(Color color, float size){
		BodyDef orbDef = new BodyDef();

		orbDef.position.set(new Vector2());
		orbDef.type = BodyType.DynamicBody;

		Body orbBody = world.createBody(orbDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(LIGHT_BODY_RADIUS);
		FixtureDef circfix = new FixtureDef();
		circfix.shape = circle;
		circfix.friction = 0f;
		// circfix.restitution = 1f;
		circfix.density = 0f;

		orbBody.createFixture(circfix);
		orbBody.setLinearVelocity(random.nextFloat() - .5f,
				random.nextFloat() - .5f);
		orbBody.setLinearDamping(LIGHT_LINEAR_DAMPING);

		PointLight plight = new PointLight(rayHandler, 128, color, size, 0,	0);
		plight.attachToBody(orbBody, 0, 0);
		plight.setSoft(true);
		plight.setSoftnessLenght(0);
		plight.setXray(true);
	}
	public void removeLight(){
		
	}	
	public void pulseLights(float force){
		for (Light l : rayHandler.lightList) {
			float newDistance = l.getDistance() + force;

			if (newDistance > LIGHT_DISTANCE_CAP)
				l.setDistance(LIGHT_DISTANCE_CAP);
			else
				l.setDistance(newDistance);
		}
	}
	public void jerkLights(float force){
		
		Iterator<Body> bodies = world.getBodies();
		while (bodies.hasNext()) {
			float min = .03f, max = 1f;

			float randomx = ((max - min) * random.nextFloat() + min);
			float randomy = ((max - min) * random.nextFloat() + min);

			randomx *= force;
			randomy *= force;
			
			randomx = random.nextBoolean() ? randomx : -randomx;
			randomy = random.nextBoolean() ? randomy : -randomy;
			
			bodies.next().setLinearVelocity(randomx, randomy);
		}
	}
	public void setAllLightsColor(Color color){
		for (Light l : rayHandler.lightList)
			l.setColor(color);
	}
	public void setAllLightsRandomColor(){
		float r = random.nextFloat();
		float g = random.nextFloat();
		float b = random.nextFloat();
		Color c = new Color(r, g, b, 1);
		setAllLightsColor(c);
	}
	public void setMode(Mode mode){
		this.mode = mode;
	}
	public void setGravity(Vector2 gravity){
		world.setGravity(gravity);
	}
	
	@Override
	public void dispose() {
		world.dispose();
		rayHandler.dispose();
	}
}
