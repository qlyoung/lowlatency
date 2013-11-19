package com.sawtoothdev.mgoa.ui;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;
import com.sawtoothdev.mgoa.BeatMap;
import com.sawtoothdev.mgoa.Difficulty;
import com.sawtoothdev.mgoa.PrettyLights;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.game.Playthrough;

/**
 * Responsible for loading all resources before gameplay begins. This includes
 * audio analysis, map generation, and graphics.
 * 
 * I put all the loading code its own thread so we can display an interactive
 * load screen at some point.
 * 
 * @author albatross
 * 
 */

public class LoadScreen implements Screen {

	private SpriteBatch batch = Resources.defaultSpriteBatch;

	public class LoadingThread extends Thread {

		public FileHandle audioFile;
		public BeatMap map;

		public LoadingThread(FileHandle audioFile) {
			this.audioFile = audioFile;
		}

		@Override
		public void run() {
			
			float sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE - .5f;

			ArrayList<Beat> beats = null;

			try {
				beats = FastBeatDetector.detectBeats(audioFile, sensitivity);
			} catch (IOException e) {
				Gdx.app.log("Load Screen", e.getMessage());
				return;
			}

			
			//maybe process the beats somehow so they
			//have a minimum energy level?
			
			
			ArrayList<Beat> easy, normal, hard, original;

			easy = BeatsProcessor.removeCloseBeats(beats,
					Difficulty.EASY.minBeatSpace);
			normal = BeatsProcessor.removeCloseBeats(beats,
					Difficulty.NORMAL.minBeatSpace);
			hard = BeatsProcessor.removeCloseBeats(beats,
					Difficulty.HARD.minBeatSpace);
			original = beats;

			map = new BeatMap(easy, normal, hard, original);

			System.gc();
		}

	}

	private LoadingThread loadThread;
	private PrettyLights prettyLights = new PrettyLights(15);

	public LoadScreen() {

	}

	@Override
	public void render(float delta) {

		{// update
			if (!loadThread.isAlive()) {

				if (loadThread.map != null) {
					PreviewScreen previewScreen = new PreviewScreen(
							loadThread.map, loadThread.audioFile);
					Resources.game.setScreen(previewScreen);
				} else
					Resources.game.setScreen(new ChooseSongScreen());
			}
			
			prettyLights.update(delta);
		}

		{// draw
			prettyLights.draw(null);

			batch.setProjectionMatrix(Resources.screenCam.combined);
			batch.begin();
				Resources.uiFnt.draw(Resources.defaultSpriteBatch, "Loading...",
						Gdx.graphics.getWidth() / 2f - 20f,
						Gdx.graphics.getHeight() / 2f);
			batch.end();

		}

	}

	@Override
	public void show() {
		loadThread = new LoadingThread(Playthrough.song.getHandle());
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

}
