package com.sawtoothdev.mgoa;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ProgressBar {
	
	private String bar;
	
	private Vector2 position;
	private int width;
	
	private BitmapFont progressFont = new BitmapFont();
	
	public ProgressBar(Vector2 position, int width, float initialValue){
		this.width = width;
		this.setValue(initialValue);
		this.position = position;
		
		progressFont.setFixedWidthGlyphs("abcdefghijklmnopqrstuvwxyz=][<>.");
	}
	
	public void setValue(float value){
		float barWidth = value * width;
		int segments = Math.round((barWidth / progressFont.getSpaceWidth()) / 2f);
		
		bar = new String();
		for (int i = 0; i < segments; i++)
			bar += "=";
		
		bar += ">";
	}
	public void setPosition(Vector2 position){
		this.position = position;
	}
	
	public void draw(SpriteBatch batch){
		batch.begin();
		progressFont.draw(batch, "[", position.x, position.y);
		progressFont.draw(batch, bar, position.x + progressFont.getSpaceWidth(), position.y);
		progressFont.draw(batch, "]", position.x + width, position.y);
		batch.end();
	}

}
