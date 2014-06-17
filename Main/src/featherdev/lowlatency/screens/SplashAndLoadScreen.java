package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.objects.Difficulty;
import featherdev.lowlatency.subsystems.MusicPlayer;
import featherdev.lowlatency.subsystems.ScoreRecords;

/**
 * loads everything
 * @author snowdrift
 */
public class SplashAndLoadScreen implements Screen {
	
	Texture splash;
	OrthographicCamera cam;
	SpriteBatch s;
	long l;
	boolean b, loaded;
	
	public SplashAndLoadScreen(){
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
		splash = new Texture(Gdx.files.internal("textures/splash.png"));
		s = new SpriteBatch();
		loaded = b = false;
		l = 0;
	}
	
	private void load(){
		LowLatency g = LowLatency.instance();
		
		// textures
		g.textures = new TextureAtlas("textures/mgoa.atlas");
		Gdx.app.log("[+]", "Loaded textures");
		
		// graphics
		g.batch = new SpriteBatch();
		
		// difficulties
		g.difficulties = new Difficulty[3];
		g.difficulties[0] = new Difficulty(800, .01f, "Relaxed", 1);
		g.difficulties[1] = new Difficulty(650, .005f, "Normal", 2);
		g.difficulties[2] = new Difficulty(550, .003f, "Altered", 3);

		// music
		MusicPlayer.instance();
		Gdx.app.log("[+]", "Initialized music player");
		ScoreRecords.instance();
		Gdx.app.log("[+]", "Initialized records");
		g.skin = new Skin(Gdx.files.internal("ui/skin.json"), g.textures);
		Gdx.app.log("[+]", "Initialized ui");

		// settings
		g.settings = Gdx.app.getPreferences("settings");
		g.settings.putBoolean("firstrun", !g.settings.contains("firstrun"));
		g.settings.flush();
		Gdx.app.log("[+]", "Loaded settings");
		
		l = System.currentTimeMillis();
	}
	
	public void render(float delta) {

        // we need the splash to draw before we call load()
        // 'b' indicates whether we've drawn the splash yet
		if (b && !loaded){
			load();
			loaded = true;
		}
		if (loaded){
			if (System.currentTimeMillis() - l > 2000 && System.currentTimeMillis() - l < 3000)
				return;
			else if (System.currentTimeMillis() - l >= 3000){
				LowLatency.instance().setScreen(new MenuScreen());
				Gdx.app.log("[+]", "Load complete.");
				return;
			}
		}

        // 'b' is a marker that indicates whether this next bit has
        // executed yet
		float x = (Gdx.graphics.getWidth() - splash.getWidth()) / 2f;
		float y = (Gdx.graphics.getHeight() - splash.getHeight()) / 2f;
		
		s.setProjectionMatrix(cam.combined);
		s.begin();
		s.draw(splash, x, y);
		s.end();
		
		b = true;
		
	}
	public void resize(int width, int height) {

	}
	public void show() {
		l = System.currentTimeMillis();
	}
	public void hide() {

	}
	public void pause() {

	}
	public void resume() {

	}
	public void dispose() {

	}

}
