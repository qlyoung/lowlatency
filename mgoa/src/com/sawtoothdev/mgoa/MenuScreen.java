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
		
		playButton = new Sprite(new TextureRegion(new Texture("data/textures/menubutton.png"), 128, 50));
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		
		if (playButton.getBoundingRectangle().contains(touchPos.x, touchPos.y))
			Resources.game.setScreen(Resources.chooseSongScreen);
		
		
		Resources.spriteBatch.begin();
		playButton.draw(Resources.spriteBatch);
		Resources.spriteBatch.end();
	}
	
	

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Resources.menuMusic.setLooping(true);
		Resources.menuMusic.play();
		Vector2 pos = Resources.projectToScreen(new Vector2(5, 3));
		
		playButton.setPosition(pos.x - playButton.getWidth() / 2f, pos.y - playButton.getHeight() / 2f);
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
