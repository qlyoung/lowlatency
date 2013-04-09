package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;

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
			m = MapGenerator.generate(audioFile);
		}
		
	}
	
	private LoadingThread loadThread;
	private Map m;
	private MgoaMusic music;
	
	public LoadScreen(FileHandle audioFile){
		
		loadThread = new LoadingThread(audioFile);
		music = new MgoaMusic(audioFile);
	}
	
	
	@Override
	public void render(float delta) {
		
		//show soft interactive light show synced to ambient music while loading
		//
		//...maybe later
		
		//if done loading, move on
		if (!loadThread.isAlive()){
			
			Resources.game.setScreen(new PlayScreen(m, music));
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
