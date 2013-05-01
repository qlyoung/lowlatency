package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
		
		BitmapFont font = new BitmapFont();
		String lastAccuracy = "READY";
		
		// box2d
		World world;

		// le pool
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
			
			{// handle input
				
				if (Gdx.input.isTouched()){
					
					// translate touch coords to world coords
					Vector2 touchPos = Resources.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()), camera);
					
					// check collisions
					for (BeatCore core : activeCores){
						
						if (core.getBoundingRectangle().contains(touchPos.x, touchPos.y)){
							String nextAccuracy = core.onHit(engine.getSongTime()).toString();
							if (nextAccuracy != "INACTIVE")
								lastAccuracy = nextAccuracy;
						}
							
					}
				}
			}
			

			{// rings
				
				{// update
					for (int i = 0; i < activeCores.size(); i++){
						BeatCore c = activeCores.get(i);
						if (c.isComplete()){
							activeCores.remove(c);
							corePool.free(c);
						}
					}
				}
				
				Resources.spriteBatch.begin();
				
				font.setColor(Color.WHITE);
				font.draw(Resources.spriteBatch, lastAccuracy, 50, 50);
				
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
					
					float y = Resources.random.nextInt(5) - 2;
					float x = Resources.random.nextInt(9) - 4;
					
					
					BeatCore core = corePool.obtain();
					core.setPosition(new Vector2(x, y), camera);
					core.setup(b, Resources.difficulty.ringTimeMs);
					
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
	
	// misc
	Texture background = new Texture("data/textures/bg.png");

	
	public PlayScreen(BeatMap map, FileHandle audioFile) {

		worldManager = new WorldManager();
		
		ArrayList<Beat> bMap;
		
		switch (Resources.difficulty.name){
		case EASY:
			bMap = map.easy;
			break;
		case NORMAL:
			bMap = map.medium;
			break;
		default:
		case HARD:
		case HARDPLUS:
			bMap = map.hard;
		}
			
		engine = new SongEngine(bMap, Resources.difficulty.ringTimeMs, audioFile);

		engine.addListener(worldManager);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Resources.worldDimensions.x, Resources.worldDimensions.y);
		camera.position.set(0, 0, 0);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Resources.spriteBatch.begin();
		Resources.spriteBatch.draw(background, 0, 0);
		Resources.spriteBatch.end();

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
