package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.PrettyLights;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.UIResources;
import com.sawtoothdev.mgoa.ui.component.AudioCtlButton;

public class MenuScreen implements Screen {

	private Stage stage = new Stage();
	Table root = new Table();
	
	private PrettyLights prettyLights = new PrettyLights(15);
	
	AudioCtlButton audioControl = new AudioCtlButton();
	
	public MenuScreen() {
		
		// stage setup
		Gdx.input.setInputProcessor(stage);

		// actors
		TextButton
			playButton = new TextButton("Play", UIResources.uiTextButtonStyle),
			optionsButton = new TextButton("Options", UIResources.uiTextButtonStyle),
			statsButton = new TextButton("Leaderboards", UIResources.uiTextButtonStyle),
			creditsButton = new TextButton("Credits", UIResources.uiTextButtonStyle);

		// actors setup
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Resources.game.setScreen(new ChooseSongScreen());
				super.clicked(event, x, y);
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Resources.game.setScreen(new OptionsScreen());
				super.clicked(event, x, y);
			}
		});

		// scene layout
		root.setFillParent(true);
		root.defaults().uniform().padBottom(10);

		root.add(playButton);
		root.row();
		root.add(optionsButton);
		root.row();
		root.add(statsButton);
		root.row();
		root.add(creditsButton);
		root.row();

		stage.addActor(root);

		audioControl.setPosition(10, Gdx.graphics.getHeight() - 30);
	}

	@Override
	public void render(float delta) {

		{// update
			prettyLights.update(delta);
			stage.act(delta);
			audioControl.update(delta);
		}

		{// draw
			prettyLights.draw(null);
			stage.draw();
			audioControl.draw(Resources.defaultSpriteBatch);
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		Resources.menuMusic.setLooping(true);
		Resources.menuMusic.setVolume(1f);
		if (!Resources.settings.contains("bgmusic")
				|| Resources.settings.getBoolean("bgmusic"))
			Resources.menuMusic.play();
		
		System.gc();
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
