package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sawtoothdev.mgoa.MainGame;
import com.sawtoothdev.mgoa.objects.LoadingThread;

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
			MainGame.gfx.lights.update(delta);
			
			if (!loadThread.isAlive())
				finish();
		}

		{// draw
			MainGame.gfx.lights.draw(null);
			
			MainGame.gfx.sysSB.setProjectionMatrix(MainGame.gfx.screenCam.combined);
			MainGame.gfx.sysSB.begin();
			MainGame.ui.uiFnt.draw(MainGame.gfx.sysSB, "Loading...",
					Gdx.graphics.getWidth() / 2f - 20f,
					Gdx.graphics.getHeight() / 2f);
			MainGame.gfx.sysSB.end();
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
		MainGame.audio.menuMusic.pause();
		System.gc();
		MainGame.game.setScreen(new GameScreen());
	}
}
