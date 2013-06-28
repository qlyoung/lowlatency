package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

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
		
		Resources.game = this;
		
		//settings init
		Resources.settings = Gdx.app.getPreferences("settings");
		
		if (!Resources.settings.contains("firstrun"))
			Resources.settings.putBoolean("firstrun", true);
		else if (Resources.settings.getBoolean("firstrun"))
			Resources.settings.putBoolean("firstrun", false);
		
		// copy what we need to external storage
		Gdx.files.internal("data/audio/title.mp3").copyTo(Gdx.files.external("tmp/title.mp3"));

		// initialize necessary resources
		Resources.menuMusic = new MusicPlayer(Gdx.files.external("tmp/title.mp3"));
		
		this.setScreen(new MenuScreen());

	}

}