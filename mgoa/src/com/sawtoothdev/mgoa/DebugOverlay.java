package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugOverlay implements IDrawable {

	
	public static final BitmapFont debugFont = new BitmapFont();
	
	public DebugOverlay(){
		debugFont.setFixedWidthGlyphs("123456789abcdefghijklmnopqrstuvwxyz-");
		debugFont.setColor(Color.GREEN);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.setProjectionMatrix(MGOA.gfx.screenCam.combined);
		MGOA.gfx.defaultSpriteBatch.begin();
			debugFont.draw(
				MGOA.gfx.defaultSpriteBatch,
				MGOA.VERSION,
				Gdx.graphics.getWidth() - 120,
				Gdx.graphics.getHeight());
			debugFont.draw(
				MGOA.gfx.defaultSpriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps",
				Gdx.graphics.getWidth() - 120,
				Gdx.graphics.getHeight() - debugFont.getCapHeight() - 10);
		MGOA.gfx.defaultSpriteBatch.end();

	}

}
