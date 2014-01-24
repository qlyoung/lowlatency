package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Mgoa;

public class OptionsScreen implements Screen {

	private Stage stage = new Stage();
	private final Table root = new Table();
	Mgoa game;

	public OptionsScreen(Mgoa gam) {
		game = gam;
		Gdx.input.setInputProcessor(stage);
		stage.addActor(root);
		root.setFillParent(true);

		TextButton fullscreenToggle = new TextButton("Toggle Fullscreen", game.skin, "menuTextButtonStyle");
		fullscreenToggle.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!Gdx.graphics.isFullscreen()) {
					Gdx.graphics.setDisplayMode(
							Gdx.graphics.getDesktopDisplayMode().width,
							Gdx.graphics.getDesktopDisplayMode().height, true);
					game.setScreen(new OptionsScreen(game));
				} else {
					Gdx.graphics.setDisplayMode(1280, 720, false);
					game.setScreen(new OptionsScreen(game));
				}

				super.clicked(event, x, y);
			}
		});
		TextButton backToMenu = new TextButton("Main Menu", game.skin, "menuTextButtonStyle");
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
				super.clicked(event, x, y);
			}
		});

		root.add(new Label("---Graphics Options---", game.skin, "menuLabelStyle"));
		root.row();
		root.add(fullscreenToggle);
		root.row();
		root.add(backToMenu);

	}

	@Override
	public void render(float delta) {

		{// update
			if (Gdx.input.isKeyPressed(Keys.ESCAPE))
				game.setScreen(new MenuScreen(game));
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
