package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;

/**
 * Responsible for loading all resources before gameplay begins.
 * This includes audio analysis, map generation, and graphics.
 * @author albatross
 *
 */

public class LoadScreen implements Screen {
	
	public class LoadingThread extends Thread{
		
		private FileHandle audioFile;
		
		public LoadingThread(FileHandle audioFile){
			this.audioFile = audioFile;
		}
		
		@Override
		public void run() {
			
			float sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE;
			
			ArrayList<Beat> beats = null;
			
			try {
				beats = FastBeatDetector.detectBeats(sensitivity, audioFile);
			} catch (IOException e) { Gdx.app.log("Load Screen", e.getMessage()); }
			
			
			ArrayList<Beat> easy, medium, hard;
			
			easy = BeatsProcessor.removeCloseBeats(beats, 250);
			medium = BeatsProcessor.removeCloseBeats(beats, 175);
			hard = BeatsProcessor.removeCloseBeats(beats, 100);
			
			map = new BeatMap(easy, medium, hard);
			
		}
		
	}
	
	private BeatMap map;
	private LoadingThread loadThread;
	private FileHandle audioFile;
	
	public LoadScreen(FileHandle audioFile){
		this.audioFile = audioFile;
		loadThread = new LoadingThread(audioFile);
	}
	
	
	@Override
	public void render(float delta) {
		
		//show soft interactive light show synced to ambient music while loading
		//
		//...maybe later
		
		
		//if done loading, move on
		if (!loadThread.isAlive()){
			Resources.game.setScreen(new PlayScreen(map, audioFile));
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

}
