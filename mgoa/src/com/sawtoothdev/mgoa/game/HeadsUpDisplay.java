package com.sawtoothdev.mgoa.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.sawtoothdev.mgoa.Drawable;
import com.sawtoothdev.mgoa.Updateable;
import com.sawtoothdev.mgoa.objects.Song;
import com.sawtoothdev.mgoa.objects.Stats;

public class HeadsUpDisplay implements Updateable, Drawable, Disposable {
	
	private Stage stage;
	private HashMap<String, Actor> tickers;
	private Stats stats;
	private Skin skin;
	
	public HeadsUpDisplay(Song song, Skin skin, Stats stat, SpriteBatch batch){
		this.skin = skin;
		stats = stat;
		tickers = new HashMap<String, Actor>();
		Table base = new Table(skin);
		base.setFillParent(true);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		stage.addActor(base);
		
		String songinfo = song.getArtist() + " - " + song.getTitle();
		
		Label scoreLabel = new Label("000000000", skin);
		tickers.put("score", scoreLabel);

		base.add(songinfo).expand().bottom().left().pad(10);
		base.add(scoreLabel).bottom().right().pad(10);
	}
	
	@Override
	public void update(float delta) {
		Label scoreLabel = (Label) tickers.get("score");
		scoreLabel.setText(String.valueOf(stats.points));
		
		stage.act();
	}
	@Override
	public void draw(SpriteBatch batch) {
		stage.draw();
	}
	
	public Stats getStats(){
		return stats;
	}
	public void showMessage(String message){
		Label msg = new Label(message, skin);
		msg.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
		Action set = Actions.sequence(Actions.fadeOut(1), Actions.removeActor());
		msg.addAction(set);
		stage.addActor(msg);
	}
	public void fadein(){
		stage.getRoot().getColor().a = 0;
		stage.addAction(Actions.fadeIn(2));
	}
	public void fadeout(){
		stage.addAction(Actions.fadeOut(1));
	}
	public float getAlpha(){
		return stage.getRoot().getColor().a;
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
