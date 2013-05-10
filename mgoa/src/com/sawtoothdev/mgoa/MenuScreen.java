package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;

public class MenuScreen implements Screen {

	public FileHandle chosenSong;
	
	public MenuScreen(){
		
	}
	
	@Override
	public void render(float delta) {

		Resources.game.setScreen(new LoadScreen(chosenSong));
		
	}
	
	

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Resources.menuMusic.play();
		
		// load an internal song for now
		chosenSong = Gdx.files.internal("data/audio/promises.mp3");
		chosenSong = Gdx.files.absolute(chosenSong.file().getAbsolutePath());
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
