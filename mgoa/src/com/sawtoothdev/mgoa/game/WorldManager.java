package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.mgoa.BeatMap;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.OneShotMusicPlayer;

public class WorldManager implements IUpdateable, IDrawable {

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

	public WorldManager(BeatMap map, FileHandle audioFile) {

		music = new OneShotMusicPlayer(audioFile);
		hud = new HUD();
		fxBox = new FxBox();
		coreManager = new CoreManager(music, fxBox, hud, map.NORMAL);
		eyecandy = new EyeCandy(map.ORIGINAL, music);
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
			if (!music.isPlaying())
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

}
