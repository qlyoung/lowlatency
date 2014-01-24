package com.sawtoothdev.mgoa;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.objects.Difficulty;
import com.sawtoothdev.mgoa.objects.PrettyLights;
import com.sawtoothdev.mgoa.objects.PrettyLights.Mode;
import com.sawtoothdev.mgoa.objects.Song;
import com.sawtoothdev.mgoa.screens.MenuScreen;

/**
 * Floor zero. Global resources, bootstrapping, Screen delegation and preempts.
 * 
 * Data all the way down!
 * 
 * @author albatross
 * 
 */

public class Mgoa extends Game {

	public static final String VERSION = "pre-alpha";
	public static final boolean TESTING = true;

	private DebugOverlay debugOverlay;
	public Random random;
	public Difficulty difficulties[];
	public Music menuMusic;
	public SpriteBatch batch;
	public PrettyLights lights;
	public Skin skin;
	public Song song;
	public Difficulty difficulty;
	public LinkedList<Beat> beatmap;
	public LinkedList<Beat> rawmap;
	public Preferences settings;

	@Override
	public void create() {

		// util
		Difficulty[] diffs = new Difficulty[3];
		diffs[0] = new Difficulty(800, 250, "Relaxed", 1);
		diffs[1] = new Difficulty(650, 150, "Normal", 2);
		diffs[2] = new Difficulty(600, 120, "Altered", 3);
		difficulties = diffs;
		random = new Random();
		debugOverlay = new DebugOverlay();

		// audio
		FileHandle musicpath;
		switch (Gdx.app.getType()) {
		case Android:
			// work around Music's inability to load 'internal' files on Android
			Gdx.files.internal("audio/title.mp3").copyTo(
					Gdx.files.local("title.mp3"));
			musicpath = Gdx.files.local("title.mp3");
			break;
		case Desktop:
			musicpath = Gdx.files.internal("audio/title.mp3");
			break;
		default:
			musicpath = null;
		}
		menuMusic = Gdx.audio.newMusic(musicpath);
		menuMusic.setLooping(true);
		menuMusic.setVolume(.5f);
		menuMusic.play();

		// gfx
		batch = new SpriteBatch();
		lights = new PrettyLights(4, Mode.IDLE, random);

		// ui
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"), atlas);

		// other
		settings = Gdx.app.getPreferences("settings");
		Gdx.app.log("launch", Boolean.toString(settings.contains("firstrun")));
		settings.putBoolean("firstrun", !settings.contains("firstrun"));
		settings.flush();

		// begin
		setScreen(new MenuScreen(this));
	}

	@Override
	public void render() {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		super.render();

		if (TESTING)
			debugOverlay.draw(batch);
	}

	public static Vector2 screenToWorld(Vector2 screenCoords, Camera worldCam) {
		Vector3 v3 = new Vector3(screenCoords.x, screenCoords.y, 0);
		worldCam.unproject(v3);
		Vector2 v2 = new Vector2(v3.x, v3.y);
		return v2;
	}
	public static Vector2 worldToScreen(Vector2 worldCoords, Camera worldCam) {
		Vector3 v3 = new Vector3(worldCoords.x, worldCoords.y, 0);
		worldCam.project(v3);
		Vector2 v2 = new Vector2(v3.x, v3.y);
		return v2;
	}

}
