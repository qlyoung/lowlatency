package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sawtoothdev.mgoa.LoadingThread;
import com.sawtoothdev.mgoa.PrettyLights;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.game.GameScreen;

/**
 * Responsible for loading all resources before gameplay begins. This includes
 * audio analysis, map generation, and graphics.
 * 
 * @author albatross
 * 
 */

public class LoadScreen implements Screen {

	private LoadingThread loadThread;
	private PrettyLights prettyLights = new PrettyLights(8);

	public LoadScreen(){
		loadThread = new LoadingThread();
	}
	
	@Override
	public void render(float delta) {

		{// update
			prettyLights.update(delta);
			
			if (!loadThread.isAlive())
				finish();
		}

		{// draw
			prettyLights.draw(null);

			Resources.defaultSpriteBatch.setProjectionMatrix(Resources.screenCam.combined);
			Resources.defaultSpriteBatch.begin();
				Resources.uiFnt.draw(Resources.defaultSpriteBatch, "Loading...",
						Gdx.graphics.getWidth() / 2f - 20f,
						Gdx.graphics.getHeight() / 2f);
			Resources.defaultSpriteBatch.end();

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

	private void finish(){
		Resources.menuMusic.pause();
		System.gc();
		Resources.game.setScreen(new GameScreen());
	}
}
