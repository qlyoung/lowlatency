package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
		
		// init
		Resources.game = this;
		
		//	settings
		Resources.settings = Gdx.app.getPreferences("settings");
		if (Resources.settings.contains("firstrun"))
			Resources.settings.putBoolean("firstrun", false);
		else
			Resources.settings.putBoolean("firstrun", true);
		
		// copy necessary data to external storage
		Gdx.files.internal("data/audio/title.mp3").copyTo(Gdx.files.external(".tmp/title.mp3"));
		
		// initialize necessary resources
		Resources.menuMusic = new OneShotMusicPlayer(Gdx.files.external(".tmp/title.mp3"));
		debugFont = new BitmapFont();
		
		this.setScreen(new MenuScreen());

	}

	@Override
	public void render() {
		super.render();
		
		if (Resources.DEBUG){
			Resources.defaultSpriteBatch.begin();
			debugFont.draw(Resources.defaultSpriteBatch, Resources.version, 10, Gdx.graphics.getHeight());
			Resources.defaultSpriteBatch.end();
		}
	}
}