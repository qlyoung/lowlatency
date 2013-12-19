package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.PrettyLights;
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
			playButton = new TextButton("Play", MGOA.ui.uiTextButtonStyle),
			optionsButton = new TextButton("Options", MGOA.ui.uiTextButtonStyle),
			statsButton = new TextButton("Leaderboards", MGOA.ui.uiTextButtonStyle),
			creditsButton = new TextButton("Credits", MGOA.ui.uiTextButtonStyle);

		// actors setup
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MGOA.game.setScreen(new ChooseSongScreen());
				super.clicked(event, x, y);
			}
		});
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MGOA.game.setScreen(new OptionsScreen());
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
			audioControl.draw(MGOA.gfx.defaultSpriteBatch);
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		MGOA.audio.menuMusic.setLooping(true);
		MGOA.audio.menuMusic.setVolume(1f);
		if (!MGOA.settings.contains("bgmusic")
				|| MGOA.settings.getBoolean("bgmusic"))
			MGOA.audio.menuMusic.play();
		
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
