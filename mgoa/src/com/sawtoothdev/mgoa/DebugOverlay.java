package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DebugOverlay implements IDrawable {

	private BitmapFont debugFont = new BitmapFont();
	private OrthographicCamera cam = new OrthographicCamera();
	
	public DebugOverlay(){
		debugFont.setFixedWidthGlyphs("123456789abcdefghijklmnopqrstuvwxyz-");
		debugFont.setColor(Color.BLUE);
		cam.setToOrtho(false);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
			debugFont.draw(
				batch,
				Mgoa.VERSION,
				Gdx.graphics.getWidth() - 120,
				Gdx.graphics.getHeight());
			debugFont.draw(
				batch, String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps",
				Gdx.graphics.getWidth() - 120,
				Gdx.graphics.getHeight() - debugFont.getCapHeight() - 10);
		batch.end();

	}

}
