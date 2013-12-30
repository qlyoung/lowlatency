package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.Song;
import com.sawtoothdev.mgoa.ui.component.AudioCtlButton;
import com.sawtoothdev.mgoa.ui.component.FileBrowser;

public class ChooseSongScreen implements Screen {

	private final Stage stage;
	FileBrowser browser;
	SelectBox selector;
	
	private final AudioCtlButton audioBtn = new AudioCtlButton();

	TextButtonStyle controlStyle = new TextButtonStyle();

	public ChooseSongScreen() {

		// stage setup
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// styles
		controlStyle.font = new BitmapFont(Gdx.files.internal("data/fonts/typeone.fnt"), false);
		controlStyle.fontColor = Color.WHITE;

		// controls
		TextButton homeButton = new TextButton("Main Menu", controlStyle);
		homeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				MGOA.game.setScreen(new MenuScreen());
			}
		});
		audioBtn.setPosition(10, Gdx.graphics.getHeight() - 30);

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

		{// update
			MGOA.temporals.lights.update(delta);
			audioBtn.update(delta);
			stage.act();
			
			if (browser.getSelection() != null)
				finish();
		}

		{// draw
			MGOA.temporals.lights.draw(null);
			stage.draw();
			audioBtn.draw(MGOA.gfx.defaultSpriteBatch);
			Table.drawDebug(stage);
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
	
	private void finish(){
		Gdx.input.setInputProcessor(null);
		MGOA.temporals.song = new Song(browser.getSelection());
		MGOA.game.setScreen(new ConfigScreen());
	}

}
