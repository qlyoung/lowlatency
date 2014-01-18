package com.sawtoothdev.mgoa;

import java.util.Iterator;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
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
import com.sawtoothdev.mgoa.game.BeatCore;

/**
 * Eye candy for a more...synchronized age.
 * @author snowdrift
 *
 */

public class PrettyLights implements IUpdateable, IDrawable {

	public enum Mode {IDLE, REACT};
	
	private World world = new World(new Vector2(0, 0), false);

	private RayHandler rayHandler;
	
	private final float
		LIGHT_DISTANCE_CAP = 3,
		LIGHT_MIN_DISTANCE = 1,
		LIGHT_SHRINK_RATE = 6,
		LIGHT_LINEAR_DAMPING = .5f;
	private final float
		IDLE_TIMER_MIN = 4,
		IDLE_TIMER_MAX = 8;
	
	float idleTimer;
	
	private Mode mode;

	public PrettyLights(int numLights, Mode mode) {
		this.mode = mode;

		makeWall(5, 0, .25f, 6);
		makeWall(-5, 0, .25f, 6);
		makeWall(0, 3, 10, .25f);
		makeWall(0, -3, 10, .25f);

		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(MGOA.gfx.worldCam.combined);
		
		for (int i = 0; i < numLights; i++)
			addOrb(Color.WHITE, MGOA.util.random.nextFloat() + .5f);
		
		idleTimer = IDLE_TIMER_MIN;

	}

	@Override
	public void update(float delta) {

		world.step(1 / 45f, 6, 2);

		switch (mode){
		case IDLE:
			idleTimer -= delta;
			if (idleTimer <= 0){
				applyRandomImpulseToAllLights(2);
				idleTimer = ((IDLE_TIMER_MAX - IDLE_TIMER_MIN) * MGOA.util.random.nextFloat() + IDLE_TIMER_MIN);
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

	public void react(Beat b) {
		if (mode == Mode.IDLE)
			return;

		// pulse lights
		additivePulse(b.energy * 4);

		// move lights
		//if (b.energy > .3)
			applyRandomImpulseToAllLights(25 * b.energy);
		
		// colorize
		if (BeatCore.getEnergyColor(b.energy) != Color.MAGENTA)
			changeAllColors(BeatCore.getEnergyColor(b.energy));
	}
	private void additivePulse(float distance){
		for (Light l : rayHandler.lightList) {
			float newDistance = l.getDistance() + distance;

			if (newDistance > LIGHT_DISTANCE_CAP)
				l.setDistance(LIGHT_DISTANCE_CAP);
			else
				l.setDistance(newDistance);
		}
	}
	private void applyRandomImpulseToAllLights(float multiplier){
		Iterator<Body> bodies = world.getBodies();

		while (bodies.hasNext()) {
			float min = .03f, max = 1f;

			float xImpulse = ((max - min) * MGOA.util.random.nextFloat() + min)
					* multiplier;
			float yImpulse = ((max - min) * MGOA.util.random.nextFloat() + min)
					* multiplier;

			xImpulse = MGOA.util.random.nextBoolean() ? xImpulse : -xImpulse;
			yImpulse = MGOA.util.random.nextBoolean() ? -yImpulse : yImpulse;

			bodies.next().setLinearVelocity(xImpulse, yImpulse);
		}
	}
	public void changeAllColors(Color color){
		for (Light l : rayHandler.lightList)
			l.setColor(color);
	}
	
	private void addOrb(Color color, float distance) {

		BodyDef orbDef = new BodyDef();

		orbDef.position.set(new Vector2());
		orbDef.type = BodyType.DynamicBody;

		Body orbBody = world.createBody(orbDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(.75f);
		FixtureDef circfix = new FixtureDef();
		circfix.shape = circle;
		circfix.friction = 0f;
		// circfix.restitution = 1f;
		circfix.density = 0f;

		orbBody.createFixture(circfix);
		orbBody.setLinearVelocity(MGOA.util.random.nextFloat() - .5f,
				MGOA.util.random.nextFloat() - .5f);
		orbBody.setLinearDamping(LIGHT_LINEAR_DAMPING);

		PointLight plight = new PointLight(rayHandler, 128, color, distance, 0,
				0);
		plight.attachToBody(orbBody, 0, 0);
		plight.setSoft(true);
		plight.setSoftnessLenght(0);
		plight.setXray(true);
	}
	private void makeWall(float x, float y, float width, float height) {
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

	public void setMode(Mode mode){
		this.mode = mode;
	}
}
