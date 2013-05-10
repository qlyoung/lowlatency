package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
			} catch (IOException e) { Gdx.app.log("Load Screen", e.getMessage()); }
			
			
			ArrayList<Beat> easy, medium, hard, original;
			
			easy = BeatsProcessor.removeCloseBeats(beats, 350);
			medium = BeatsProcessor.removeCloseBeats(beats, 200);
			hard = BeatsProcessor.removeCloseBeats(beats, 150);
			original = beats;
			
			map = new BeatMap(easy, medium, hard, original);
		}
		
	}
	
	private LoadingThread loadThread;
	private BitmapFont font = new BitmapFont();
	
	public LoadScreen(FileHandle audioFile){
		loadThread = new LoadingThread(audioFile);
		loadThread.start();
	}
	
	@Override
	public void render(float delta) {
		
		Resources.spriteBatch.begin();
		font.draw(Resources.spriteBatch, "Loading...", 15, 455);
		Resources.spriteBatch.end();
		
		//if done loading, move on
		if (!loadThread.isAlive()) {
			Resources.menuMusic.stop();
			PlayScreen playScreen = new PlayScreen(loadThread.map, loadThread.audioFile);
			Resources.game.setScreen(playScreen);
		}
	}

	@Override
	public void show() {
		
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
