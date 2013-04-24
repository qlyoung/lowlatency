package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
		ArrayList<Body> cores = new ArrayList<Body>();
		ArrayList<Sprite> circleSprites = new ArrayList<Sprite>();

		
		public WorldManager(){
			
			world = new World(new Vector2(), true);
			
			{// set up lighting
				handler = new RayHandler(world);
				handler.setAmbientLight(Color.WHITE);
			}
			
			{// make three cores
				BodyDef coreDef = new BodyDef();
				coreDef.type = BodyType.StaticBody;
				
				FixtureDef coreFixture = new FixtureDef();
				coreFixture.shape = new CircleShape();
				coreFixture.shape.setRadius(.5f);
				
				Body core1 = world.createBody(coreDef);
				core1.setTransform(new Vector2(1, -1), 0);
				
				Body core2 = world.createBody(coreDef);
				core2.setTransform(new Vector2(-1, -1), 0);
				
				Body core3 = world.createBody(coreDef);
				core3.setTransform(new Vector2(0, 1f), 0);
				
				cores.add(core1);
				cores.add(core2);
				cores.add(core3);
				
			}
			
			{// light it up
				for (Body core : cores){
					
					PointLight pLight = new PointLight(handler, 500, Color.WHITE, .5f, core.getPosition().x, core.getPosition().y);
					pLight.attachToBody(core, 0, 0);
					pLight.setXray(true);
					lights.add(pLight);
				}
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
			
			{// render lights
				
				// decrease light distance
				for (Light l : lights){
					if (l.getDistance() > LIGHT_DISTANCE)
						l.setDistance(l.getDistance() - (delta * 3));
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
			
			spriteBatch.begin();
			{
				// shrink and draw rings
				for (int i = 0; i < circleSprites.size(); i++) {
					Sprite s = circleSprites.get(i);
					
					s.scale(-delta);
					
					if (s.getScaleX() < 0)
						circleSprites.remove(s);
				}
				
				for (Sprite s : circleSprites){
					s.draw(spriteBatch);
				}
				
			}
			spriteBatch.end();
			
			// render debug lines
			renderer.render(world, camera.combined);
		}
		
		@Override
		public void onBeat(Beat b) {
			
			//add circle sprite if high energy\
			//int core = Resources.random.nextInt(3);
			
			if (b.energy > .25){
				Sprite s = new Sprite(new Texture("data/textures/circ.png"));
				circleSprites.add(s);
				s.setPosition(400 - s.getWidth() / 2f, 320 - s.getHeight() / 2f);
			}
			
			//pulse all the lights
			for (Light l : lights){
				l.setDistance(l.getDistance() + b.energy);
			}
			
		}
	}
	
	
	// camera
	private final OrthographicCamera camera;
	
	// engines and managers
	private final WorldManager worldManager;
	private final SongEngine engine;
	
	// misc
	private SpriteBatch spriteBatch = new SpriteBatch();
	
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
