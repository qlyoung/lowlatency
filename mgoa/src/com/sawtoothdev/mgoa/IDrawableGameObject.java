package com.sawtoothdev.mgoa;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IDrawableGameObject extends IGameObject {

	/**
	 * Draws this object to the provided spritebatch
	 * @param batch
	 */
	public void draw(SpriteBatch batch);
}
