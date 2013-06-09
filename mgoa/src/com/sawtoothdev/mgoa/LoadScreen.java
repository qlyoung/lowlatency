package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	
	public class LoadingThread extends Thread{
		
		public FileHandle audioFile;
		public BeatMap map;
		
		public LoadingThread(FileHandle audioFile){
			this.audioFile = audioFile;
		}
		
		@Override
		public void run() {
			
			float sensitivity = FastBeatDetector.SENSITIVITY_STANDARD;
			
			ArrayList<Beat> beats = null;
			
			try {
				beats = FastBeatDetector.detectBeats(sensitivity, audioFile);
			} catch (IOException e) {
				Gdx.app.log("Load Screen", e.getMessage());
				//return;
			}
			
			Gdx.app.log("load screen", "load complete, adjusting difficulty...");
			
			ArrayList<Beat> easy, medium, hard, original;
			
			easy = BeatsProcessor.removeCloseBeats(beats, 250);
			medium = BeatsProcessor.removeCloseBeats(beats, 150);
			hard = BeatsProcessor.removeCloseBeats(beats, 100);
			original = beats;
			
			map = new BeatMap(easy, medium, hard, original);
			
			Gdx.app.log("Load thread", "garbage collecting...");
			System.gc();
			Gdx.app.log("load thread", "processing complete");
			
		}
		
	}
	
	private LoadingThread loadThread;
	private BitmapFont font = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
	
	
	public LoadScreen(){
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Resources.screenBatch.begin();
			font.draw(Resources.screenBatch, "Loading...", 20, Gdx.graphics.getHeight() - 50f);
		Resources.screenBatch.end();
		
		//if done loading, move on
		if (!loadThread.isAlive()) {
			
			if (loadThread.map != null) {
				Resources.menuMusic.stop();
				PreviewScreen previewScreen = new PreviewScreen(loadThread.map, loadThread.audioFile);
				Resources.game.setScreen(previewScreen);
			} else
				Resources.game.setScreen(new ChooseSongScreen());
		}	
	}

	@Override
	public void show() {
		loadThread = new LoadingThread(Resources.currentSong);
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
