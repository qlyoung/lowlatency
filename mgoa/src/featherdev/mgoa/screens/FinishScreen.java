package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.Mgoa;

public class FinishScreen extends UiScreen {

	Table root;
	
	public FinishScreen(int points){
		game = Mgoa.getInstance();
		stage = new Stage();
		root = new Table();
		
		Gdx.input.setInputProcessor(stage);
		root.setFillParent(true);
		
		int totalPoints = points * game.difficulty.scoreMultiplier;
		game.records.writeScore(game.song.getHandle(), totalPoints);
		
		String songinfo = game.song.getArtist() + " - " + game.song.getTitle();
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
		summary.add(new Label("Raw Points", game.skin)).left();
		summary.add(new Label(String.valueOf(points), game.skin)).right();
		summary.row();
		summary.add(new Label("Difficulty Multiplier", game.skin)).left();
		summary.add(new Label(String.valueOf(game.difficulty.scoreMultiplier), game.skin)).right();
		summary.row();
		summary.add(new Label("Total Points", game.skin)).left();
		summary.add(new Label(String.valueOf(totalPoints), game.skin)).right();

		root.add(songLabel);
		root.row();
		root.add(summary);
		root.row();
		root.add(okay);
		
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
	}

	public void render(float delta) {
		
		stage.act();
		stage.draw();
	}

}