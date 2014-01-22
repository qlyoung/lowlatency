package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MainGame;
import com.sawtoothdev.mgoa.objects.PrettyLights.Mode;

public class MenuScreen implements Screen {

	private Stage stage = new Stage();
	Table root = new Table();
	
	public MenuScreen() {
		
		// stage setup
		Gdx.input.setInputProcessor(stage);
		
		// lights setup
		MainGame.Gfx.lights.setMode(Mode.IDLE);
		
		// actors
		TextButtonStyle tbstyle = MainGame.Ui.skin.get("menuTextButtonStyle", TextButtonStyle.class);
		TextButton
			playButton = new TextButton("Play", tbstyle),
			optionsButton = new TextButton("Options", tbstyle),
			statsButton = new TextButton("Leaderboards", tbstyle),
			creditsButton = new TextButton("Credits", tbstyle);

		// actors setup
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainGame.game.setScreen(new ChooseSongScreen());
				super.clicked(event, x, y);
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainGame.game.setScreen(new OptionsScreen());
				super.clicked(event, x, y);
			}
		});

		// scene layout
		root.setFillParent(true);
		root.defaults().uniform().padBottom(30);

		root.add(playButton);
		root.row();
		root.add(optionsButton);
		root.row();
		root.add(statsButton);
		root.row();
		root.add(creditsButton);
		root.row();

		stage.addActor(root);

	}

	@Override
	public void render(float delta) {

		{// update
			MainGame.Gfx.lights.update(delta);
			stage.act(delta);
		}

		{// draw
			MainGame.Gfx.lights.draw(null);
			stage.draw();
			//audioControl.draw(MGOA.Gfx.defaultSpriteBatch);
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
