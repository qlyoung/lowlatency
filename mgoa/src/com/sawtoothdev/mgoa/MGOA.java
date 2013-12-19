package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.sawtoothdev.mgoa.ui.MenuScreen;

/**
 * Main game class, top level.
 * Responsible for initialization and clearing the screen.
 * 
 * @author albatross
 *
 */

public class MGOA extends Game {
	
	public static Audio audio;
	public static Gfx gfx;
	public static Utilities util;
	public static Preferences settings;
	public static UI ui;
	public static Game game;
	
	public static final String VERSION = "pre-alpha";
	public static final boolean TESTING = true;
	
	@Override
	public void create() {
		
		// init
		audio = new Audio();
		gfx = new Gfx();
		util = new Utilities();
		settings = Gdx.app.getPreferences("settings");
		ui = new UI();
		game = this;
		
		// config
		settings.putBoolean("firstrun", !settings.contains("firstrun"));
		settings.flush();
		
		// begin
		this.setScreen(new MenuScreen());
	}

	@Override
	public void render() {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		super.render();
		
		if (TESTING)
			util.debugOverlay.draw(gfx.defaultSpriteBatch);
	}
}
