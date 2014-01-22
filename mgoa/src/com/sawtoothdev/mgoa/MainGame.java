package com.sawtoothdev.mgoa;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.sawtoothdev.mgoa.objects.Stats;
import com.sawtoothdev.mgoa.screens.MenuScreen;

/**
 * Floor zero.
 * Global resources, bootstrapping, Screen delegation and preempts.
 * 
 * Data all the way down!
 * 
 * @author albatross
 *
 */

public class MainGame extends Game {
	
	public static final String VERSION = "pre-alpha";
	public static final boolean TESTING = true;
	
	public static class Util {
		public static Random random;
		public static DebugOverlay debugOverlay;
		public static Difficulty difficulties[];
	}
	public static class Audio {
		public static Music menuMusic;
	}
	public static class Gfx {
		public static SpriteBatch systemBatch;
		public static OrthographicCamera screenCam, worldCam;
		public static PrettyLights lights;
		public static Vector2 screenToWorld(Vector2 screenCoords){
			Vector3 v3 = new Vector3(screenCoords.x, screenCoords.y, 0);
			worldCam.unproject(v3);
			Vector2 v2 = new Vector2(v3.x, v3.y);
			return v2;
		}
		public static Vector2 worldToScreen(Vector2 worldCoords){
			Vector3 v3 = new Vector3(worldCoords.x, worldCoords.y, 0);
			worldCam.project(v3);
			Vector2 v2 = new Vector2(v3.x, v3.y);
			return v2;
		}
	}
	public static class Ui {
		public static Skin skin;
		public static Stats stats;
	}
	public static class Temporal {
		public static Song song;
		public static Difficulty difficulty;
		public static LinkedList<Beat> beatmap;
		public static LinkedList<Beat> rawmap;
		public static Stats stats;
	}
	public static Game game;
	
	public static Preferences settings;
	
	@Override
	public void create() {
		
		// init
		
		// util
		Difficulty[] diffs = new Difficulty[3];
		diffs[0] = new Difficulty(800, 250, "Relaxed", 1);
		diffs[1] = new Difficulty(650, 150, "Normal", 2);
		diffs[2] = new Difficulty(600, 120, "Altered", 3);
		Util.difficulties = diffs;
		Util.random = new Random();
		Util.debugOverlay = new DebugOverlay();

		// audio
		FileHandle musicpath;
		switch (Gdx.app.getType()){
		case Android:
			// work around Music's inability to load 'internal' files on Android
			Gdx.files.internal("audio/title.mp3").copyTo(Gdx.files.local("title.mp3"));
			musicpath = Gdx.files.local("title.mp3");
			break;
		case Desktop:
			musicpath = Gdx.files.internal("audio/title.mp3");
			break;
		default:
			musicpath = null;
		}
		Audio.menuMusic = Gdx.audio.newMusic(musicpath);
		Audio.menuMusic.setLooping(true);
		Audio.menuMusic.setVolume(.5f);
		Audio.menuMusic.play();
		
		// gfx
		Gfx.systemBatch = new SpriteBatch();
		Gfx.screenCam = new OrthographicCamera();
		Gfx.screenCam.setToOrtho(false);
		Gfx.worldCam = new OrthographicCamera(10, 6);
		Gfx.lights = new PrettyLights(4, Mode.IDLE, Gfx.worldCam);
		
		// ui
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
		Ui.skin = new Skin(Gdx.files.internal("ui/uiskin.json"), atlas);
		
		settings = Gdx.app.getPreferences("settings");
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
			Util.debugOverlay.draw(Gfx.systemBatch);
	}

}
