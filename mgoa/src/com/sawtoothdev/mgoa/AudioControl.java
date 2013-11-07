package com.sawtoothdev.mgoa;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AudioControl extends Sprite implements IDrawableGameObject {
	
	private Sprite audioBtn = new Sprite(new TextureRegion(new Texture("data/textures/audio.png")));

	@Override
	public void update(float delta) {
		
		// check if icon is touched, change state
		
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.begin();
			audioBtn.draw(spriteBatch);
		spriteBatch.end();
		
	}

}
