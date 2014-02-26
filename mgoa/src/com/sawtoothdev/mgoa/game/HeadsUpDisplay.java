package com.sawtoothdev.mgoa.game;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.GameScreen;
import com.sawtoothdev.mgoa.objects.Song;
import com.sawtoothdev.mgoa.objects.Stats;

public class HeadsUpDisplay extends Stage {
	
	private HashMap<String, Actor> tickers;
	private Stats stats;
	private Skin skin;
	
	public HeadsUpDisplay(Song song, Skin skn, Stats stat, SpriteBatch batch, final GameScreen gs){
		super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
		
		skin = skn;
		stats = stat;
		tickers = new HashMap<String, Actor>();

		String songinfo = song.getArtist() + " - " + song.getTitle();
		
		Label scoreLabel = new Label("000000000", skin);
		tickers.put("score", scoreLabel);

		ImageButton pause = new ImageButton(skin);
		pause.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gs.pause();
			}
		});
		
		Table table = new Table(skin);
		//table.getColor().a = 0;
		table.setFillParent(true);
		table.add(pause).top().left();
		table.row().expandY();
		table.add(songinfo).expandX().bottom().left().pad(5);
		table.add(scoreLabel).bottom().right().pad(5);
		
		addActor(table);
	}
	
	@Override
	public void act(float delta) {
		Label scoreLabel = (Label) tickers.get("score");
		scoreLabel.setText(String.valueOf(stats.points));
		
		super.act(delta);
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
		addActor(msg);
	}
	public void fadein(float time){
		getRoot().addAction(Actions.fadeIn(time));
	}
	public void fadeout(float time){
		getRoot().addAction(Actions.fadeOut(time));
	}
	public void present(){
		fadein(3);
	}
	public float getAlpha(){
		return getRoot().getColor().a;
	}

}
