package com.sawtoothdev.mgoa;

import java.util.ArrayList;
import java.util.Random;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sawtoothdev.audioanalysis.Beat;

/**
 * Heart of the game, controls gameplay itself.
 * @author albatross
 *
 */

public class PlayScreen implements Screen {
	
	private class WorldManager implements ISongEventListener, IGameObject {

		//TODO: pool lights and bodies
		
		// settings
		private final float LIGHT_DISTANCE = .5f;
		
		// box2d
		private final World world;
		private final Box2DDebugRenderer renderer = new Box2DDebugRenderer();
		
		// lights
		RayHandler handler;
		
		// collections
		ArrayList<Body> orbs = new ArrayList<Body>();
		ArrayList<Light> lights = new ArrayList<Light>();
		
		// player
		Body playerCircle;
		
		public WorldManager(){
			
			world = new World(new Vector2(), true);
			
			{// set up lighting
				handler = new RayHandler(world);
				handler.setAmbientLight(Color.WHITE);
			}
			
			{// create circle
				BodyDef circleDef = new BodyDef();
				circleDef.type = BodyType.KinematicBody;
				circleDef.position.set(0, 0);
				
				FixtureDef circFixture = new FixtureDef();
				circFixture.density = 1f;
				circFixture.friction = 0f;
				
				circFixture.shape = ShapeFactory.makeCircle(26, circleRadius, 3);
				
				playerCircle = world.createBody(circleDef);
				playerCircle.createFixture(circFixture);
				
				// put a light in the middle
				PointLight light = new PointLight(handler, 500, Color.MAGENTA, LIGHT_DISTANCE * 2f, playerCircle.getPosition().x, playerCircle.getPosition().y);
				light.setXray(true);
				lights.add(light);
				
				light.attachToBody(playerCircle, 0, 0);
			}
			
		}

		@Override
		public void render(float delta) {
			
			// render orbs
			for (Body orb : orbs){
				float posX = orb.getPosition().x + (delta * orb.getLinearVelocity().x);
				float posY = orb.getPosition().y + (delta * orb.getLinearVelocity().y);
				orb.setTransform(posX, posY, 0);
			}
			
			{// render player circle
				if (Gdx.input.isKeyPressed(Keys.LEFT))
					playerCircle.setTransform(new Vector2(), playerCircle.getAngle() + delta * Resources.difficulty.player_velocity);
				if (Gdx.input.isKeyPressed(Keys.RIGHT))
					playerCircle.setTransform(new Vector2(), playerCircle.getAngle() - delta * Resources.difficulty.player_velocity);
			}
			
			{// spin camera
				camera.rotate(delta * 120);
				camera.update();
			}
			
			{// render lights
				
				// decrease light distance
				for (Light l : lights){
					if (l.getDistance() > LIGHT_DISTANCE)
						l.setDistance(l.getDistance() - (delta * 2));
				}
				
				// update the handler's render matrix
				handler.setCombinedMatrix(
						camera.combined,
						camera.position.x,
						camera.position.y,
						camera.viewportWidth * camera.zoom,
						camera.viewportHeight * camera.zoom);
				
				// render
				handler.updateAndRender();
			}
			
			// render debug lines
			renderer.render(world, camera.combined);
		}
		
		@Override
		public void onBeat(Beat b) {
			
			//make a new orb
			BodyDef circleDef = new BodyDef();
			circleDef.type = BodyType.KinematicBody;
		
			int angle = Resources.random.nextInt(361);
			
			Vector2 velocity = new Vector2(
					(float) Math.sin(angle) * Resources.difficulty.beat_velocity,
					(float) Math.cos(angle) * Resources.difficulty.beat_velocity);
			
			circleDef.linearVelocity.set(velocity);
			circleDef.position.set(0, 0);
			
			FixtureDef circFixture = new FixtureDef();
			circFixture.density = 1f;
			circFixture.friction = 0f;
			
			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(.1f);
			
			circFixture.shape = circleShape;
			
			Body orb = world.createBody(circleDef);
			orb.createFixture(circFixture);
			
			circleShape.dispose();
			
			orbs.add(orb);
			
			
			//light it up
			PointLight light = new PointLight(handler, 500, Color.CYAN, LIGHT_DISTANCE, orb.getPosition().x, orb.getPosition().y);
			light.setXray(true);
			light.attachToBody(orb, 0, 0);
			lights.add(light);
			
			//pulse all the lights
			for (Light l : lights){
				l.setDistance(l.getDistance() + b.energy);
			}
		}
	}
	
	
	// settings
	private final float circleRadius = 2.0f;
	
	// camera
	private final OrthographicCamera camera;
	
	// engines and managers
	private final WorldManager worldManager;
	private final SongEngine engine;
	
	public PlayScreen(ArrayList<Beat> beats, FileHandle audioFile) {
		
		worldManager = new WorldManager();
		
		//long delayMs = (long) ((circleRadius / Resources.difficulty.beat_velocity) * 1000);
		engine = new SongEngine(beats, 0, audioFile);

		engine.addListener(worldManager);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10, 6);
		camera.position.set(0, 0, 0);
	}

	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		engine.render(delta);
		worldManager.render(delta);
	}
	

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		engine.start();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}


}
