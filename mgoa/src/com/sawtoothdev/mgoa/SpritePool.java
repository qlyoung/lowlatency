package com.sawtoothdev.mgoa;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool;

public class SpritePool extends Pool<Sprite>{

	Texture texture;
	
	public SpritePool(Texture tex){
		this.texture = tex;
	}
	
	@Override
	protected Sprite newObject() {
		return new Sprite(texture);
	}
	
	@Override
	public Sprite obtain() {
		Sprite s = super.obtain();
		s.setScale(1);
		return s;
	}

}
