package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ChooseSongScreen implements Screen {

	private final Stage stage;
	private final Table container;
	private final Table directoryTable;
	
	private ClickListener elementClickListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			Actor actor = event.getListenerActor();
			FileHandle newPath = Gdx.files.external(actor.getName());
			
			if (newPath.isDirectory()) {
				updateTable(newPath);
			}
			else {
				Resources.currentSong = Gdx.files.external(actor.getName());
				System.out.println(Resources.currentSong.path());
				Gdx.input.setInputProcessor(null);
				Resources.game.setScreen(new LoadScreen());
			}
			event.cancel();
			
			super.clicked(event, x, y);
		}
	};
	
	public ChooseSongScreen() {
		
		{// stage setup
			stage = new Stage();
			Gdx.input.setInputProcessor(stage);
			
			//root element
			container = new Table();
			container.setFillParent(true);
			stage.addActor(container);
			
			//directory and file list
			directoryTable = new Table();
		}
		
		FileHandle external = Gdx.files.external("");
		updateTable(external);
		
		container.add(new ScrollPane(directoryTable));
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
		
	}

	
	public void updateTable(FileHandle directory){
		
		TextButtonStyle style = new TextButtonStyle();
		style.font = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
		directoryTable.clear();
		
		for (FileHandle fh : directory.list()){
			
			TextButton lol = new TextButton(fh.name(), style);
			lol.setName(fh.path());
			lol.addListener(elementClickListener);
			
			directoryTable.add(lol);
			directoryTable.row();
		}
		
		TextButton up = new TextButton("UP", style);
		up.setName(directory.parent().path());
		up.addListener(elementClickListener);
		directoryTable.add(up);
		directoryTable.row();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		
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
