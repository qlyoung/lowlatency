package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
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
		
		// box2d
		World world;

		//TODO: Pool everything
		CorePool corePool;
		
		// lights
		RayHandler handler;
		
		// objects
		ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();

		public WorldManager() {

			world = new World(new Vector2(), true);

			{// set up lighting
				handler = new RayHandler(world);
				handler.setAmbientLight(Color.WHITE);
			}
			
			corePool = new CorePool(world, handler);
			
		}

		@Override
		public void render(float delta) {

			{// rings
				
				{// update
					for (int i = 0; i < activeCores.size(); i++){
						BeatCore c = activeCores.get(i);
						if (c.isComplete()){
							c.deactivate();
							activeCores.remove(c);
							corePool.free(c);
						}
					}
				}
				
				Resources.spriteBatch.begin();
				
				for (BeatCore core : activeCores)
					core.render(delta);
				
				Resources.spriteBatch.end();
			}
			
			{// lights

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
				
				if (b.energy > 0f){
					
					BeatCore core = corePool.obtain();
					Vector2 position = new Vector2(Resources.random.nextInt(9), Resources.random.nextInt(5));
					position.x -= 4;
					position.y -= 2;
					core.setPosition(position, camera);
					core.activate();
					
					activeCores.add(core);
					
				}
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

		engine = new SongEngine(beats, (long) (Resources.difficulty.ring_time_secs * 1000f), audioFile);

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
