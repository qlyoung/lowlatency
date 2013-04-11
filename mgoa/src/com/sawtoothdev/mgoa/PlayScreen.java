package com.sawtoothdev.mgoa;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.sawtoothdev.audioanalysis.Beat;

/**
 * Heart of the game, controls gameplay itself.
 * @author albatross
 *
 */

public class PlayScreen implements Screen, SongEventListener {
	
	OrthographicCamera camera;
	
	private class WorldManager implements SongEventListener, GameObject {
		
		World world;
		Box2DDebugRenderer renderer = new Box2DDebugRenderer();
		Random random = new Random();
		
		ArrayList<Body> orbs = new ArrayList<Body>();
		Body player;
		
		public WorldManager(){
			
			world = new World(new Vector2(), true);
			
			{// create circle
				BodyDef circleDef = new BodyDef();
				circleDef.type = BodyType.StaticBody;
				circleDef.position.set(0, 0);
				
				FixtureDef circFixture = new FixtureDef();
				circFixture.density = 1f;
				circFixture.friction = 0f;
				
				CircleShape circleShape = new CircleShape();
				circleShape.setRadius(2.5f);
				
				circFixture.shape = circleShape;
				
				Body circle = world.createBody(circleDef);
				circle.createFixture(circFixture);
				
				circleShape.dispose();
				
			}
			
			{//create player
				BodyDef playerDef = new BodyDef();
				playerDef.type = BodyType.KinematicBody;
				
				FixtureDef playerFixture = new FixtureDef();
				playerFixture.density = 1f;
				playerFixture.friction = 0f;
				
				PolygonShape triangle = new PolygonShape();
				triangle.set( new float[] {0, 0, .3f, 0, 0.15f, .3f});
				
				playerFixture.shape = triangle;
				
				player = world.createBody(playerDef);
				player.createFixture(playerFixture);
				
				player.setTransform(0, 2.5f, 0);
				
				triangle.dispose();
				
			}
			
		}

		@Override
		public void onBeat(Beat b) {
			{// create circle
				BodyDef circleDef = new BodyDef();
				circleDef.type = BodyType.KinematicBody;
				
				int angle = random.nextInt(361);
				Vector2 velocity = new Vector2((float) Math.sin(angle) * 5f, (float) Math.cos(angle) * 5f);
				
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
		}

		@Override
		public void render(float delta) {
			
			for (Body orb : orbs){
				float posX = orb.getPosition().x + (delta * orb.getLinearVelocity().x);
				float posY = orb.getPosition().y + (delta * orb.getLinearVelocity().y);
				
				orb.setTransform(posX, posY, 0);
			}
			
			//rotate player
			player.setTransform(player.getPosition(), (float) (Math.toRadians(180)));
			
			renderer.render(world, camera.combined);
		}
		
	}
	
	private WorldManager worldManager;
	private SongEngine engine;
	
	public PlayScreen(ArrayList<Beat> beats, FileHandle audioFile) {
		
		worldManager = new WorldManager();
		engine = new SongEngine(beats, 0, audioFile);
		engine.addListener(worldManager);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10, 6);
		camera.position.set(0, 0, 0);
	}

	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();

		engine.render(delta);
		worldManager.render(delta);
	}
	
	
	@Override
	public void onBeat(Beat b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		engine.start();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}


}
