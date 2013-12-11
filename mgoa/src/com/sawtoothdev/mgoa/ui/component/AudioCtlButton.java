package com.sawtoothdev.mgoa.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.Resources;

public class AudioCtlButton extends Sprite implements IUpdateable, IDrawable {

	Texture on = new Texture("data/textures/ui/audio-on.png"),
			off = new Texture("data/textures/ui/audio-off.png");

	private Sprite audioBtn = new Sprite(on);

	@Override
	public void update(float delta) {

		if (Gdx.input.justTouched()) {
			Vector2 lastTouch = new Vector2(Gdx.input.getX(),
					Gdx.graphics.getHeight() - Gdx.input.getY());
			Rectangle spriteBox = audioBtn.getBoundingRectangle();

			if (spriteBox.contains(lastTouch.x, lastTouch.y)) {

				if (Resources.menuMusic.isPlaying()) {
					Resources.menuMusic.pause();
					audioBtn.setTexture(off);
				} else {
					Resources.menuMusic.play();
					audioBtn.setTexture(on);
				}

			}

		}

	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.setProjectionMatrix(Resources.screenCam.combined);
		spriteBatch.begin();
		audioBtn.draw(spriteBatch);
		spriteBatch.end();

	}

	public void setPosition(float x, float y) {
		audioBtn.setPosition(x, y);
	}

}
