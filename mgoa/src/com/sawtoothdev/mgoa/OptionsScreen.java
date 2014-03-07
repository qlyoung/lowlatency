package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsScreen implements Screen {

	private Stage stage = new Stage();
	private final Table root = new Table();
	Mgoa game;

	public OptionsScreen() {
		game = Mgoa.getInstance();
		
		Gdx.input.setInputProcessor(stage);
		stage.addActor(root);
		root.setFillParent(true);

		TextButton fullscreenToggle = new TextButton("Toggle Fullscreen", game.skin);
		fullscreenToggle.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				if (!Gdx.graphics.isFullscreen())
					Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
				else
					Gdx.graphics.setDisplayMode(1280, 720, false);
				
				game.setScreen(new OptionsScreen());
			}
		});
		TextButton backToMenu = new TextButton("Main Menu", game.skin);
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		TextButton musicOff = new TextButton("Toggle Music", game.skin);
		musicOff.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				boolean value = !game.settings.getBoolean("musicon");
				game.settings.putBoolean("musicon", value);
				
				if (value)
					game.menuMusic.play();
				else
					game.menuMusic.pause();
			}
		});
		
		root.defaults().uniform().padBottom(30);
		
		root.add(new Label("Graphics Options", game.skin));
		root.row();
		root.add(fullscreenToggle);
		root.row();
		root.add(musicOff);
		root.row();
		root.add(backToMenu);

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
