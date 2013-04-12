package com.sawtoothdev.mgoa;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sawtoothdev.audioanalysis.Beat;

/**
 * Heart of the game, controls gameplay itself.
 * @author albatross
 *
 */

public class PlayScreen implements Screen {
	
	OrthographicCamera camera;
	
	final float circleRadius = 2.0f;
	
	private class WorldManager implements ISongEventListener, IGameObject {
		
		World world;
		Box2DDebugRenderer renderer = new Box2DDebugRenderer();
		Random random = new Random();
		
		ArrayList<Body> orbs = new ArrayList<Body>();
		Body circle;
		
		public WorldManager(){
			
			world = new World(new Vector2(), true);
			
			
			{// create circle
				BodyDef circleDef = new BodyDef();
				circleDef.type = BodyType.KinematicBody;
				circleDef.position.set(0, 0);
				
				FixtureDef circFixture = new FixtureDef();
				circFixture.density = 1f;
				circFixture.friction = 0f;
				
				circFixture.shape = ShapeFactory.makeCircle(26, 2.0f);
				
				circle = world.createBody(circleDef);
				circle.createFixture(circFixture);
			}
			
		}

		@Override
		public void onBeat(Beat b) {
				
			BodyDef circleDef = new BodyDef();
			circleDef.type = BodyType.KinematicBody;
		
			int angle = random.nextInt(361);
			
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
			
			Body circle = world.createBody(circleDef);
			circle.createFixture(circFixture);
			
			circleShape.dispose();
			
			orbs.add(circle);
			
		}

		@Override
		public void render(float delta) {
			
			// update orbs
			for (Body orb : orbs){
				float posX = orb.getPosition().x + (delta * orb.getLinearVelocity().x);
				float posY = orb.getPosition().y + (delta * orb.getLinearVelocity().y);
				orb.setTransform(posX, posY, 0);
			}
			
			// process input
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				circle.setTransform(new Vector2(), circle.getAngle() + delta * Resources.difficulty.player_velocity);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				circle.setTransform(new Vector2(), circle.getAngle() - delta * Resources.difficulty.player_velocity);
			
			// render debug lines
			renderer.render(world, camera.combined);
			
		}
		
		private Vector2 rotate(float angle, Vector2 currentPos, Vector2 centre)
		{
		    double distance = Math.sqrt(Math.pow(currentPos.x - centre.x, 2) + Math.pow(currentPos.y - centre.y, 2));
		    return new Vector2( (float)(distance * Math.cos(angle)), (float)(distance * Math.sin(angle)) ).add(centre);
		}
		
	}
	
	private WorldManager worldManager;
	private SongEngine engine;
	
	public PlayScreen(ArrayList<Beat> beats, FileHandle audioFile) {
		
		worldManager = new WorldManager();
		
		long delayMs = (long) ((circleRadius / Resources.difficulty.beat_velocity) * 1000);
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
		camera.update();
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
