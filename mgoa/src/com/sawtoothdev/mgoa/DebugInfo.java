package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugInfo implements IDrawable {

	
	public static final BitmapFont debugFont = new BitmapFont();
	
	public DebugInfo(){
		debugFont.setFixedWidthGlyphs("123456789abcdefghijklmnopqrstuvwxyz-");
		debugFont.setColor(Color.GREEN);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.setProjectionMatrix(Resources.screenCam.combined);
		Resources.defaultSpriteBatch.begin();
			debugFont.draw(
				Resources.defaultSpriteBatch, Resources.VERSION,
				Gdx.graphics.getWidth() - 120,
				Gdx.graphics.getHeight());
			debugFont.draw(
				Resources.defaultSpriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps",
				Gdx.graphics.getWidth() - 120,
				Gdx.graphics.getHeight() - debugFont.getCapHeight() - 10);
		Resources.defaultSpriteBatch.end();

	}

}
