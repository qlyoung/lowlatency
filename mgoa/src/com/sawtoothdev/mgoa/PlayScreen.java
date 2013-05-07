package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
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

		// le pool
		CorePool corePool;

		// objects
		ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();
		int index = 0;

		public WorldManager() {
			corePool = new CorePool();
			Resources.font.setColor(Color.WHITE);
		}

		@Override
		public void render(float delta) {

			// input
			if (Gdx.input.isTouched()) {

				// translate touch coords to world coords
				Vector2 touchPos = Resources.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

				// check collisions
				for (BeatCore core : activeCores) {

					if (core.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
						
						String nextAccuracy = core.onHit(engine.getSongTime()).toString();
						
						if (nextAccuracy != "INACTIVE") {
							hud.setLastAccuracy(nextAccuracy + "!");
							hud.addToScore(core.getScoreValue());
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

				// don't place beats on top of each other
				if (activeCores.size() > 0) {
					
					boolean clean = false;

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
				}
				else {
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

	}

	private class HUD implements IGameObject {

		private BitmapFont accuracyFont = new BitmapFont();
		private BitmapFont scoreFont = new BitmapFont();
		
		private int score, displayScore;
		String lastAccuracy = "READY";

		public HUD() {

		}

		@Override
		public void render(float delta) {

			if (displayScore < score)
				displayScore += 5;
			else if (displayScore > score)
				displayScore = score;

			scoreFont.draw(Resources.spriteBatch, String.format("%08d", displayScore), 10, 460);
			accuracyFont.draw(Resources.spriteBatch, lastAccuracy, 380, 250);
			
			
			Color c = accuracyFont.getColor();
			if (c.a > 0f){
				float alpha = (c.a - delta) < 0 ? 0 : c.a - delta;

				accuracyFont.setColor(c.r, c.g, c.b, alpha);
			}
			
		}

		public void addToScore(int value) {
			score += value;
		}
		public void setLastAccuracy(String message) {
			this.lastAccuracy = message;
			accuracyFont.setColor(Color.WHITE);
		}

	}

	// engines and managers
	private final SongEngine engine;
	private final WorldManager worldManager;
	private final HUD hud;

	public PlayScreen(BeatMap map, FileHandle audioFile) {

		worldManager = new WorldManager();
		hud = new HUD();

		ArrayList<Beat> bMap;
		switch (Resources.difficulty.name) {
		case EASY:
			bMap = map.easy;
			break;
		case NORMAL:
			bMap = map.medium;
			break;
		default:
		case HARD:
			bMap = map.hard;
		}

		engine = new SongEngine(bMap, Resources.difficulty.ringTimeMs, audioFile);
		engine.addListener(worldManager);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Resources.spriteBatch.begin();
		{
			engine.render(delta);
			worldManager.render(delta);
			hud.render(delta);
		}
		Resources.spriteBatch.end();

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
