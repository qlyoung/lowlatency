package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.Utilities;
import featherdev.mgoa.objects.Difficulty;
import featherdev.mgoa.objects.LightBox;
import featherdev.mgoa.subsystems.MusicPlayer;
import featherdev.mgoa.subsystems.ScoreRecords;

/**
 * easily the most disgusting class I've ever written
 * @author snowdrift
 */
public class SplashAndLoadScreen implements Screen {
	
	Texture t;
	OrthographicCamera cam;
	SpriteBatch s;
	MenuScreen m;
	long l;
	boolean b, loaded;
	
	public SplashAndLoadScreen(){
		cam = new OrthographicCamera();
		cam.setToOrtho(false);
		t = new Texture(Gdx.files.internal("textures/splash.png"));
		s = new SpriteBatch();
		loaded = b = false;
		l = 0;
		
	}
	
	private void load(){
		Mgoa g = Mgoa.instance();
		
		g.textures = new TextureAtlas("textures/mgoa.atlas");
		Gdx.app.log("[+]", "Loaded textures");
		
		g.batch = new SpriteBatch();
		
		g.lights = new LightBox();
		g.lights.setNumLights(Utilities.getRandomColor(), 5);
		
		g.difficulties = new Difficulty[3];
		g.difficulties[0] = new Difficulty(800, .01f, "Relaxed", 1);
		g.difficulties[1] = new Difficulty(650, .005f, "Normal", 2);
		g.difficulties[2] = new Difficulty(600, .003f, "Altered", 3);

		MusicPlayer.instance();
		Gdx.app.log("[+]", "Initialized music player");
		ScoreRecords.instance();
		Gdx.app.log("[+]", "Initialized records");
		g.skin = new Skin(Gdx.files.internal("ui/skin.json"), g.textures);
		Gdx.app.log("[+]", "Initialized ui");

		g.settings = Gdx.app.getPreferences("settings");
		g.settings.putBoolean("firstrun", !g.settings.contains("firstrun"));
		g.settings.flush();
		Gdx.app.log("[+]", "Loaded settings");
		
		g.visualizer = false;
		
		l = System.currentTimeMillis();
		m = new MenuScreen();
	}
	
	public void render(float delta) {
		
		if (b && !loaded){
			load();
			loaded = true;
		}
		if (loaded){
			if (System.currentTimeMillis() - l > 2000 && System.currentTimeMillis() - l < 3000)
				return;
			else if (System.currentTimeMillis() - l >= 3000){
				Mgoa.instance().setScreen(m);
				System.out.println("[#] Initialization sequence complete.");
				return;
			}
		}
		
		float x = (Gdx.graphics.getWidth() - t.getWidth()) / 2f;
		float y = (Gdx.graphics.getHeight() - t.getHeight()) / 2f;
		
		s.setProjectionMatrix(cam.combined);
		s.begin();
		s.draw(t, x, y);
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
