package com.sawtoothdev.mgoa;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.BeatCore.Accuracy;

public class CoreManager implements IDrawableGameObject {

	// pools and cores
	private CorePool corePool = new CorePool();
	private ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();

	// gfx
	private OrthographicCamera camera;
	
	// event list
	private LinkedList<Beat> events = new LinkedList<Beat>();

	// vars
	public int combo = 0;
	public int totalBeatsShown = 0;
	public int totalBeatsHit = 0;
	public int score = 0;
	
	private OneShotMusicPlayer music;
	private FxBox fx;
	private HUD hud;

	
	public CoreManager(OneShotMusicPlayer music, FxBox fx, HUD hud, ArrayList<Beat> events, OrthographicCamera camera){
		
		for (Beat b : events)
			this.events.add(b);
		
		this.camera = camera;
		this.music = music;
		this.fx = fx;
		this.hud = hud;
	}
	
	@Override
	public void update(float delta) {
			
			// input
			if (Gdx.input.isTouched()) {

				Vector2 touchPos = Resources.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()), camera);

				for (BeatCore core : activeCores) {
					
					if (Resources.DEBUG && Gdx.input.isKeyPressed(Keys.SPACE)){
						if (core.isDying() && !core.beenHit()){
							core.onHit(music.currentTime());
							fx.makeExplosion(core.getPosition(), core.getColor());
						}
					}
					else if (core.getHitbox().contains(touchPos.x, touchPos.y) && !core.beenHit()) {
						
						// register a hit event with the beat and note the accuracy
						Accuracy accuracy = core.onHit(music.currentTime());
						
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
			
			// song events
			while (events.peek() != null && music.currentTime() >= events.peek().timeMs - Playthrough.difficulty.ringTimeMs)
				onBeatWarning(events.poll());
			
			// hud
			hud.updateDisplay(totalBeatsShown, totalBeatsHit, combo, score);
			
			// cull cores
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
			
			// update cores
			for (BeatCore core : activeCores)
				core.update(delta);
			
		}

	@Override
	public void draw(SpriteBatch batch) {

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		{
			for (BeatCore core : activeCores)
				core.draw(batch);
		}
		batch.end();

	}

	public void onBeatWarning(Beat beat) {

		if (beat.energy > 0f) {

			BeatCore core = corePool.obtain();
			core.setBeat(beat);

			Vector2 position = new Vector2();
			position.set(Resources.random.nextInt(9) - 4,
					Resources.random.nextInt(5) - 2);

			if (activeCores.size() > 0) {

				boolean emptySpace = false;

				while (!emptySpace) {

					position.set(Resources.random.nextInt(9) - 4,
							Resources.random.nextInt(5) - 2);

					for (BeatCore c : activeCores) {
						emptySpace = !(c.getPosition().x == position.x && c
								.getPosition().y == position.y);
						if (!emptySpace)
							break;
					}
				}
			}

			core.setPosition(position);

			activeCores.add(core);
		}

	}
}