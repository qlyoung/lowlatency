package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.sawtoothdev.mgoa.ui.MenuScreen;
import com.sawtoothdev.mgoa.ui.UIResources;

/**
 * Main game class, top level.
 * Responsible for initialization and clearing the screen.
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
		Resources.settings.putBoolean("firstrun", !Resources.settings.contains("firstrun"));
		Resources.settings.flush();
		
		// copy necessary data to external storage
		Gdx.files.internal("data/audio/title.mp3").copyTo(Gdx.files.external(".tmp/title.mp3"));
		
		// initialize necessary resources
		Resources.menuMusic = Gdx.audio.newMusic(Gdx.files.external(".tmp/title.mp3"));
		UIResources.initializeStyles();
		Resources.difficulties = new Difficulty[3];
		Resources.difficulties[0] = new Difficulty(800, 250, "Relaxed");
		Resources.difficulties[1] = new Difficulty(650, 150, "Normal");
		Resources.difficulties[2] = new Difficulty(600, 100, "Altered");
		
		// miscellaneous setup
		Resources.screenCam.setToOrtho(false);
		
		// ~~begin~~
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
