package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
		
		String songinfo = game.song.getArtist() + " - " + game.song.getTitle();
		Label songLabel = new Label(songinfo, game.skin);
		TextButton okay = new TextButton("OK", game.skin);
		okay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
				super.clicked(event, x, y);
			}
		});
		
		Table summary = new Table();
		summary.add(new Label("Raw Points", game.skin)).left();
		summary.add(new Label(String.valueOf(stats.points), game.skin)).right();
		summary.row();
		summary.add(new Label("Difficulty Multiplier", game.skin)).left();
		summary.add(new Label(String.valueOf(game.difficulty.scoreMultiplier), game.skin)).right();

		root.add(songLabel);
		root.row();
		root.add(summary);
		root.row();
		root.add(okay);
		
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
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
		stage.addAction(Actions.fadeIn(1));
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
