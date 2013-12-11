package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.game.WorldManager.WorldState;
import com.sawtoothdev.mgoa.ui.MenuScreen;
import com.sawtoothdev.mgoa.ui.PausedScreen;

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
	
	public GameScreen() {

		worldManager = new WorldManager();

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
			Gdx.app.log("gamescreen", "paused update");
		default:
			break;
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		
		if (state == GameScreenState.INITIALIZED) {
			worldManager.start();
			state = GameScreenState.RUNNING;
			Gdx.app.log("game screen", "starting");
		}
		else if (state == GameScreenState.PAUSED){
			resume();
		}
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		Gdx.app.log("game screen", "pausing");
		worldManager.pause();
		this.state = GameScreenState.PAUSED;
		Resources.game.setScreen(new PausedScreen(this));
	}

	@Override
	public void resume() {
		Gdx.app.log("game screen", "resuming");
		worldManager.unpause();
		this.state = GameScreenState.RUNNING;
	}

	@Override
	public void dispose() {

	}

}
