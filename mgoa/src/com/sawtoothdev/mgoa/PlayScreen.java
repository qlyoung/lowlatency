package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
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
		
		// le pool
		CorePool corePool;
		
		// objects
		ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();
		int index = 0;

		public WorldManager() {
			corePool = new CorePool();
			font.setColor(Color.WHITE);
		}

		@Override
		public void render(float delta) {
			
			// input
			if (Gdx.input.isTouched()){
				
				// translate touch coords to world coords
				Vector2 touchPos = Resources.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()));	
				
				// check collisions
				for (BeatCore core : activeCores){
					
					if (core.getBoundingRectangle().contains(touchPos.x, touchPos.y)){
						String nextAccuracy = core.onHit(engine.getSongTime()).toString();
						if (nextAccuracy != "INACTIVE")
							lastAccuracy = nextAccuracy;
					}
						
				}
			}
			
			
			{// rings
				
				// update
				for (int i = 0; i < activeCores.size(); i++){
					
					BeatCore c = activeCores.get(i);
					
					if (c.isComplete()){
						activeCores.remove(c);
						corePool.free(c);
					}
				}
				
				// draw
				Resources.spriteBatch.begin();
				{
					font.draw(Resources.spriteBatch, lastAccuracy, 50, 50);

					for (BeatCore core : activeCores)
						core.render(delta);
				}
				Resources.spriteBatch.end();
			}
			
		}

		@Override
		public void onBeat(Beat b) {

			if (b.energy > 0f){
				
				float y = Resources.random.nextInt(5) - 2;
				float x = Resources.random.nextInt(9) - 4;					
				
				BeatCore core = corePool.obtain();
				core.setPosition(new Vector2(x, y));
				core.setup(b, Resources.difficulty.ringTimeMs, String.valueOf(index));
			
				index++;
				if (index > 15)
					index = 0;
				
				activeCores.add(core);					
			}

		}
	
	}


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
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Resources.spriteBatch.begin();
		Resources.spriteBatch.draw(background, 0, 0);
		Resources.spriteBatch.end();

		engine.render(delta);
		worldManager.render(delta);

		Resources.camera.update();
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
