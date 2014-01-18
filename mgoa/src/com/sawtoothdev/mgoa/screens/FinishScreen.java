package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MGOA;
import com.sawtoothdev.mgoa.Stats;

public class FinishScreen implements Screen {

	private Stage stage = new Stage();
	private Table root = new Table();
	
	public FinishScreen(){
		// init
		Gdx.input.setInputProcessor(stage);
		root.setFillParent(true);
		
		LabelStyle ls = MGOA.ui.uiLabelStyle;
		
		String songinfo = MGOA.temporals.song.getArtist() + " - " + MGOA.temporals.song.getTitle();
		Label songLabel = new Label(songinfo, ls);
		TextButton okay = new TextButton("OK", MGOA.ui.uiTextButtonStyle);
		okay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MGOA.game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		
		Table summary = new Table();
		summary.add(new Label("Raw Points", ls)).left();
		summary.add(new Label(String.valueOf(MGOA.temporals.stats.points), ls)).right();
		summary.row();
		summary.add(new Label("Difficulty Multiplier", ls)).left();
		summary.add(new Label(String.valueOf(MGOA.temporals.difficulty.scoreMultiplier), ls)).right();

		root.add(songLabel);
		root.row();
		root.add(summary);
		root.row();
		root.add(okay);
		
		stage.addActor(root);
	}

	private void writeStats(Stats stats){
		FileHandle statsFile = Gdx.files.local("corndog");
		
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
