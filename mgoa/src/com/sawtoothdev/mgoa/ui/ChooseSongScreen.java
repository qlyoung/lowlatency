package com.sawtoothdev.mgoa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Resources;
import com.sawtoothdev.mgoa.Song;
import com.sawtoothdev.mgoa.game.Playthrough;

public class ChooseSongScreen implements Screen {

	private final Stage stage;
	private final Table root;
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
				Playthrough.song = new Song(Gdx.files.external(actor.getName()));
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
		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		// directory and file list
		directoryTable = new Table();
		directoryTable.center();
		directoryTable.defaults().width(Gdx.graphics.getWidth());

		// styles
		elementStyle.font = Resources.uiFnt;
		elementStyle.fontColor = Color.WHITE;
		controlStyle.font = new BitmapFont(Gdx.files.internal("data/fonts/typeone.fnt"), false);
		controlStyle.fontColor = Color.WHITE;
		
		// controls
		backButton = new TextButton("", controlStyle);
		backButton.addListener(backClickListener);
		homeButton = new TextButton("Main Menu", controlStyle);
		homeButton.addListener(homeListener);

		// container setup
		root.add(homeButton).expandX().left();
		root.row();
		root.add(new ScrollPane(directoryTable)).expand();
		root.row();
		root.add(backButton);
		
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
