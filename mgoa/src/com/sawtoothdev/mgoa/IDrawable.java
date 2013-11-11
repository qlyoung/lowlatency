package com.sawtoothdev.mgoa;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IDrawable extends IUpdateable {

	/**
	 * Draws this object to the provided spritebatch
	 * @param batch
	 */
	public void draw(SpriteBatch batch);
}
