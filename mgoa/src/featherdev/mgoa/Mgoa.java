package featherdev.mgoa;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import featherdev.lwbd.Beat;
import featherdev.mgoa.objects.Difficulty;
import featherdev.mgoa.objects.LightBox;
import featherdev.mgoa.objects.ScoreRecords;
import featherdev.mgoa.objects.Song;
import featherdev.mgoa.screens.MenuScreen;
import featherdev.mgoa.screens.Utilities;

/**
 * Main entry point. Hooks into GDX.
 * 
 * @author albatross
 * 
 */

public class Mgoa extends Game {

	private static Mgoa instance;
	public static Mgoa getInstance(){
		return instance;
	}
	
	public static final String VERSION = "pre-alpha";
	public static final boolean TESTING = true;
	
	private BitmapFont font;
	private Camera cam;
	
	public SpriteBatch batch;
	public LightBox lights;
	public Music menuMusic;
	public Difficulty difficulties[];
	public Difficulty difficulty;
	public LinkedList<Beat> beatmap;
	public LinkedList<Beat> rawmap;
	public Preferences settings;
	public ScoreRecords records;
	public Song song;
	public Skin skin;
	public TextureAtlas textures;
	
	@Override
	public void create() {
		instance = this;
		
		textures = new TextureAtlas("textures/mgoa.atlas");
		font = new BitmapFont();
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		
		lights = new LightBox(5, Utilities.getRandomColor());
		
		difficulties = new Difficulty[3];
		difficulties[0] = new Difficulty(800, .01f, "Relaxed", 1);
		difficulties[1] = new Difficulty(650, .005f, "Normal", 2);
		difficulties[2] = new Difficulty(600, .003f, "Altered", 3);

		FileHandle musicpath;
		switch (Gdx.app.getType()) {
		case Android:
			// work around Music's inability to load Internal files on Android
			Gdx.files.internal("audio/title.mp3").copyTo( Gdx.files.local("title.mp3"));
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

		records = new ScoreRecords();

		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/mgoa.atlas"));
		skin = new Skin(Gdx.files.internal("ui/skin.json"), atlas);

		settings = Gdx.app.getPreferences("settings");
		settings.putBoolean("firstrun", !settings.contains("firstrun"));
		settings.flush();

		setScreen(new MenuScreen());
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

}
