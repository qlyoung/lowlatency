package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.mgoa.WorldManager.WorldState;

/**
 * Heart of the game, controls gameplay itself.
 * 
 * @author albatross
 * 
 */

public class PlayScreen implements Screen {

	
	private final WorldManager worldManager;
	
	//state
	private enum GameScreenState {INITIALIZED, RUNNING, DONE, PAUSED};
	private GameScreenState state;

	
	public PlayScreen(BeatMap map, FileHandle audioFile) {

		worldManager = new WorldManager(map, audioFile);
		
		// IT BEGINS
		state = GameScreenState.INITIALIZED;
	}

	@Override
	public void render(float delta) {

		//state update
		if (worldManager.getState() == WorldState.FINISHED)
			this.state = GameScreenState.DONE;
		
		
		switch (state) {
		
		case INITIALIZED:
			break;
			
		case RUNNING:
			
			worldManager.update(delta);
			worldManager.draw(Resources.defaultSpriteBatch);
			break;
			
		case DONE:
			Resources.game.setScreen(new MenuScreen());
			break;
			
		case PAUSED:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		worldManager.start();
		state = GameScreenState.RUNNING;
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
