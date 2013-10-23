package com.sawtoothdev.mgoa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
		private final TextButton backButton, homeButton;
		
	TextButtonStyle elementStyle = new TextButtonStyle();
	TextButtonStyle controlStyle = new TextButtonStyle();
	
	private ClickListener elementClickListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {

			// get the TextButton that was clicked
			Actor actor = event.getListenerActor();
			
			// construct a FileHandle pointing at the path that the
			// TextButton represents
			FileHandle newPath = Gdx.files.external(actor.getName());

			if (newPath.isDirectory()) {
				updateTable(newPath);
			} else {
				Playthrough.songHandle = Gdx.files.external(actor.getName());
				Gdx.input.setInputProcessor(null);
				Resources.game.setScreen(new LoadScreen());
			}
			event.cancel();

			super.clicked(event, x, y);
		}
	};
	private ClickListener backClickListener = new ClickListener() {
		public void clicked(InputEvent event, float x, float y) {
			FileHandle parentDirectory = Gdx.files.external(backButton.getName());
			updateTable(parentDirectory);
			
			super.clicked(event, x, y);
		}
	};
	private ClickListener homeListener = new ClickListener(){
		public void clicked(InputEvent event, float x, float y) {
			Resources.game.setScreen(new MenuScreen());
		}
	};
	
	public ChooseSongScreen() {

		// stage setup
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		// root element
		container = new Table();
		container.setFillParent(true);
		stage.addActor(container);

		// directory and file list
		directoryTable = new Table();
		directoryTable.center();
		directoryTable.defaults().width(Gdx.graphics.getWidth());

		// styles
		elementStyle.font = new BitmapFont(Gdx.files.internal("data/fonts/naipol.fnt"), false);
		elementStyle.fontColor = Color.WHITE;
		controlStyle.font = new BitmapFont(Gdx.files.internal("data/fonts/typeone.fnt"), false);
		controlStyle.fontColor = Color.WHITE;
		
		// controls
		backButton = new TextButton("", controlStyle);
		backButton.addListener(backClickListener);
		homeButton = new TextButton("Main Menu", controlStyle);
		homeButton.addListener(homeListener);

		// container setup
		container.add(homeButton).expandX().left();
		container.row();
		container.add(new ScrollPane(directoryTable)).expand();
		container.row();
		container.add(backButton);
		
		// load root of external file system
		updateTable(Gdx.files.external(""));
	}

	public void updateTable(FileHandle directory) {

		directoryTable.clear();

		for (FileHandle fh : directory.list()) {

			String extension = fh.extension().toLowerCase();
			
			if (fh.isDirectory() || extension.contains("mp3") || extension.contains("ogg")) {

				TextButton element = new TextButton(fh.name(), elementStyle);
				element.setName(fh.path());
				element.addListener(elementClickListener);
				
				if (!fh.isDirectory())
					element.getLabel().setColor(Color.GREEN);

				directoryTable.add(element);
				directoryTable.row();
			}
		}
		
		backButton.setName(directory.parent().path());
		
		if (directory.path() == ""){
			backButton.setText("ROOT");
			backButton.setColor(Color.GRAY);
		}
		else {
			backButton.setText("^ PREVIOUS DIRECTORY ^");
			backButton.setColor(Color.WHITE);
		}
			

	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();

		Resources.defaultSpriteBatch.begin();
		Resources.defaultSpriteBatch.end();

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
