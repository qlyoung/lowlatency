package com.sawtoothdev.mgoa.screens;

import java.io.IOException;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;
import com.sawtoothdev.mgoa.MainGame;

/**
 * Responsible for loading all resources before gameplay begins. This includes
 * audio analysis, map generation, and graphics.
 * 
 * @author albatross
 * 
 */

public class LoadScreen implements Screen {
	
	class LoadingThread extends Thread {

		@Override
		public void run() {

			float sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE - .5f;

			LinkedList<Beat> rawbeats = null;

			try {
				rawbeats = FastBeatDetector.detectBeats(MainGame.Temporal.song.getHandle(), sensitivity);
			} catch (IOException e) {
				Gdx.app.log("Load Screen", e.getMessage());
				return;
			}

			// drop beats under .01 energiez
			LinkedList<Beat> beatmap = BeatsProcessor.dropLowBeats(rawbeats, .01f);
			beatmap = BeatsProcessor.removeCloseBeats(rawbeats, MainGame.Temporal.difficulty.minBeatSpace);
			
			MainGame.Temporal.rawmap = rawbeats;
			MainGame.Temporal.beatmap = beatmap;
		}
	}

	private LoadingThread loadThread;
	BitmapFont font = MainGame.Ui.skin.getFont("naipol");
	
	public LoadScreen(){
		loadThread = new LoadingThread();
	}
	
	@Override
	public void render(float delta) {

		{// update
			MainGame.Gfx.lights.update(delta);
			
			if (!loadThread.isAlive())
				finish();
		}

		{// draw
			MainGame.Gfx.lights.draw(null);
			
			MainGame.Gfx.systemBatch.setProjectionMatrix(MainGame.Gfx.screenCam.combined);
			MainGame.Gfx.systemBatch.begin();
			
			font.draw(MainGame.Gfx.systemBatch, "Loading...",
					Gdx.graphics.getWidth() / 2f - 20f,
					Gdx.graphics.getHeight() / 2f);
			MainGame.Gfx.systemBatch.end();
		}

	}

	@Override
	public void show() {
		loadThread.start();
	}

	@Override
	public void resize(int width, int height) {

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

	private void finish(){
		MainGame.Audio.menuMusic.pause();
		System.gc();
		MainGame.game.setScreen(new GameScreen());
	}
}
