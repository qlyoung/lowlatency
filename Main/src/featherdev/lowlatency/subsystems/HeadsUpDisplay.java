package featherdev.lowlatency.subsystems;

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

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.objects.IDrawable;
import featherdev.lowlatency.objects.IUpdateable;

public class HeadsUpDisplay implements IUpdateable, IDrawable, Disposable {
	
	private static HeadsUpDisplay instance;
	public static HeadsUpDisplay instance(){
		if (instance == null)
			instance = new HeadsUpDisplay();
		return instance;
	}

    Viewport viewport;
	Stage stage;
	Skin skin;
	
	private HeadsUpDisplay(){
        //viewport
        viewport = new ScreenViewport();
		// layout & styling
        stage = new Stage(viewport);
		skin = LowLatency.instance().skin;
        // ui
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
	public void showEvaporatingMessage(String message, Vector2 screenPosition, float live){
		Label msg = new Label(message, skin);
		msg.setPosition(screenPosition.x, screenPosition.y);
		Action s = Actions.parallel(
				Actions.moveTo(screenPosition.x, screenPosition.y + 100, live),
				Actions.fadeOut(live));
		Action r = Actions.sequence(s, Actions.removeActor());
		msg.addAction(r);
		stage.addActor(msg);
	}
	public void showMessage(String message, Vector2 screenPosition, float live, float fadetime){
		Label msg = new Label(message, skin);
		msg.setPosition(screenPosition.x, screenPosition.y);
		Action composite = Actions.sequence(
				Actions.fadeIn(fadetime),
				Actions.delay(live),
				Actions.fadeOut(fadetime),
				Actions.removeActor());

		msg.addAction(composite);
		stage.addActor(msg);
	}
	public void fadein(float time){
		stage.getRoot().addAction(Actions.fadeIn(time));
	}
	public void fadeout(float time){
		stage.getRoot().addAction(Actions.fadeOut(time));
	}
	public float getAlpha(){
		return stage.getRoot().getColor().a;
	}
	public void dispose() {
		stage.dispose();
	}
}
