package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen {

	private Stage stage = new Stage();
	private Visualizer visualizer;

	
	public MenuScreen() {
		Gdx.input.setInputProcessor(stage);
		visualizer = new Visualizer(Resources.menuMusic);
		
		TextButtonStyle style = new TextButtonStyle();
		style.font = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
		style.up = new TextureRegionDrawable(new TextureRegion(new Texture("data/textures/menubutton.png"), 256, 40));
		
		
		
		TextButton
			playButton = new TextButton("Play", style),
			optionsButton = new TextButton("Options", style),
			statsButton = new TextButton("Leaderboards", style),
			creditsButton = new TextButton("Credits", style);
		
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Resources.game.setScreen(new ChooseSongScreen());
				super.clicked(event, x, y);
			}
		});

		Table table = new Table();
		table.setFillParent(true);
		table.defaults().uniform().padBottom(10);
		
		table.add(playButton);
		table.row();
		table.add(optionsButton);
		table.row();
		table.add(statsButton);
		table.row();
		table.add(creditsButton);
		table.row();
		
		stage.addActor(table);
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		visualizer.render(delta);
		
		stage.act();
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Resources.menuMusic.setLooping(true);
		Resources.menuMusic.play();
		System.gc();
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
