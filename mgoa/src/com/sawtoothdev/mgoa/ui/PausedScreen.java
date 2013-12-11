package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.UIResources;
import com.sawtoothdev.mgoa.game.GameScreen;

public class PausedScreen implements Screen {
	
	private Stage stage;
	private Table root;
	
	private GameScreen home;
	
	public PausedScreen(GameScreen home) {
		
		this.home = home;
		
		// stage setup
		stage = new Stage();
		root = new Table();
		Gdx.input.setInputProcessor(stage);
		
		// actors
		
		TextButton resume = new TextButton("Resume", UIResources.uiTextButtonStyle);
		
		TextButton quitToMenu = new TextButton("Quit to Main Menu", UIResources.uiTextButtonStyle);
		
		// actor setup
		resume.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				returnToGame();
				super.clicked(event, x, y);
			}
		});
		quitToMenu.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(null);
				Resources.game.setScreen(new MenuScreen());
				super.clicked(event, x, y);
			}
		});
		
		// layout setup
		root.setFillParent(true);
		root.add(resume);
		root.row();
		root.add(quitToMenu);
		stage.addActor(root);
	}
	
	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	public void returnToGame(){
		Gdx.input.setInputProcessor(null);
		Resources.game.setScreen(home);
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
		
	}

	@Override
	public void hide() {
		
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void resume() {
		
		
	}

	@Override
	public void dispose() {
		
		
	}

}
