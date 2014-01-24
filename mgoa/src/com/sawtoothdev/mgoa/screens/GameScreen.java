package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.game.GameWorld;
import com.sawtoothdev.mgoa.game.GameWorld.WorldState;

/**
 * What are we here for, anyway?
 * 
 * @author albatross
 */

public class GameScreen implements Screen {

	private enum GameScreenState { INITIALIZED, RUNNING, DONE, PAUSED };
	private final GameWorld world;
	private GameScreenState state;
	Mgoa game;
	
	public GameScreen(Mgoa gam) {
		game = gam;
		world = new GameWorld(game);

		state = GameScreenState.INITIALIZED;
	}

	@Override
	public void render(float delta) {

		switch (state) {

		case INITIALIZED:
			break;
		case RUNNING:
			world.update(delta);
			world.draw(game.batch);

			// end condition
			if (world.getState() == WorldState.FINISHED)
				this.state = GameScreenState.DONE;
			break;
		case DONE:
			game.setScreen(new FinishScreen(game, world.getStats()));
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
			world.start();
			state = GameScreenState.RUNNING;
		}
		else if (state == GameScreenState.PAUSED)
			resume();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		world.pause();
		this.state = GameScreenState.PAUSED;
		game.setScreen(new PausedScreen(game));
	}

	@Override
	public void resume() {
		world.unpause();
		this.state = GameScreenState.RUNNING;
	}

	@Override
	public void dispose() {

	}

}
