package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MainGame;

public class FinishScreen implements Screen {

	private Stage stage = new Stage();
	private Table root = new Table();
	
	public FinishScreen(){
		// init
		Gdx.input.setInputProcessor(stage);
		root.setFillParent(true);
		
		LabelStyle ls = MainGame.Ui.skin.get("menuLabelStyle", LabelStyle.class);
		TextButtonStyle tbstyle = MainGame.Ui.skin.get("menuTextButtonStyle", TextButtonStyle.class);
		
		String songinfo = MainGame.Temporal.song.getArtist() + " - " + MainGame.Temporal.song.getTitle();
		Label songLabel = new Label(songinfo, ls);
		TextButton okay = new TextButton("OK", tbstyle);
		okay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainGame.game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		
		Table summary = new Table();
		summary.add(new Label("Raw Points", ls)).left();
		summary.add(new Label(String.valueOf(MainGame.Temporal.stats.points), ls)).right();
		summary.row();
		summary.add(new Label("Difficulty Multiplier", ls)).left();
		summary.add(new Label(String.valueOf(MainGame.Temporal.difficulty.scoreMultiplier), ls)).right();

		root.add(songLabel);
		root.row();
		root.add(summary);
		root.row();
		root.add(okay);
		
		stage.addActor(root);
	}

	@Override
	public void render(float delta) {
		
		stage.act();
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

}
