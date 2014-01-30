package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Mgoa;

public class PausedScreen implements Screen {
	
	final Stage stage;
	final Table root;
	final Mgoa game;
	final Camera camera;
	
	public PausedScreen(Mgoa gam) {
		game = gam;
		camera = new OrthographicCamera();
		stage = new Stage();
		root = new Table();
		Gdx.input.setInputProcessor(stage);
		
		TextButton resume = new TextButton("Resume", game.skin);
		TextButton quitToMenu = new TextButton("Quit to Main Menu", game.skin);
		
		resume.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
			}
		});
		quitToMenu.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setInputProcessor(null);
				game.setScreen(new MenuScreen(game));
				super.clicked(event, x, y);
			}
		});
		
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
