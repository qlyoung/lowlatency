package com.sawtoothdev.mgoa.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.sawtoothdev.mgoa.GameScreen;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.objects.Song;

public class HeadsUpDisplay implements IUpdateable, IDrawable, Disposable {
	
	Stage stage;
	HashMap<String, Actor> tickers;
	Skin skin;
	OrthographicCamera screencam;
	
	public HeadsUpDisplay(Song song, final GameScreen gs){
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		screencam = new OrthographicCamera();
		screencam.setToOrtho(false);
		
		skin = Mgoa.getInstance().skin;
		tickers = new HashMap<String, Actor>();

		String songinfo = song.getArtist() + " - " + song.getTitle();
		
		Label scoreLabel = new Label("000000000", skin);
		tickers.put("points", scoreLabel);

		ImageButton pause = new ImageButton(skin);
		pause.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gs.pause();
			}
		});
		
		Table table = new Table(skin);
		table.setFillParent(true);
		table.add(pause).top().left();
		table.row().expandY();
		table.add(songinfo).expandX().bottom().left().pad(5);
		table.add(scoreLabel).bottom().right().pad(5);
		
		stage.addActor(table);
		stage.getRoot().getColor().a = 0;
	}

	@Override
	public void update(float delta) {
		stage.act(delta);
	}
	@Override
	public void draw(SpriteBatch batch) {
		stage.draw();
	}
		
	public void showMessage(String message, Vector2 screenPosition, float fadein, float fadeout, float live){
		Label msg = new Label(message, skin);
		msg.setPosition(screenPosition.x, screenPosition.y);
		Action set = Actions.sequence(
				Actions.fadeIn(fadein),
				Actions.delay(live),
				Actions.fadeOut(fadeout),
				Actions.removeActor());
		msg.addAction(set);
		stage.addActor(msg);
	}
	public void showDialog(Dialog d){
		d.show(stage);
	}
	public void setProgressBarPercent(float percentOutOfOne){
		//TODO: Implement
	}
	public void setPoints(int points) {
		Label scoreLabel = (Label) tickers.get("points");
		String text = String.format("%08d", points);
		
		scoreLabel.setText(text);
	}
	public void fadein(float time){
		stage.getRoot().addAction(Actions.fadeIn(time));
	}
	public void fadeout(float time){
		stage.getRoot().addAction(Actions.fadeOut(time));
	}
	public void setAsInputProcessor(){
		Gdx.input.setInputProcessor(stage);
	}
	public float getAlpha(){
		return stage.getRoot().getColor().a;
	}
	@Override
	public void dispose() {
		stage.dispose();
	}
}
