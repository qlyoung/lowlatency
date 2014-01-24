package com.sawtoothdev.mgoa.screens;

import java.io.IOException;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;
import com.sawtoothdev.mgoa.Mgoa;

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
				rawbeats = FastBeatDetector.detectBeats(game.song.getHandle(), sensitivity);
			} catch (IOException e) {
				Gdx.app.log("Load Screen", e.getMessage());
				return;
			}

			// drop beats under .01 energiez
			LinkedList<Beat> beatmap = BeatsProcessor.dropLowBeats(rawbeats, .01f);
			beatmap = BeatsProcessor.removeCloseBeats(rawbeats, game.difficulty.minBeatSpace);
			
			game.rawmap = rawbeats;
			game.beatmap = beatmap;
		}
	}

	LoadingThread loadThread;
	BitmapFont font;
	Mgoa game;
	Camera cam;
	
	public LoadScreen(Mgoa gam){
		game = gam;
		loadThread = new LoadingThread();
		font = gam.skin.getFont("naipol");
		cam = new OrthographicCamera();
	}
	
	@Override
	public void render(float delta) {

		{// update
			game.lights.update(delta);
			
			if (!loadThread.isAlive())
				finish();
		}

		{// draw
			game.lights.draw(null);
			
			game.batch.setProjectionMatrix(cam.combined);
			game.batch.begin();
				font.draw(game.batch, "Loading...", 
						Gdx.graphics.getWidth() / 2f - 20f,
						Gdx.graphics.getHeight() / 2f);
			game.batch.end();
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
		game.menuMusic.pause();
		System.gc();
		game.setScreen(new GameScreen(game));
	}
}
