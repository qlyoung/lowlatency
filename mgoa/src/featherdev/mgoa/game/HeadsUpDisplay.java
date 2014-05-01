package featherdev.mgoa.game;

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

import featherdev.mgoa.IDrawable;
import featherdev.mgoa.IUpdateable;
import featherdev.mgoa.Mgoa;
import featherdev.mgoa.objects.Song;
import featherdev.mgoa.screens.GameScreen;

public class HeadsUpDisplay implements IUpdateable, IDrawable, Disposable {
	
	Stage stage;
	HashMap<String, Actor> tickers;
	Skin skin;
	OrthographicCamera screencam;
	
	public HeadsUpDisplay(Song song, final GameScreen gs){
		screencam = new OrthographicCamera();
		screencam.setToOrtho(false);
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		skin = Mgoa.getInstance().skin;
		String songinfo = song.getArtist() + " - " + song.getTitle();
		ImageButton pause = new ImageButton(skin);
		pause.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gs.pause();
			}
		});
		Label scoreLabel = new Label("000000000", skin);
		Label accuracy   = new Label("0", skin);
		Table table = new Table(skin);
		table.setFillParent(true);
		table.add(pause).top().left();
		table.add(scoreLabel).top().right().padRight(10);
		//table.add(accuracy).top().right().expandX();
		table.row().expandY();
		table.add(songinfo).expandX().bottom().left().pad(5);
		
		stage.addActor(table);
		stage.getRoot().getColor().a = 0;
		
		tickers = new HashMap<String, Actor>();
		tickers.put("points", scoreLabel);
		tickers.put("accuracy", accuracy);
	}

	@Override
	public void update(float delta) {
		stage.act(delta);
	}
	@Override
	public void draw(SpriteBatch batch) {
		stage.draw();
	}
		
	public void showMessage(String message, Vector2 screenPosition, float fadetime, float live){
		Label msg = new Label(message, skin);
		msg.setPosition(screenPosition.x, screenPosition.y);
		Action set = Actions.sequence(
				Actions.fadeIn(fadetime),
				Actions.delay(live),
				Actions.fadeOut(fadetime),
				Actions.removeActor());
		msg.addAction(set);
		stage.addActor(msg);
	}
	public void showPlayDialog(ClickListener l){
		Dialog.fadeDuration = .5f;
		Dialog playdialog = new Dialog("", Mgoa.getInstance().skin);
		playdialog.text("  Touch anywhere  ");
		playdialog.addListener(l);
		playdialog.show(stage);
		
	}
	public void setPoints(int points) {
		Label scoreLabel = (Label) tickers.get("points");
		String text = String.format("%08d", points);
		
		scoreLabel.setText(text);
	}
	public void setAccuracy(float avgaccuracy){
		Label accuracy = (Label) tickers.get("accuracy");
		accuracy.setText(String.valueOf(avgaccuracy));
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