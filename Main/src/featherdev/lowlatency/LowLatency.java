package featherdev.lowlatency;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import featherdev.lowlatency.objects.Difficulty;
import featherdev.lowlatency.screens.SplashAndLoadScreen;

/***
 * platform-independent entry point
 * @author snowdrift
 *
 */

public class LowLatency extends Game {

	private static LowLatency instance;
	public static LowLatency instance(){
		return instance;
	}
	public static final String VERSION = "1.0";
	public static final boolean TESTING = false;
	
	public SpriteBatch batch;
	public Difficulty difficulties[];
	public Preferences settings;
	public Skin skin;
	public TextureAtlas textures;
	
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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		super.dispose();
	}

}
