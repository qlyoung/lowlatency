package com.sawtoothdev.mgoa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.Song;

class HUD implements IUpdateable, IDrawable {

	// fonts
	private BitmapFont messageFont = new BitmapFont(Gdx.files.internal("data/fonts/typeone.fnt"), false);

	// gfx
	private TextureRegion bottomFadeBar = new TextureRegion(new Texture("data/textures/fadebar_bottom.png"));

	// game values
	private int score, displayScore;
	private String message = null;
	private Song song;

	// controls
	private Sprite pauseButton = new Sprite(new Texture("data/textures/ui/pause.png"));
	
	public HUD(Song song){
		pauseButton.setPosition(pauseButton.getX() + 5, Gdx.graphics.getHeight() - pauseButton.getHeight() - 5);
		this.song = song;
	}
	
	@Override
	public void update(float delta) {

		// check pause
		if (Gdx.input.justTouched()) {
			Vector2 lastTouch =
					new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			Rectangle spriteBox = pauseButton.getBoundingRectangle();

			if (spriteBox.contains(lastTouch.x, lastTouch.y))
				Resources.game.getScreen().pause();
		}
		
		// update the score spinner
		if (displayScore < score)
			displayScore += 17;
		else if (displayScore > score)
			displayScore = score;

		// fade messages
		Color c = messageFont.getColor();
		if (c.a > 0f) {
			float alpha = (c.a - delta) < 0 ? 0 : c.a - delta;
			messageFont.setColor(c.r, c.g, c.b, alpha);
		}

	}
	@Override
	public void draw(SpriteBatch batch){
		
		batch.setProjectionMatrix(Resources.screenCam.combined);
		batch.begin();
		{
			// gfx
			batch.draw(bottomFadeBar, 0, 0);
			
			// song data
			Resources.uiFnt.draw(batch, song.getArtist() + " - " + song.getTitle(), 10, 23);
			
			// score
			Resources.uiFnt.draw(batch, String.format("%08d", displayScore), Gdx.graphics.getWidth() - 140f, 20);

			// messages
			if (message != null){
				float length = messageFont.getBounds(message).width;
				messageFont.draw(batch, message, Gdx.graphics.getWidth() / 2f - (length / 2f), Gdx.graphics.getHeight() / 2f);
			}
			
			// controls
			pauseButton.draw(batch);
			
		}
		batch.end();
	}

	public void showMessage(String message) {
		this.message = message;
		messageFont.setColor(Color.WHITE);
	}
	
	public void updateDisplay(int totalBeatsShown, int totalBeatsHit, int combo, int score){
		this.score = score;
	}

}
