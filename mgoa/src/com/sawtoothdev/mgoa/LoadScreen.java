package com.sawtoothdev.mgoa;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.audioanalysis.BeatsProcessor;
import com.sawtoothdev.audioanalysis.FastBeatDetector;
import com.sawtoothdev.mgoa.Difficulty.DifficultyName;

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
			
			//overhaul all of this before release
			
			FastBeatDetector analyzer = new FastBeatDetector(audioFile);
			
			float sensitivity = 0;
			
			switch (Resources.difficulty.name){
			case EASY:
				sensitivity = FastBeatDetector.SENSITIVITY_LOW;
				break;
			case NORMAL:
				sensitivity = FastBeatDetector.SENSITIVITY_STANDARD;
				break;
			case HARD:
				sensitivity = FastBeatDetector.SENSITIVITY_STANDARD;
				break;
			case HARDPLUS:
				sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE;
			case TESTING:
				sensitivity = FastBeatDetector.SENSITIVITY_AGGRESSIVE;
			}
			
			try {
				beats = analyzer.detectBeats(sensitivity);
			} catch (IOException e) {
				Gdx.app.log("Load Screen", e.getMessage());
			}
			
			if (!(Resources.difficulty.name == DifficultyName.TESTING))
				beats = BeatsProcessor.removeCloseBeatsExp(beats, 300);
			
		}
		
	}
	
	private LoadingThread loadThread;
	private ArrayList<Beat> beats;
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
			Resources.game.setScreen(new PlayScreen(beats, audioFile));
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
