package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ConfigScreen implements Screen {
	
	Stage stage;
	Table root;
	SelectBox selector;
	Mgoa game;
	
	public ConfigScreen(){
		game = Mgoa.getInstance();
		
		String[] selections = new String[game.difficulties.length];
		for (int i = 0; i < game.difficulties.length; i++)
			selections[i] = game.difficulties[i].name;
		selector = new SelectBox(selections, game.skin);
		selector.setSelection(1);
		
		Label lbl = new Label("Difficulty: ", game.skin);
		TextButton playButton = new TextButton("Play", game.skin);
		TextButton backToMenu = new TextButton("Main Menu", game.skin);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int index = selector.getSelectionIndex();
				game.difficulty = game.difficulties[index];
				Gdx.input.setInputProcessor(null);
				dispose();
				game.setScreen(new LoadScreen());
				super.clicked(event, x, y);
			}
		});
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		root = new Table();
		root.setFillParent(true);
		
		Table controlBar = new Table();
		controlBar.add(backToMenu).left();;
		
		Table content = new Table();
		content.add(lbl);
		content.add(selector);
		content.row();
		content.add(playButton).colspan(2).fillX();
		
		root.add(controlBar).expandX().fillX();
		root.row();
		root.add(content).expandY();
		
		stage.addActor(root);
	}

	@Override
	public void render(float delta) {
		game.lights.update(delta);
		stage.act();
		
		game.lights.draw(null);
		stage.draw();
		
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		stage.dispose();
	}

}
