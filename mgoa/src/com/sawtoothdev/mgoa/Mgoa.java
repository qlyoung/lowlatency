package com.sawtoothdev.mgoa;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sawtoothdev.audioanalysis.Beat;
import com.sawtoothdev.mgoa.objects.Difficulty;
import com.sawtoothdev.mgoa.objects.LightBox;
import com.sawtoothdev.mgoa.objects.LightBox.Mode;
import com.sawtoothdev.mgoa.objects.ScoreRecords;
import com.sawtoothdev.mgoa.objects.Song;

/**
 * Main entry point. Hooks into GDX.
 * 
 * @author albatross
 * 
 */

public class Mgoa extends Game {

	public static final String VERSION = "pre-alpha";
	public static final boolean TESTING = true;
	
	private BitmapFont font;
	private Camera cam = new OrthographicCamera();
	
	public Random random;
	public Difficulty difficulties[];
	public Music menuMusic;
	public SpriteBatch batch;
	public LightBox lights;
	public Skin skin;
	public Song song;
	public Difficulty difficulty;
	public LinkedList<Beat> beatmap;
	public LinkedList<Beat> rawmap;
	public Preferences settings;
	public ScoreRecords records;

	@Override
	public void create() {

		font = new BitmapFont();
		
		// util
		Difficulty[] diffs = new Difficulty[3];
		diffs[0] = new Difficulty(800, .10f, "Relaxed", 1);
		diffs[1] = new Difficulty(650, .05f, "Normal", 2);
		diffs[2] = new Difficulty(600, .03f, "Altered", 3);
		difficulties = diffs;
		random = new Random();

		// audio
		FileHandle musicpath;
		switch (Gdx.app.getType()) {
		case Android:
			// work around Music's inability to load Internal
			// files on Android
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

		// gfx
		batch = new SpriteBatch();
		lights = new LightBox(Mode.IDLE, random);
		for (int i = 0; i < 5; i++)
			lights.addLight(Color.WHITE, 1f);

		// scoring
		records = new ScoreRecords();

		// ui
		TextureAtlas atlas = new TextureAtlas(
				Gdx.files.internal("ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"), atlas);

		// settings
		settings = Gdx.app.getPreferences("settings");
		settings.putBoolean("firstrun", !settings.contains("firstrun"));
		settings.flush();

		// begin
		setScreen(new MenuScreen(this));
	}

	@Override
	public void render() {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		super.render();

		if (TESTING) {
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
				font.draw(
					batch,
					VERSION,
					Gdx.graphics.getWidth() - 120,
					Gdx.graphics.getHeight());
				font.draw(
					batch,
					String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps",
					Gdx.graphics.getWidth() - 120,
					Gdx.graphics.getHeight() - font.getCapHeight() - 10);
			batch.end();
		}
	}

	@Override
	public void dispose() {
		menuMusic.dispose();
		batch.dispose();
		lights.dispose();
		skin.dispose();
		super.dispose();
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
