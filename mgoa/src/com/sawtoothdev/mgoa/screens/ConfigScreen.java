package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Mgoa;

public class ConfigScreen implements Screen {
	
	Stage stage;
	Table root;
	SelectBox selector;
	Mgoa game;
	
	public ConfigScreen(Mgoa gam){
		game = gam;
		stage = new Stage();
		root = new Table();
		
		root.setFillParent(true);
		Gdx.input.setInputProcessor(stage);
		
		String[] selections = new String[game.difficulties.length];
		for (int i = 0; i < game.difficulties.length; i++)
			selections[i] = game.difficulties[i].name;
		selector = new SelectBox(selections, game.skin, "menuSelectBoxStyle");
		
		TextButtonStyle tbstyle = game.skin.get("menuTextButtonStyle", TextButtonStyle.class);
		Label lbl = new Label("Difficulty: ", game.skin, "menuLabelStyle");
		TextButton playButton = new TextButton("Play", tbstyle);
		TextButton backToMenu = new TextButton("MAIN MENU", tbstyle);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int index = selector.getSelectionIndex();
				game.difficulty = game.difficulties[index];
				Gdx.input.setInputProcessor(null);
				game.setScreen(new LoadScreen(game));
				super.clicked(event, x, y);
			}
		});
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
				super.clicked(event, x, y);
			}
		});
		
		Table controlBar = new Table();
		controlBar.add(backToMenu);
		
		Table configPane = new Table();
		configPane.add(lbl);
		configPane.add(selector);
		configPane.row();
		configPane.add(playButton).colspan(2).center();
		
		root.add(controlBar);
		root.row();
		root.add(configPane).expandY();
		
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
		// TODO Auto-generated method stub
		
	}

}
