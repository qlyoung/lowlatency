package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PreviewScreen implements Screen {

	private Stage stage = new Stage();
	
	public PreviewScreen(final BeatMap map, final FileHandle fileHandle) {
		Gdx.input.setInputProcessor(this.stage);
		
		LabelStyle lStyle = new LabelStyle();
		lStyle.font = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
		TextButtonStyle tStyle = new TextButtonStyle();
		tStyle.font = lStyle.font;
		
		Label diff = new Label("Difficulty - " + Resources.difficulty.name.toString(), lStyle);
		
		int totalBeats = 0;
		switch (Resources.difficulty.name){
		case EASY:
			totalBeats = map.easy.size();
			break;
		case NORMAL:
			totalBeats = map.medium.size();
			break;
		case HARD:
			totalBeats = map.hard.size();
			break;
		case ORIGINAL:
			totalBeats = map.ORIGINAL.size();
			break;
		}
		
		Label beatCount = new Label("Total beats - " + String.valueOf(totalBeats), lStyle);
		Label length = new Label("Song Length - ", lStyle);
		TextButton tButton = new TextButton("Play", tStyle);
		tButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Resources.game.setScreen(new PlayScreen(map, fileHandle));
				super.clicked(event, x, y);
			}
		});
		
		Table container = new Table();
		container.add(diff);
		container.row();
		container.add(length);
		container.row();
		container.add(beatCount);
		container.row();
		container.add(tButton);
		container.row();
		
		container.setFillParent(true);
		
		stage.addActor(container);
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act();
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
