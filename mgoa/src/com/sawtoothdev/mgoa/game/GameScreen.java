package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.sawtoothdev.mgoa.BeatMap;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.game.WorldManager.WorldState;
import com.sawtoothdev.mgoa.ui.MenuScreen;

/**
 * What are we here for, anyway?
 * 
 * @author albatross
 */

public class GameScreen implements Screen {

	private final WorldManager worldManager;

	// state
	private enum GameScreenState {
		INITIALIZED, RUNNING, DONE, PAUSED
	};

	private GameScreenState state;

	public GameScreen(BeatMap map, FileHandle audioFile) {

		worldManager = new WorldManager(map, audioFile);

		// IT BEGINS
		state = GameScreenState.INITIALIZED;
	}

	@Override
	public void render(float delta) {

		switch (state) {

		case INITIALIZED:
			break;
		case RUNNING:
			worldManager.update(delta);
			worldManager.draw(Resources.defaultSpriteBatch);
			if (worldManager.getState() == WorldState.FINISHED)
				this.state = GameScreenState.DONE;
			break;
		case DONE:
			Resources.game.setScreen(new MenuScreen());
			break;
		case PAUSED:
		default:
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
