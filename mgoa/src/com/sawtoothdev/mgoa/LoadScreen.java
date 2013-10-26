package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;

/**
 * Responsible for loading all resources before gameplay begins.
 * This includes audio analysis, map generation, and graphics.
 * 
 * I put all the loading code its own thread so we can display
 * an interactive load screen at some point.
 * 
 * @author albatross
 *
 */

public class LoadScreen implements Screen {
	
	private OrthographicCamera camera = new OrthographicCamera();
	private SpriteBatch batch = Resources.defaultSpriteBatch;
	
	public class LoadingThread extends Thread{
		
		public FileHandle audioFile;
		public BeatMap map;
		
		public LoadingThread(FileHandle audioFile){
			this.audioFile = audioFile;
		}
		
		@Override
		public void run() {
			
			float sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE;
			
			ArrayList<Beat> beats = null;
			
			try {
				beats = FastBeatDetector.detectBeats(sensitivity, audioFile);
			} catch (IOException e) {
				Gdx.app.log("Load Screen", e.getMessage());
				//return;
			}
			
			Gdx.app.log("load screen", "load complete, adjusting difficulty...");
			
			ArrayList<Beat> easy, normal, hard, original;
			
			easy = BeatsProcessor.removeCloseBeats(beats, Difficulty.EASY.minBeatSpace);
			normal = BeatsProcessor.removeCloseBeats(beats, Difficulty.NORMAL.minBeatSpace);
			hard = BeatsProcessor.removeCloseBeats(beats, Difficulty.HARD.minBeatSpace);
			original = beats;
			
			map = new BeatMap(easy, normal, hard, original);
			
			Gdx.app.log("Load thread", "garbage collecting...");
			System.gc();
			Gdx.app.log("load thread", "processing complete");
			
		}
		
	}
	
	private LoadingThread loadThread;
	private BitmapFont font = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
	private PrettyLights prettyLights = new PrettyLights(new OrthographicCamera(10, 6));
	
	public LoadScreen(){
		camera.setToOrtho(false);
	}
	
	@Override
	public void render(float delta) {
		
		// update
		if (!loadThread.isAlive()) {
			
			if (loadThread.map != null) {
				PreviewScreen previewScreen = new PreviewScreen(loadThread.map, loadThread.audioFile);
				Resources.game.setScreen(previewScreen);
			} else
				Resources.game.setScreen(new ChooseSongScreen());
		}

		prettyLights.update(delta);
		
		// draw
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		prettyLights.draw(null);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		{
			font.draw(Resources.defaultSpriteBatch, "Loading...", Gdx.graphics.getWidth() / 2f - 20f, Gdx.graphics.getHeight() / 2f);
		}
		batch.end();
		
	}

	@Override
	public void show() {
		loadThread = new LoadingThread(Playthrough.songHandle);
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
