package com.sawtoothdev.mgoa.game;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.Difficulty;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.Utilities;
import com.sawtoothdev.mgoa.game.BeatCore.Accuracy;
import com.sawtoothdev.mgoa.game.BeatCore.CoreState;

public class CoreManager implements IUpdateable, IDrawable {

	// pools and cores
	private CorePool corePool;
	private ArrayList<BeatCore> activeCores = new ArrayList<BeatCore>();

	// event list
	private LinkedList<Beat> events = new LinkedList<Beat>();

	// vars
	public int combo = 0;
	public int totalBeatsShown = 0;
	public int totalBeatsHit = 0;
	public int score = 0;
	
	private Difficulty difficulty;
	
	private GameWorld GW;
	
	public CoreManager(GameWorld gw){
		
		this.GW = gw;
		
		for (Beat b : GameConfiguration.beatmap)
			this.events.add(b);
		
		this.difficulty = GameConfiguration.difficulty;
		this.corePool = new CorePool();
	}
	
	@Override
	public void update(float delta) {
			
			// input
			if (Gdx.input.isTouched()) {

				Vector2 touchPos = MGOA.gfx.projectToWorld(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

				for (BeatCore core : activeCores) {
					
					if (!core.beenHit())
						if (MGOA.TESTING){
							if(core.getState() == CoreState.dying)
								onCoreHit(core);
						}
						else if (core.checkCollision(touchPos))
							onCoreHit(core);
				}
			}
			
			// song events
			while (events.peek() != null && GW.music.currentTime() >= events.peek().timeMs - difficulty.ringTimeMs)
				onBeatWarning(events.poll());
			
			// hud
			GW.hud.updateDisplay(totalBeatsShown, totalBeatsHit, combo, score);
			
			// cull cores
			for (int i = 0; i < activeCores.size(); i++) {

				BeatCore c = activeCores.get(i);
				
				// free the dead ones
				if (c.getState() == BeatCore.CoreState.dead) {
					
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

		batch.setProjectionMatrix(MGOA.gfx.worldCam.combined);
		batch.begin();
		{
			for (BeatCore core : activeCores)
				core.draw(batch);
		}
		batch.end();

	}

	private void onBeatWarning(Beat beat) {

		if (beat.energy > 0f) {

			BeatCore core = corePool.obtain();
			core.setBeat(beat);

			Vector2 position = new Vector2();
			position.set(MGOA.util.random.nextInt(9) - 4,
					MGOA.util.random.nextInt(5) - 2);

			if (activeCores.size() > 0) {

				boolean emptySpace = false;

				while (!emptySpace) {

					position.set(MGOA.util.random.nextInt(9) - 4,
							MGOA.util.random.nextInt(5) - 2);

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
	
	private void onCoreHit(BeatCore core){
		// register a hit event with the beat and note the accuracy
		Accuracy accuracy = core.onHit(GW.music.currentTime());
		
		// calculate the score value based on accuracy
		int divisor = accuracy.ordinal() + 1;
		int scoreValue = (int) core.getScoreValue() / divisor;
		
		// statistics & scoring
		combo++;
		totalBeatsHit++;
		score += scoreValue;
		
		// 	pretty lights
		GW.visuals.box.makeExplosion(core.getPosition(), core.getColor());
		
		// user feedback
		GW.hud.showMessage(accuracy.toString() + "!");
		GW.hud.showPoints(core.getScoreValue(), core.getPosition());
	}

}