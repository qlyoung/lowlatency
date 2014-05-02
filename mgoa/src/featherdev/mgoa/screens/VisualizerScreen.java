package featherdev.mgoa.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.game.BackgroundManager;
import featherdev.mgoa.subsystems.MusicPlayer;

public class VisualizerScreen implements Screen {
	
	BackgroundManager bm;
	SpriteBatch batch;
	
	public VisualizerScreen(){
		MusicPlayer.instance().load(Mgoa.instance().song.getHandle());
		bm = new BackgroundManager(Mgoa.instance().rawmap);
		bm.numlights(6);
		bm.setFountainOn(false);
		batch = Mgoa.instance().batch;
	}

	@Override
	public void render(float delta) {
		bm.update(delta);
		bm.draw(batch);
		
		if (!MusicPlayer.instance().isPlaying())
			Mgoa.instance().setScreen(new MenuScreen());
	}
	public void resize(int width, int height) {
		
	}
	public void show() {
		MusicPlayer.instance().play();
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
