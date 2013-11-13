package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sawtoothdev.mgoa.ui.MenuScreen;

/**
 * Main game class, top level.
 * Responsible for initialization and screen switching.
 * 
 * @author albatross
 *
 */

public class MGOA extends Game {
	
	private BitmapFont debugFont;
	
	@Override
	public void create() {
		
		// ~~initialize~~
		
		Resources.game = this;
		
		// load settings
		Resources.settings = Gdx.app.getPreferences("settings");
		
		// copy necessary data to external storage
		Gdx.files.internal("data/audio/title.mp3").copyTo(Gdx.files.external(".tmp/title.mp3"));
		
		// initialize necessary resources
		Resources.menuMusic = Gdx.audio.newMusic(Gdx.files.external(".tmp/title.mp3"));
		debugFont = new BitmapFont();
		debugFont.setFixedWidthGlyphs("123456789abcdefghijklmnopqrstuvwxyz-");
		debugFont.setColor(Color.GREEN);
		
		Resources.screenCam.setToOrtho(false);
		
		
		// ~~begin~~
		
		if (Resources.settings.contains("firstrun"))
			Resources.settings.putBoolean("firstrun", false);
		else
			Resources.settings.putBoolean("firstrun", true);
		
		//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		
		this.setScreen(new MenuScreen());

	}

	@Override
	public void render() {
		super.render();
		
		if (Resources.DEBUG){
			Resources.defaultSpriteBatch.setProjectionMatrix(Resources.screenCam.combined);
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
}