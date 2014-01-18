package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.sawtoothdev.mgoa.screens.MenuScreen;

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
	public static UI ui;
	public static Temporals temporals;
	public static Game game;
	
	public static Preferences settings;
	
	public static final String VERSION = "pre-alpha";
	public static final boolean TESTING = true;
	//public static final FileHandle DATA_DIRECTORY = Gdx.files.external(".mgoa/");
	
	@Override
	public void create() {
		
		// init
		audio = new Audio();
		gfx = new Gfx();
		util = new Utilities();
		ui = new UI();
		temporals = new Temporals();
		game = this;
		settings = Gdx.app.getPreferences("settings");
		
		// config
		settings.putBoolean("firstrun", !settings.contains("firstrun"));
		settings.flush();
		if (!MGOA.settings.contains("bgmusic") || MGOA.settings.getBoolean("bgmusic"))
			MGOA.audio.menuMusic.play();
		MGOA.temporals.lights = new PrettyLights(4, PrettyLights.Mode.IDLE);
		
		// begin
		this.setScreen(new MenuScreen());
	}

	@Override
	public void render() {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		super.render();
		
		if (TESTING)
			util.debugOverlay.draw(gfx.sysSB);
	}
	
	
}
