package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.FileBrowser;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.Song;

public class ChooseSongScreen implements Screen {

	private final Stage stage;
	FileBrowser browser;
	SelectBox selector;

	TextButtonStyle controlStyle = new TextButtonStyle();

	public ChooseSongScreen() {

		// stage setup
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// controls
		TextButton homeButton = new TextButton("Main Menu", MGOA.ui.uiTextButtonStyle);
		homeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				MGOA.game.setScreen(new MenuScreen());
			}
		});

		// layout
		browser = new FileBrowser(Gdx.files.external(""));
		
		Table root = new Table();
		root.setFillParent(true);
		
		root.add(homeButton);
		root.row();
		root.add(browser).expand().fill();

		stage.addActor(root);
	}

	@Override
	public void render(float delta) {

		// update
		MGOA.temporals.lights.update(delta);
		stage.act();
		if (browser.getSelection() != null)
			finish();

		// draw
		MGOA.temporals.lights.draw(null);
		stage.draw();
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
	
	private void finish(){
		Gdx.input.setInputProcessor(null);
		MGOA.temporals.song = new Song(browser.getSelection());
		MGOA.game.setScreen(new ConfigScreen());
	}

}
