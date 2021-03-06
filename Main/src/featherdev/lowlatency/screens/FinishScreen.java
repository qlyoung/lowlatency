package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.lowlatency.LowLatency;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.ScoreRecords;
import featherdev.lowlatency.subsystems.Stats;

public class FinishScreen extends UiScreen {

	Table root;
	
	public FinishScreen(){
		game = LowLatency.instance();
		stage = new Stage();
		root = new Table();
		
		Gdx.input.setInputProcessor(stage);
		root.setFillParent(true);
		
		int totalPoints = Stats.points * Holder.difficulty.scoreMultiplier;
		ScoreRecords.instance().writeScore(Holder.song.getHandle(), totalPoints);
		
		String songinfo = Holder.song.getArtist() + " - " + Holder.song.getTitle();
		Label songLabel = new Label(songinfo, game.skin);
		TextButton okay = new TextButton("  Ok  ", game.skin);
		okay.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		okay.pad(0, 10, 0, 10);
		
		Table summary = new Table();
		summary.add(new Label("Raw Points", game.skin)).right().padRight(50);
		summary.add(new Label(String.valueOf(Stats.points), game.skin)).left().padLeft(50);
		summary.row();
		summary.add(new Label("Difficulty Multiplier", game.skin)).right().padRight(50);
		summary.add(new Label(String.valueOf(Holder.difficulty.scoreMultiplier), game.skin)).left().padLeft(50);
		summary.row();
		summary.add(new Label("Total Points", game.skin)).right().padRight(50);
		summary.add(new Label(String.valueOf(totalPoints), game.skin)).left().padLeft(50);

		root.add(songLabel).padBottom(80);
		root.row();
		root.add(summary);
		root.row();
		root.add(okay).padTop(70).minWidth(600);
		
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
	}

	public void render(float delta) {
		stage.act();
		stage.draw();
	}

    @Override
    public void dispose() {
        super.dispose();
    }
}
