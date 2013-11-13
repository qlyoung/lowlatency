package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.sawtoothdev.mgoa.PrettyLights;
import com.sawtoothdev.mgoa.Resources;

public class MenuScreen implements Screen {

	private Stage stage = new Stage();
	private PrettyLights prettyLights = new PrettyLights(
			new OrthographicCamera(10, 6), 15);
	AudioControl audioControl = new AudioControl();

	public MenuScreen() {
		Gdx.input.setInputProcessor(stage);

		TextButtonStyle style = new TextButtonStyle();
		style.font = new BitmapFont(
				Gdx.files.internal("data/fonts/naipol.fnt"), false);
		style.up = new TextureRegionDrawable(new TextureRegion(new Texture(
				"data/textures/ui/menubutton.png")));

		TextButton playButton = new TextButton("Play", style), optionsButton = new TextButton(
				"Options", style), statsButton = new TextButton("Leaderboards",
				style), creditsButton = new TextButton("Credits", style);

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

		Table table = new Table();
		table.setFillParent(true);
		table.defaults().uniform().padBottom(10);

		table.add(playButton);
		table.row();
		table.add(optionsButton);
		table.row();
		table.add(statsButton);
		table.row();
		table.add(creditsButton);
		table.row();

		stage.addActor(table);
		
		audioControl.setPosition(20, 20);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		prettyLights.update(delta);
		stage.act(delta);
		audioControl.update(delta);
		
		prettyLights.draw(null);
		stage.draw();
		audioControl.draw(Resources.defaultSpriteBatch);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		Resources.menuMusic.setLooping(true);
		Resources.menuMusic.setVolume(.4f);
		if (Resources.settings.getBoolean("bgmusic"))
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
