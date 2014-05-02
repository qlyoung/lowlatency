package featherdev.mgoa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.objects.IDrawable;
import featherdev.mgoa.objects.IUpdateable;
import featherdev.mgoa.subsystems.Stats;

public class HeadsUpDisplay implements IUpdateable, IDrawable, Disposable {
	
	Stage stage;
	Skin skin;
	
	public HeadsUpDisplay(){
		
		// ui
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		skin = Mgoa.instance().skin;
		ImageButton pause = new ImageButton(skin);
		Label scoreLabel = new Label("000000000", skin);
		scoreLabel.setName("score");
		Label songinfo = new Label("", skin);
		songinfo.setName("songinfo");
		Table table = new Table(skin);
		table.setFillParent(true);
		table.add(pause).top().left();
		table.add(scoreLabel).top().right().padRight(10);
		table.row().expandY();
		table.add(songinfo).expandX().bottom().left().pad(5);
		
		stage.addActor(table);
		stage.getRoot().getColor().a = 0;
	}

	public void update(float delta) {
		// update points
		Label score = (Label) stage.getRoot().findActor("score");
		String text = String.format("%08d", Stats.instance().points);		
		score.setText(text);
		
		stage.act(delta);
	}
	public void draw(SpriteBatch batch) {
		stage.draw();
	}
	
	public void setSongInfo(String artist, String title){
		Label songinfo = (Label) stage.getRoot().findActor("songinfo");
		songinfo.setText(artist + " - " + title);
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
	public void dispose() {
		stage.dispose();
	}
}
