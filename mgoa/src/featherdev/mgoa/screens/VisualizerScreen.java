package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.game.BackgroundManager;
import featherdev.mgoa.subsystems.MusicPlayer;

public class VisualizerScreen implements Screen {
	enum State { RUNNING, PAUSED, DONE }
	
	BackgroundManager bm;
	SpriteBatch batch;
	State state;
	Stage stage;
	
	
	public VisualizerScreen(){
		MusicPlayer.instance().load(Mgoa.instance().song.getHandle());
		bm = new BackgroundManager(Mgoa.instance().rawmap);
		bm.numlights(6);
		bm.setFountainOn(false);
		batch = Mgoa.instance().batch;
		stage = new Stage();
		TextButton unpause = new TextButton("Resume", Mgoa.instance().skin);
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
		TextButton quit = new TextButton("Quit to Main Menu", Mgoa.instance().skin);
		quit.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(null);
				Mgoa.instance().setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		Table root = new Table();
		root.defaults().pad(10);
		root.setSkin(Mgoa.instance().skin);
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
				Mgoa.instance().setScreen(new MenuScreen());
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
			Mgoa.instance().setScreen(new MenuScreen());
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
