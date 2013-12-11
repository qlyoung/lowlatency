package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.mgoa.GameConfiguration;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IPausable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.OneShotMusicPlayer;

public class WorldManager implements IUpdateable, IDrawable, IPausable {

	public final OneShotMusicPlayer music;
	private final HUD hud;
	private final FxBox fxBox;
	private final CoreManager coreManager;
	private final Countdown countdown;
	private final EyeCandy eyecandy;

	public enum WorldState {
		INITIALIZED, COUNTDOWN, ACTIVE, PAUSED, FINISHED
	};

	private WorldState state;

	public WorldManager() {

		music = new OneShotMusicPlayer(GameConfiguration.song.getHandle());
		hud = new HUD(GameConfiguration.song);
		fxBox = new FxBox();
		coreManager = new CoreManager(music, fxBox, hud, GameConfiguration.beatmap, GameConfiguration.difficulty);
		eyecandy = new EyeCandy(GameConfiguration.rawmap, music);
		countdown = new Countdown(hud, 4);

		state = WorldState.INITIALIZED;
	}

	@Override
	public void update(float delta) {

		switch (state) {
		case INITIALIZED:
			break;
		case COUNTDOWN:
			countdown.update(delta);
			hud.update(delta);
			if (countdown.isFinished()){
				state = WorldState.ACTIVE;
				music.play();
			}
			break;
		case ACTIVE:
			eyecandy.update(delta);
			coreManager.update(delta);
			fxBox.update(delta);
			hud.update(delta);
			// weird behavior here, when pause() gets called libgdx
			// calls it from another thread and so it is possible that
			// the music will pause in the middle of an update tick
			// and be interpreted as a game over, thus we check if
			// the game is paused before setting game over
			if (!music.isPlaying() && state != WorldState.PAUSED)
				state = WorldState.FINISHED;
			break;
		case PAUSED:
		case FINISHED:
		default:
			break;
		}

	}

	@Override
	public void draw(SpriteBatch batch) {

		eyecandy.draw(batch);
		fxBox.draw(batch);
		coreManager.draw(batch);
		hud.draw(batch);
	}

	public void start() {
		state = WorldState.COUNTDOWN;
	}

	public WorldState getState() {
		return state;
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

}
