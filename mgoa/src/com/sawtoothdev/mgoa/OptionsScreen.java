package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionsScreen implements Screen {

	private Stage stage = new Stage();
	private final Table root = new Table();

	LabelStyle lblStyle = new LabelStyle();
	TextButtonStyle tbStyle = new TextButtonStyle();
	Label bgMusicOnLbl;
	TextButton bgMusicSwitch;

	public OptionsScreen() {
		Gdx.input.setInputProcessor(stage);
		stage.addActor(root);
		root.setFillParent(true);

		lblStyle.font = Resources.uiFnt;
		tbStyle.font = Resources.uiFnt;
		bgMusicOnLbl = new Label("Background music: ", lblStyle);
		bgMusicSwitch = new TextButton(Resources.settings.getBoolean("bgmusic") ? "ON" : "OFF", tbStyle);
		bgMusicSwitch.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				// change the setting
				boolean musicOn = !Resources.settings.getBoolean("bgmusic");
				Resources.settings.putBoolean("bgmusic", musicOn);

				// change the button text
				bgMusicSwitch.setText(musicOn ? "ON" : "OFF");
				
				// do what's necessary to the music
				if (musicOn)
					Resources.menuMusic.play();
				else
					Resources.menuMusic.pause();

				super.clicked(event, x, y);
			}
		});

		root.add(bgMusicOnLbl);
		root.add(bgMusicSwitch);

	}

	@Override
	public void render(float delta) {

		stage.act();

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			Resources.game.setScreen(new MenuScreen());
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
