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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sawtoothdev.audioanalysis.Beat;

/**
 * Heart of the game, controls gameplay itself.
 * 
 * @author albatross
 * 
 */

public class PlayScreen implements Screen {

	private class WorldManager implements ISongEventListener, IGameObject {
		
		// settings
		final float LIGHT_DISTANCE = .5f;

		// box2d
		World world;

		// TODO: pool everything
		SpritePool spritePool;
		
		// lights
		RayHandler handler;
		ArrayList<Light> lights = new ArrayList<Light>();
		
		// objects
		ArrayList<Body> cores = new ArrayList<Body>();
		ArrayList<Sprite> rings = new ArrayList<Sprite>();
		

		public WorldManager() {

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

			{// light the cores
				for (Body core : cores) {

					PointLight pLight = new PointLight(handler, 500,
							Color.CYAN, .5f, core.getPosition().x,
							core.getPosition().y);
					pLight.attachToBody(core, 0, 0);
					pLight.setXray(true);
					lights.add(pLight);
				}
			}
			
			{// setup pool
				spritePool = new SpritePool(new Texture("data/textures/circ.png"));
			}
		}

		@Override
		public void render(float delta) {

			{// rings
				
				{// update
					// scale
					for (int i = 0; i < rings.size(); i++) {
						Sprite s = rings.get(i);
						
						s.scale(-delta);
					
						if (s.getScaleX() < 0){
							rings.remove(s);
							spritePool.free(s);
						}
					}
					
				}
				
				{// draw
					Resources.spriteBatch.begin();
					{
						for (Sprite s : rings)
							s.draw(Resources.spriteBatch);
					}
					Resources.spriteBatch.end();
				}

			}
			
			{// lights

				{// update
					for (Light l : lights) {
						if (l.getDistance() > LIGHT_DISTANCE)
							l.setDistance(l.getDistance() - (delta * 3));
					}
				}

				{// draw
					handler.setCombinedMatrix(camera.combined, camera.position.x,
							camera.position.y, camera.viewportWidth * camera.zoom,
							camera.viewportHeight * camera.zoom);
					handler.updateAndRender();
				}


			}
			
		}

		@Override
		public void onBeat(Beat b) {

			{// add new ring if energy high enough
				
				if (b.energy > .2f){
					Body core = cores.get(Resources.random.nextInt(3));
					
					Sprite s = spritePool.obtain();
					
					// gotta translate box2d coords to screen coords because spritebatch uses screen coords
					Vector3 position = new Vector3(core.getPosition().x, core.getPosition().y, 0);
					camera.project(position);
					
					s.setPosition(position.x - s.getWidth() / 2f, position.y - s.getHeight() / 2f);
					
					rings.add(s);
				}
			}

			// pulse all the lights
			for (Light l : lights) {
				l.setDistance(l.getDistance() + b.energy);
			}

		}
	
	}

	// camera
	private final OrthographicCamera camera;

	// engines and managers
	private final WorldManager worldManager;
	private final SongEngine engine;


	
	public PlayScreen(ArrayList<Beat> beats, FileHandle audioFile) {

		worldManager = new WorldManager();

		// long delayMs = (long) ((circleRadius /
		// Resources.difficulty.beat_velocity) * 1000);
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
