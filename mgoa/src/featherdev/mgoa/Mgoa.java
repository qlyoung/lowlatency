package featherdev.mgoa;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import featherdev.lwbd.Beat;
import featherdev.mgoa.objects.Difficulty;
import featherdev.mgoa.objects.LightBox;
import featherdev.mgoa.objects.Song;
import featherdev.mgoa.screens.SplashAndLoadScreen;

/***
 * platform-independent entry point
 * @author snowdrift
 *
 */

public class Mgoa extends Game {

	private static Mgoa instance;
	public static Mgoa instance(){
		return instance;
	}
	public static final String VERSION = "1.0";
	public static final boolean TESTING = false;
	
	public SpriteBatch batch;
	public LightBox lights;
	public Difficulty difficulties[];
	public Preferences settings;
	public Skin skin;
	public TextureAtlas textures;
	
	public Difficulty difficulty;
	public LinkedList<Beat> beatmap;
	public LinkedList<Beat> rawmap;
	public Song song;
	public boolean visualizer;
	
	@Override
	public void create() {
		instance = this;
		
		Gdx.app.log("[+]", "Initialized GDX subsystems");
		Gdx.app.log("[+]", "Initialized OpenGL context");
		Gdx.app.log("[+]", "Loading assets...");
		setScreen(new SplashAndLoadScreen());

	}
	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	@Override
	public void dispose() {
		batch.dispose();
		lights.dispose();
		skin.dispose();
		super.dispose();
	}

}
