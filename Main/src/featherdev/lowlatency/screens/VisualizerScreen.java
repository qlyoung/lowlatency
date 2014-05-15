package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.game.BackgroundManager;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.MusicPlayer;

public class VisualizerScreen implements Screen {
	enum State { RUNNING, PAUSED, DONE }
	
	BackgroundManager bm;
	SpriteBatch batch;
	State state;
	Stage stage;
	
	
	public VisualizerScreen(){
		MusicPlayer.instance().load(Holder.song.getHandle());
		bm = new BackgroundManager(Holder.rawmap);
		bm.numlights(6);
		bm.setFountainOn(false);
		batch = LowLatency.instance().batch;
		stage = new Stage();
		TextButton unpause = new TextButton("Resume", LowLatency.instance().skin);
		unpause.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				if (state != State.DONE){
					state = State.RUNNING;
					Gdx.input.setInputProcessor(null);
					MusicPlayer.instance().play();
				}
				super.clicked(event, x, y);
			}
		});
		TextButton quit = new TextButton("Quit to Main Menu", LowLatency.instance().skin);
		quit.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(null);
				LowLatency.instance().setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		Table root = new Table();
		root.defaults().pad(10);
		root.setSkin(LowLatency.instance().skin);
		root.add("Paused");
		root.row();
		root.add(unpause);
		root.row();
		root.add(quit);
		root.setFillParent(true);
		stage.addActor(root);
		
	}

	public void render(float delta) {
		
		switch (state){
		case RUNNING:
			if (!MusicPlayer.instance().isPlaying())
				LowLatency.instance().setScreen(new MenuScreen());
			bm.update(delta);
			bm.draw(batch);
			if (Gdx.input.justTouched()){
				state = State.PAUSED;
				MusicPlayer.instance().pause();
				Gdx.input.setInputProcessor(stage);
			}
			break;
		case PAUSED:
			stage.act();
			stage.draw();
			break;
		case DONE:
			LowLatency.instance().setScreen(new MenuScreen());
			break;
		}
	}
	public void resize(int width, int height) {
		
	}
	public void show() {
		MusicPlayer.instance().play();
		state = State.RUNNING;
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
