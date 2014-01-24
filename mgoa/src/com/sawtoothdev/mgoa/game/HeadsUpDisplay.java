package com.sawtoothdev.mgoa.game;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sawtoothdev.mgoa.IDrawable;
import com.sawtoothdev.mgoa.IUpdateable;
import com.sawtoothdev.mgoa.objects.Song;
import com.sawtoothdev.mgoa.objects.Stats;

public class HeadsUpDisplay implements IUpdateable, IDrawable {
	
	private Stage stage;
	private HashMap<String, Actor> tickers;
	private Stats stats;
	
	public HeadsUpDisplay(Song song, Skin skin, Stats stat){
		stats = stat;
		tickers = new HashMap<String, Actor>();
		Table base = new Table(skin);
		base.setFillParent(true);
		
		stage = new Stage();
		stage.addActor(base);
		
		String songinfo = song.getArtist() + " - " + song.getTitle();
		
		Label scoreLabel = new Label("000000000", skin);
		tickers.put("score", scoreLabel);

		base.add(songinfo).expand().bottom().left();
		base.add(scoreLabel).bottom().right();
		
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
	
	public void notify(String message){
		
	}
}
