package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IPausable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.MainGame;
import com.sawtoothdev.mgoa.objects.OneShotMusicPlayer;
import com.sawtoothdev.mgoa.objects.Stats;

public class GameWorld implements IUpdateable, IDrawable, IPausable {
	
	final OneShotMusicPlayer music;
	final CoreManager coreManager;
	final Visualizer visualizer;
	final EffectsManager fxbox;
	final HudManager hud;
	final Countdown countdown;
	
	public enum WorldState { INITIALIZED, BEFORE, ACTIVE, AFTER, PAUSED, FINISHED };
	private WorldState state;
	
	public GameWorld() {

		music = new OneShotMusicPlayer(MainGame.Temporal.song.getHandle());
		hud = new HudManager(MainGame.Temporal.song);
		coreManager = new CoreManager(this);
		visualizer = new Visualizer(MainGame.Temporal.rawmap, music);
		fxbox = new EffectsManager();
		
		MainGame.Temporal.stats = new Stats();
		MainGame.Temporal.stats.numBeats = MainGame.Temporal.beatmap.size();
		
		countdown = new Countdown(hud, 4);

		state = WorldState.INITIALIZED;
	}

	@Override
	public void update(float delta) {

		switch (state) {
		case INITIALIZED:
			break;
		case BEFORE:
			countdown.update(delta);
			countdown.draw(null);
			hud.update(delta);
			
			if (countdown.isFinished()){
				state = WorldState.ACTIVE;
				music.play();
			}
			break;
		case ACTIVE:
			visualizer.update(delta);
			fxbox.update(delta);
			coreManager.update(delta);
			hud.update(delta);
			// weird behavior here, when pause() gets called libgdx
			// calls it from another thread and so it is possible that
			// the music will pause in the middle of an update tick
			// and be interpreted as a game over, thus we check if
			// the game is paused before setting game over
			if (!music.isPlaying() && state != WorldState.PAUSED)
				state = WorldState.FINISHED;
			break;
		case AFTER:
			
		case PAUSED:
		case FINISHED:
		default:
			break;
		}

	}

	@Override
	public void draw(SpriteBatch batch) {

		visualizer.draw(batch);
		
		batch.begin();
		fxbox.draw(batch);
		coreManager.draw(batch);
		hud.draw(batch);
		batch.end();
	}

	public void start() {
		state = WorldState.BEFORE;
	}

	@Override
	public void pause() {
		this.state = WorldState.PAUSED;
		music.pause();
	}

	@Override
	public void unpause() {
		music.play();
		this.state = WorldState.ACTIVE;
	}

	public WorldState getState() {
		return state;
	}
}
