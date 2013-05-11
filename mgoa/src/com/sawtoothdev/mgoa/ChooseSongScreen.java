package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
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
	
	public ChooseSongScreen() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		
		container = new Table();
		container.setFillParent(true);
		stage.addActor(container);
		
		

		
		directoryTable = new Table();
		
		FileHandle external = Gdx.files.getFileHandle(Gdx.files.getExternalStoragePath(), FileType.Absolute);
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
		style.font = new BitmapFont();
		
		for (FileHandle fh : directory.list()){
			
			TextButton lol = new TextButton(fh.name(), style);
			lol.setName(fh.path());
			lol.addListener(new ClickListener() {
				
				@Override
				public boolean handle(Event event) {
					Actor actor = event.getListenerActor();
					
					System.out.println("hit!");
					
					FileHandle newPath = Gdx.files.getFileHandle(actor.getName(), FileType.Absolute);
					if (newPath.isDirectory())
						updateTable(newPath);
					else {
						Resources.currentSong = Gdx.files.getFileHandle(actor.getName(), FileType.Absolute);
						Resources.game.setScreen(Resources.loadScreen);
					}
					
					return false;
				}
			});
			
			container.add(lol);
			container.row();
			System.out.println(lol.getName());
		}
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
