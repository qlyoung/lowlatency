package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.objects.Stats;

public class FinishScreen implements Screen {

	private Stage stage = new Stage();
	private Table root = new Table();
	Stats stats;
	Mgoa game;
	
	public FinishScreen(Mgoa gam, Stats stat){
		game = gam;
		stats = stat;

		game.records.writeScore(game.song.getHandle(), stats.points);
		
		Gdx.input.setInputProcessor(stage);
		root.setFillParent(true);
		
		LabelStyle ls = game.skin.get("menuLabelStyle", LabelStyle.class);
		TextButtonStyle tbstyle = game.skin.get("menuTextButtonStyle", TextButtonStyle.class);
		
		String songinfo = game.song.getArtist() + " - " + game.song.getTitle();
		Label songLabel = new Label(songinfo, ls);
		TextButton okay = new TextButton("OK", tbstyle);
		okay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
				super.clicked(event, x, y);
			}
		});
		
		Table summary = new Table();
		summary.add(new Label("Raw Points", ls)).left();
		summary.add(new Label(String.valueOf(stats.points), ls)).right();
		summary.row();
		summary.add(new Label("Difficulty Multiplier", ls)).left();
		summary.add(new Label(String.valueOf(game.difficulty.scoreMultiplier), ls)).right();

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
