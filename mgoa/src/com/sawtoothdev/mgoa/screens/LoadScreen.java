package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sawtoothdev.mgoa.LoadingThread;
import com.sawtoothdev.mgoa.MGOA;

/**
 * Responsible for loading all resources before gameplay begins. This includes
 * audio analysis, map generation, and graphics.
 * 
 * @author albatross
 * 
 */

public class LoadScreen implements Screen {

	private LoadingThread loadThread;

	public LoadScreen(){
		loadThread = new LoadingThread();
	}
	
	@Override
	public void render(float delta) {

		{// update
			MGOA.temporals.lights.update(delta);
			
			if (!loadThread.isAlive())
				finish();
		}

		{// draw
			MGOA.temporals.lights.draw(null);
			
			MGOA.gfx.sysSB.setProjectionMatrix(MGOA.gfx.screenCam.combined);
			MGOA.gfx.sysSB.begin();
			MGOA.ui.uiFnt.draw(MGOA.gfx.sysSB, "Loading...",
					Gdx.graphics.getWidth() / 2f - 20f,
					Gdx.graphics.getHeight() / 2f);
			MGOA.gfx.sysSB.end();
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
