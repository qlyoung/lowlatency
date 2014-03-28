package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.objects.Difficulty;
import featherdev.mgoa.objects.LightBox;
import featherdev.mgoa.objects.ScoreRecords;

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
		Mgoa g = Mgoa.getInstance();
		
		g.textures = new TextureAtlas("textures/mgoa.atlas");
		System.out.println("[+] Loaded textures");
		
		g.batch = new SpriteBatch();
		
		g.lights = new LightBox();
		g.lights.setNumLights(Utilities.getRandomColor(), 5);
		
		g.difficulties = new Difficulty[3];
		g.difficulties[0] = new Difficulty(800, .01f, "Relaxed", 1);
		g.difficulties[1] = new Difficulty(650, .005f, "Normal", 2);
		g.difficulties[2] = new Difficulty(600, .003f, "Altered", 3);

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
		g.menuMusic = Gdx.audio.newMusic(musicpath);
		g.menuMusic.setLooping(true);
		g.menuMusic.setVolume(.5f);
		System.out.println("[+] Loaded audio");
		
		g.records = new ScoreRecords();
		System.out.println("[+] Loaded saved data");

		g.skin = new Skin(Gdx.files.internal("ui/skin.json"), g.textures);
		System.out.println("[+] Initialized ui");

		g.settings = Gdx.app.getPreferences("settings");
		g.settings.putBoolean("firstrun", !g.settings.contains("firstrun"));
		g.settings.flush();
		
		l = System.currentTimeMillis();
		m = new MenuScreen();
	}

	@Override
	public void render(float delta) {
		
		if (loaded){
			if (System.currentTimeMillis() - l > 2000 && System.currentTimeMillis() - l < 3000)
				return;
			else if (System.currentTimeMillis() - l >= 3000){
				Mgoa.getInstance().setScreen(m);
				System.out.println("[#] Initialization sequence complete.");
				return;
			}
		}
		else if (b) {
			load();
			loaded = true;
		}
		
		float x = (Gdx.graphics.getWidth() - t.getWidth()) / 2f;
		float y = (Gdx.graphics.getHeight() - t.getHeight()) / 2f;
		
		s.setProjectionMatrix(cam.combined);
		s.begin();
		s.draw(t, x, y);
		s.end();
		
		b = true;
		
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
	@Override
	public void show() {
		load();
		l = System.currentTimeMillis();
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
