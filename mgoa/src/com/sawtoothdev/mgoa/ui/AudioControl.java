package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.Resources;

public class AudioControl extends Sprite implements IDrawable {
	
	private Sprite audioBtn = new Sprite(new TextureRegion(new Texture("data/textures/audio.png")));
	private OrthographicCamera camera = new OrthographicCamera();

	@Override
	public void update(float delta) {
		
		Vector2 lastTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		Rectangle spriteBox = audioBtn.getBoundingRectangle();
		
		if (spriteBox.contains(lastTouch.x, lastTouch.y)){
			
			if (Resources.menuMusic.isPlaying())
				Resources.menuMusic.pause();
			else
				Resources.menuMusic.play();
		}
			
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
			audioBtn.draw(spriteBatch);
		spriteBatch.end();
		
	}

}
