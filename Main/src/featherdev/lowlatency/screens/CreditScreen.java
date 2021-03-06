package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.lowlatency.LowLatency;

public class CreditScreen extends UiScreen {
	
	public CreditScreen (final LowLatency game){
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		Table root = new Table();
		root.setSkin(game.skin);
		root.setFillParent(true);
		
		root.add("Designed and programmed by Quentin Young");
		root.row();
		root.add("featherdev@gmail.com");
		root.row();
		root.add(" ");
		root.row();
		root.add("Music and sound effects by John Hughes");
		root.row();
		root.add("syrinxtunes.bandcamp.com");
		root.row();
		
		TextButton backtomenu = new TextButton("Return", game.skin);
		backtomenu.addListener(new ClickListener(){
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen());
			};
		});
		
		root.add(" ").row();
		root.add(" ").row();
		root.add(backtomenu);
		
		
		stage.addActor(root);
	}

	public void render(float delta) {
		stage.act();
		stage.draw();

	}
}
