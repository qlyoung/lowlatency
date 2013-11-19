package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.sawtoothdev.mgoa.ui.MenuScreen;

/**
 * Main game class, top level.
 * Responsible for initialization and screen switching.
 * 
 * @author albatross
 *
 */

public class MGOA extends Game {
	
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
		// miscellaneous setup
		Resources.screenCam.setToOrtho(false);
		
		
		// ~~begin~~
		if (Resources.settings.contains("firstrun"))
			Resources.settings.putBoolean("firstrun", false);
		else
			Resources.settings.putBoolean("firstrun", true);
				
		this.setScreen(new MenuScreen());

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		super.render();
		
		if (Resources.DEBUG)
			Resources.debugInfo.draw(Resources.defaultSpriteBatch);
	}
}