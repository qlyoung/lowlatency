package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen implements Screen {

	public boolean switchScreen = false;
	private Sprite playButton;

	
	public MenuScreen() {
		
		playButton = new Sprite(new TextureRegion(new Texture("data/textures/menubutton.png"), 128, 30));
	}
	
	@Override
	public void render(float delta) {

		if (Gdx.input.justTouched()) {
			Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPos = Resources.projectToWorld(touchPos);
			
			if (playButton.getBoundingRectangle().contains(touchPos.x, touchPos.y))
				Resources.game.setScreen(new ChooseSongScreen());
		}
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Resources.worldBatch.setProjectionMatrix(Resources.worldCamera.combined);
		Resources.worldBatch.begin();
			playButton.draw(Resources.worldBatch);
		Resources.worldBatch.end();
	}
	
	

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Resources.menuMusic.setLooping(true);
		Resources.menuMusic.play();

		playButton.setSize(1.5f, 1.5f * playButton.getHeight() / playButton.getWidth());
		playButton.setPosition(-playButton.getWidth() / 2f, -playButton.getHeight() / 2f);
		
		System.gc();
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
