package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.Mgoa;

public class OptionsScreen implements Screen {

	Stage stage = new Stage();
	Table root = new Table();
	Mgoa game;

	public OptionsScreen() {
		game = Mgoa.getInstance();
		stage = new Stage();
		root = new Table();
		root.setFillParent(true);

		CheckBox fullscreen = new CheckBox("  Fullscreen", game.skin);
		CheckBox music		= new CheckBox("  Music", game.skin);
		TextButton backToMenu = new TextButton("Return", game.skin);
		fullscreen.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!Gdx.graphics.isFullscreen())
					Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
				else
					Gdx.graphics.setDisplayMode(1280, 720, false);
				
				game.setScreen(new OptionsScreen());
			}
		});
		music.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (game.menuMusic.isPlaying())
					game.menuMusic.pause();
				else
					game.menuMusic.play();
				
				game.settings.putBoolean("music", game.menuMusic.isPlaying());
				game.settings.flush();
			}
		});
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		
		music.setChecked(game.menuMusic.isPlaying());
		
		root.defaults().uniform().padBottom(30);
		root.add(new Label("Graphics Options", game.skin));
		root.row();
		root.add(fullscreen);
		root.row();
		root.add(music);
		root.row();
		root.add(backToMenu);

		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {

		{// update
			if (Gdx.input.isKeyPressed(Keys.ESCAPE))
				game.setScreen(new MenuScreen());
			stage.act();
		}

		{// draw
			stage.draw();
		}

	}
	@Override
	public void resize(int width, int height) {

	}
	@Override
	public void show() {
		stage.addAction(Actions.fadeIn(.5f));
	}
	@Override
	public void hide() {

	}
	@Override
	public void pause() {

	}
	@Override
	public void resume() {

	}
	@Override
	public void dispose() {

	}
}
