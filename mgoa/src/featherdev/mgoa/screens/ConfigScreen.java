package featherdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ConfigScreen extends UiScreen {
	
	Table root;
	SelectBox selector;
	
	public ConfigScreen(){
		String[] selections = new String[game.difficulties.length];
		for (int i = 0; i < game.difficulties.length; i++)
			selections[i] = game.difficulties[i].name;
		selector = new SelectBox(selections, game.skin);
		selector.setSelection(1);
		Label lbl = new Label("Difficulty: ", game.skin);
		final CheckBox visualizerMode = new CheckBox(" Visualizer mode", game.skin);
		visualizerMode.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				super.clicked(event, x, y);
			}
		});
		TextButton backToMenu = new TextButton("Return", game.skin);
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new ChooseSongScreen());
				super.clicked(event, x, y);
			}
		});
		TextButton playButton = new TextButton("Play", game.skin);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int index = selector.getSelectionIndex();
				game.difficulty = game.difficulties[index];
				game.visualizer = visualizerMode.isChecked();
				game.setScreen(new LoadScreen());
				
				super.clicked(event, x, y);
				Gdx.input.setInputProcessor(null);
				dispose();
			}
		});
		Table controlBar = new Table();
		controlBar.add(backToMenu).expandX().left().pad(10);
		Table content = new Table();
		content.defaults().pad(10);
		content.add(lbl);
		content.add(selector);
		content.row();
		content.add(playButton).colspan(2).fillX();
		content.row();
		content.add(visualizerMode).colspan(2).fillX().padTop(25);
		
		root = new Table();
		root.add(content).expandY().center();
		root.row();
		root.add(backToMenu).padBottom(15);
		root.setFillParent(true);
		
		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
		Gdx.input.setInputProcessor(stage);
	}

	public void render(float delta) {
		game.lights.update(delta);
		stage.act();
		
		game.lights.draw(null);
		stage.draw();
		
	}

}
