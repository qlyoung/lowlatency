package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuScreen implements Screen {

	public boolean switchScreen = false;
	
	private Stage stage;
	
	public MenuScreen(){
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		// setup menu UI
		Table table = new Table();
		
		{
			TextButtonStyle style = new TextButtonStyle();
			style.font = new BitmapFont();
			style.fontColor = Color.GREEN;
			style.up = new TextureRegionDrawable(new TextureRegion(new Texture("data/textures/menubutton.png"), 128, 30));
			
			TextButton playButton = new TextButton("Play", style);
			playButton.addListener(new ClickListener() {
				
				@Override
				public boolean handle(Event event) {
					switchScreen = true;
					return true;
				}
			});
			TextButton optionsButton = new TextButton("Options", style);
			table.add(playButton);
			table.row();
			table.add(optionsButton);
		}

		table.setFillParent(true);
		stage.addActor(table);
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
		if (switchScreen)
			Resources.game.setScreen(Resources.chooseSongScreen);
		
	}
	
	

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Resources.menuMusic.setLooping(true);
		Resources.menuMusic.play();
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
