package featherdev.mgoa;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import featherdev.lwbd.Beat;
import featherdev.mgoa.objects.Difficulty;
import featherdev.mgoa.objects.LightBox;
import featherdev.mgoa.objects.ScoreRecords;
import featherdev.mgoa.objects.Song;
import featherdev.mgoa.screens.SplashAndLoadScreen;


public class Mgoa extends Game {

	private static Mgoa instance;
	public static Mgoa getInstance(){
		return instance;
	}
	
	private OrthographicCamera cam;
	
	public static final String VERSION = "pre-alpha";
	public static final boolean TESTING = true;
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
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
		
		System.out.println("[+] Initialized GDX subsystems");
		System.out.println("[+] Initialized OpenGL context");
		
		setScreen(new SplashAndLoadScreen());

	}
	@Override
	public void render() {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		super.render();
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
