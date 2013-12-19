package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sawtoothdev.mgoa.LoadingThread;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.PrettyLights;
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
			
			MGOA.gfx.defaultSpriteBatch.setProjectionMatrix(MGOA.gfx.screenCam.combined);
			MGOA.gfx.defaultSpriteBatch.begin();
				MGOA.ui.uiFnt.draw(MGOA.gfx.defaultSpriteBatch, "Loading...",
						Gdx.graphics.getWidth() / 2f - 20f,
						Gdx.graphics.getHeight() / 2f);
			MGOA.gfx.defaultSpriteBatch.end();

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
		MGOA.audio.menuMusic.pause();
		System.gc();
		MGOA.game.setScreen(new GameScreen());
	}
}
