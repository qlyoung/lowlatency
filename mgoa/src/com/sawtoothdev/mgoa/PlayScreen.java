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

	// engines and managers
	private final SongEngine engine;
	private final WorldManager worldManager;
	private final HUD hud;
	private final EffectMaker fx;

	public PlayScreen(BeatMap map, FileHandle audioFile) {

		worldManager = new WorldManager();
		hud = new HUD(audioFile);
		fx = new EffectMaker();

		engine = new SongEngine(map, audioFile);
		engine.addListener(worldManager);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Resources.camera.update();

		engine.render(delta);
		
		fx.render(delta);
		worldManager.render(delta);
		hud.render(delta);
		
		
		if (engine.isDone()){
			Resources.game.setScreen(Resources.menuScreen);
		}
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

	private class WorldManager implements ISongEventListener, IGameObject {

		private CorePool corePool = new CorePool();
		private ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();
		
		private int combo = 0;
		private int totalBeatsShown = 0;
		private int totalBeatsHit = 0;
		private int score = 0;
		
		public WorldManager() {
		}

		@Override
		public void render(float delta) {

			// input and hits
			if (Gdx.input.isTouched()) {

				Vector2 touchPos = Resources.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

				for (BeatCore core : activeCores) {

					if (core.getHitbox().contains(touchPos.x, touchPos.y) && !core.beenHit()) {
							
						Accuracy accuracy = core.onHit(engine.getSongTime());
						
						if (accuracy != Accuracy.INACTIVE){
							
							int divisor = accuracy.ordinal() + 1;
							int scoreValue = (int) core.getScoreValue() / divisor;
							combo++;
							totalBeatsHit++;
							score += scoreValue;
							
							fx.makeExplosion(touchPos, core.getColor());
							
							hud.showMessage(accuracy.toString() + "!");
						}
						else
							combo = 0;
					}
				}
			}

			{// rings

				for (int i = 0; i < activeCores.size(); i++) {

					BeatCore c = activeCores.get(i);

					if (c.isDead()) {
						activeCores.remove(c);
						corePool.free(c);
						
						totalBeatsShown++;
					}
				}
				
				Resources.worldBatch.begin();
					for (BeatCore core : activeCores)
						core.render(delta);
				Resources.worldBatch.end();

			}
			
			hud.update(totalBeatsShown, totalBeatsHit, combo, score);

		}

		@Override
		public void onBeatWarning(Beat beat) {

			if (beat.energy > 0f) {

				BeatCore core = corePool.obtain();
				core.setBeat(beat);
				
				Vector2 position = new Vector2();
				position.set(Resources.random.nextInt(9) - 4, Resources.random.nextInt(5) - 2);
				
				if (activeCores.size() > 0) {

					boolean emptySpace = false;

					while (!emptySpace) {

						position.set(Resources.random.nextInt(9) - 4, Resources.random.nextInt(5) - 2);

						for (BeatCore c : activeCores) {
							emptySpace = !(c.getPosition().x == position.x && c.getPosition().y == position.y);
							if (!emptySpace)
								break;
						}
					}
				} 

				core.setPosition(position);

				activeCores.add(core);
			}
			
		}
		@Override
		public void onBeat(Beat beat) {
		}

	}
	
}
