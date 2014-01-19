package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MainGame;

public class ConfigScreen implements Screen {
	
	private Stage stage;
	private Table root;
	
	SelectBox selector;
	
	public ConfigScreen(){
		
		stage = new Stage();
		root = new Table();
		
		root.setFillParent(true);
		Gdx.input.setInputProcessor(stage);
		
		String[] selections = new String[MainGame.util.difficulties.length];
		for (int i = 0; i < MainGame.util.difficulties.length; i++)
			selections[i] = MainGame.util.difficulties[i].name;
		selector = new SelectBox(selections, MainGame.ui.uiSelectBoxStyle);
		
		Label lbl = new Label("Difficulty: ", MainGame.ui.uiLabelStyle);
		TextButton playButton = new TextButton("Play", MainGame.ui.uiTextButtonStyle);
		TextButton backToMenu = new TextButton("MAIN MENU", MainGame.ui.uiTextButtonStyle);
		playButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				finish();
				super.clicked(event, x, y);
			}
		});
		backToMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainGame.game.setScreen(new MenuScreen());
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
		MainGame.gfx.lights.update(delta);
		stage.act();
		
		MainGame.gfx.lights.draw(null);
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
	
	private void finish(){
		int index = selector.getSelectionIndex();
		
		MainGame.temporals.difficulty = MainGame.util.difficulties[index];
		
		Gdx.input.setInputProcessor(null);
		MainGame.game.setScreen(new LoadScreen());
	}

}
