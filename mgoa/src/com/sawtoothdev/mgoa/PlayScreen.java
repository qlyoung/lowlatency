package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.BeatCore.Accuracy;

/**
 * Heart of the game, controls gameplay itself.
 * 
 * @author albatross
 * 
 */

public class PlayScreen implements Screen {

	private class WorldManager implements ISongEventListener, IGameObject {

		// le pool
		CorePool corePool;

		// active object management
		ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();
		
		// index for numbering the beats
		int index = 0;

		
		public WorldManager() {
			corePool = new CorePool();
		}

		@Override
		public void render(float delta) {

			// input
			if (Gdx.input.isTouched()) {

				// translate touch coords to world coords and check collisions
				Vector2 touchPos = Resources.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

				for (BeatCore core : activeCores) {

					if (core.getBoundingRectangle().contains(touchPos.x, touchPos.y) && !core.beenHit()) {
							
						Accuracy accuracy = core.onHit(engine.getSongTime());
							
						if (accuracy != Accuracy.INACTIVE){
							int divisor = Accuracy.values().length;
							
							switch (accuracy) {
							case ALMOST:
								divisor -= 1;
									break;
							case GOOD:
								divisor -= 2;
								break;
							case EXCELLENT:
								divisor -= 3;
								break;
							case PERFECT:
								divisor -= 4;
								break;
							case STELLAR:
								divisor -= 5;
								break;
							default:
								break;
							}
							
							int scoreValue = (int) core.getScoreValue() / divisor;
							
							hud.actuateHitEvent(accuracy, scoreValue);
						}
					}
				}
			}

			{// rings

				// update
				for (int i = 0; i < activeCores.size(); i++) {

					BeatCore c = activeCores.get(i);

					if (c.isComplete()) {
						activeCores.remove(c);
						corePool.free(c);
						hud.incrementTotalBeatsShown();
					}
				}

				// draw
				for (BeatCore core : activeCores)
					core.render(delta);
			}

		}

		@Override
		public void onBeatWarning(Beat b) {

			if (b.energy > 0f) {

				BeatCore core = corePool.obtain();
				core.setup(b, String.valueOf(index));

				Vector2 position = new Vector2();

				// place beat at empty position
				if (activeCores.size() > 0) {

					boolean clean = false;

					//find a clean position
					while (!clean) {

						float y = Resources.random.nextInt(5) - 2;
						float x = Resources.random.nextInt(9) - 4;
						position.set(x, y);

						for (BeatCore c : activeCores) {
							clean = !(c.getPosition().x == position.x && c.getPosition().y == position.y);
							if (!clean)
								break;
						}
					}
					
				} else {
					float y = Resources.random.nextInt(5) - 2;
					float x = Resources.random.nextInt(9) - 4;
					position.set(x, y);
				}

				core.setPosition(position);

				index++;
				if (index > 15)
					index = 0;

				activeCores.add(core);
			}
			
		}
		@Override
		public void onBeat(Beat b) {
			
		}

	}

	// engines and managers
	private final SongEngine engine;
	private final WorldManager worldManager;
	private final HUD hud;

	public PlayScreen(BeatMap map, FileHandle audioFile) {

		// set up the world
		worldManager = new WorldManager();

		// set up the heads up display
		hud = new HUD(audioFile);

		// load the map
		engine = new SongEngine(map, audioFile);
		engine.addListener(worldManager);
	}

	@Override
	public void render(float delta) {

		// clear the screen and update the camera
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Resources.camera.update();

		//update and draw
		Resources.spriteBatch.begin();
		{
			engine.render(delta);
			worldManager.render(delta);
		}
		Resources.spriteBatch.end();
		
		hud.render(delta);

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
