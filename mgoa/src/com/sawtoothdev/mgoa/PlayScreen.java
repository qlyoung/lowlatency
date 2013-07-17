package com.sawtoothdev.mgoa;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.BeatCore.Accuracy;
import com.sawtoothdev.mgoa.SongEngine.EngineState;
import com.sawtoothdev.mgoa.SongEngine.ISongEventListener;

/**
 * Heart of the game, controls gameplay itself.
 * 
 * @author albatross
 * 
 */

public class PlayScreen implements Screen {

	private class CoreManager implements IDrawableGameObject, ISongEventListener {

		// pools and cores
		private CorePool corePool = new CorePool();
		private ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();
		
		// guts
		private OrthographicCamera camera = new OrthographicCamera(10, 6);
		
		// vars
		public int combo = 0;
		public int totalBeatsShown = 0;
		public int totalBeatsHit = 0;
		public int score = 0;
		
		@Override
		public void update(float delta) {
			// input
			if (Gdx.input.isTouched()) {

				Vector2 touchPos = Resources.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()), camera);

				for (BeatCore core : activeCores) {

					if (core.getHitbox().contains(touchPos.x, touchPos.y) && !core.beenHit()) {
						
						// register a hit event with the beat and note the accuracy
						Accuracy accuracy = core.onHit(engine.getSongTime());
						
						// calculate the score value based on accuracy
						int divisor = accuracy.ordinal() + 1;
						int scoreValue = (int) core.getScoreValue() / divisor;
						
						// statistics & scoring
						combo++;
						totalBeatsHit++;
						score += scoreValue;
						
						// pretty lights
						fx.makeExplosion(core.getPosition(), core.getColor());
						
						// user feedback
						hud.showMessage(accuracy.toString() + "!");
						
					}
				}
			}
			
			// update
			for (int i = 0; i < activeCores.size(); i++) {

				BeatCore c = activeCores.get(i);
				
				// free the dead ones
				if (c.isDead()) {
					
					// check if the current combo has been broken
					if (!c.beenHit())
						combo = 0;
					
					activeCores.remove(c);
					corePool.free(c);
					
					totalBeatsShown++;
				}					
			}
			
			for (BeatCore core : activeCores)
				core.update(delta);
		}
		@Override
		public void draw(SpriteBatch batch) {
			
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			{
				for (BeatCore core : activeCores)
					core.draw(Resources.defaultSpriteBatch);
			}
			batch.end();
			
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
	

	
	// engines and managers
	private final SongEngine engine;
	private final CoreManager coreManager;
	private final HUD hud;
	private final EffectMaker fx;
	private final Visualizer visualizer;
	
	//state
	private enum ScreenState {INITIALIZED, RUNNING, DONE, PAUSED};
	private ScreenState state;

	public PlayScreen(BeatMap map, FileHandle audioFile) {

		// init
		engine = new SongEngine(map, audioFile);
		coreManager = new CoreManager();
		hud = new HUD(audioFile);
		fx = new EffectMaker();
		visualizer = new Visualizer(2048);

		// wiring
		engine.addListener(coreManager);
		
		// IT BEGINS
		state = ScreenState.INITIALIZED;
	}

	@Override
	public void render(float delta) {

		//state update
		if (engine.getState() == EngineState.DONE)
			this.state = ScreenState.DONE;
		
		switch (state) {
		
		case INITIALIZED:
			break;
			
		case RUNNING:
			
			
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			// update
			engine.update(delta);
			coreManager.update(delta);
			fx.update(delta);
			hud.updateDisplay(coreManager.totalBeatsShown, 
					coreManager.totalBeatsHit,
					coreManager.combo, 
					coreManager.score);
			hud.update(delta);
			
			// draw
			visualizer.renderFrame(engine.getMusic().getLatestSamples(), 61, Color.ORANGE, Resources.defaultSpriteBatch);
			fx.draw(Resources.defaultSpriteBatch);
			coreManager.draw(Resources.defaultSpriteBatch);
			hud.draw(Resources.defaultSpriteBatch);
			
			break;
			
		case DONE:
			Resources.game.setScreen(new MenuScreen());
			break;
			
		case PAUSED:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		engine.start();
		state = ScreenState.RUNNING;
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
