package com.sawtoothdev.mgoa.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MainGame;

public class PausedScreen implements Screen {
	
	private Stage stage;
	private Table root;
	
	private Sprite playButton = new Sprite(new Texture("ui/play.png"));
	
	private GameScreen home;
	
	public PausedScreen(GameScreen home) {
		
		this.home = home;
		
		// stage setup
		stage = new Stage();
		root = new Table();
		Gdx.input.setInputProcessor(stage);
		
		// actors
		playButton.setPosition(5, Gdx.graphics.getHeight() - playButton.getHeight() - 5);
		TextButton resume = new TextButton("Resume", MainGame.Ui.skin, "menuTextButtonStyle");
		TextButton quitToMenu = new TextButton("Quit to Main Menu", MainGame.Ui.skin, "menuTextButtonStyle");
		
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
				MainGame.game.setScreen(new MenuScreen());
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
		
		// check play
		if (Gdx.input.justTouched()) {
			Vector2 lastTouch =
					new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			Rectangle spriteBox = playButton.getBoundingRectangle();

			if (spriteBox.contains(lastTouch.x, lastTouch.y))
				returnToGame();
		}
		stage.act();
		
		stage.draw();
		MainGame.Gfx.systemBatch.setProjectionMatrix(MainGame.Gfx.screenCam.combined);
		MainGame.Gfx.systemBatch.begin();
		playButton.draw(MainGame.Gfx.systemBatch);
		MainGame.Gfx.systemBatch.end();
	}

	public void returnToGame(){
		Gdx.input.setInputProcessor(null);
		MainGame.game.setScreen(home);
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
