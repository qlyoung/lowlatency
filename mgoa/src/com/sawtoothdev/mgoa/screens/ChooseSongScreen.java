package com.sawtoothdev.mgoa.screens;

import java.util.Hashtable;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.MainGame;
import com.sawtoothdev.mgoa.objects.Song;

public class ChooseSongScreen implements Screen {
	
	class FileBrowser extends Table {

		private Hashtable<TextButton, FileHandle> currentElements;
		FileHandle selection = null;
		
		TextButton upDirBtn;
		private Table viewer;
		private ClickListener elementListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				Actor actor = event.getListenerActor();

				FileHandle choice = currentElements.get(actor);
				
				if (choice.isDirectory())
					updateViewer(choice);
				else 
					selection = choice;

				event.cancel();
			}
		};

		public FileBrowser(FileHandle root) {
			currentElements = new Hashtable<TextButton, FileHandle>();
			viewer = new Table();
			ScrollPane sp = new ScrollPane(viewer);
			upDirBtn = new TextButton("Root", MainGame.Ui.skin, "menuTextButtonStyle");
			
			viewer.defaults().padBottom(10);
			
			upDirBtn.addListener(elementListener);
			updateViewer(root);
			
			this.add(sp).expand().fill();
			this.row();
			this.add(upDirBtn).bottom();
		}

		private void updateViewer(FileHandle directory) {

			// don't play tricks on me
			if (!directory.isDirectory())
				Gdx.app.error("filebrowser", "not directory");

			// clear the viewer & element map
			viewer.clear();
			currentElements.clear();

			// repopulate the element map with the children of the new directory
			for (FileHandle child : directory.list()) {
				
				// skip dot[files|directories]
				if (child.name().charAt(0) == '.')
					continue;
				
				TextButton element = new TextButton(child.name(), MainGame.Ui.skin, "menuTextButtonStyle");
				element.setName(child.path());
				element.addListener(elementListener);
				
				currentElements.put(element, child);
			}
			
			// update the viewer to match the element map
			for (Entry<TextButton, FileHandle> e : currentElements.entrySet()){
				FileHandle handle = e.getValue();
				TextButton element = e.getKey();

				if (!handle.isDirectory()){
					String ext = handle.extension();
					if (ext.equalsIgnoreCase("mp3") || ext.equalsIgnoreCase("ogg"))
						element.getLabel().setColor(Color.GREEN);
				}
				
				viewer.add(element).expandX();
				viewer.row();
			}

			currentElements.put(upDirBtn, directory.parent());

			if (directory.path() == "") {
				upDirBtn.setText("ROOT");
				upDirBtn.setColor(Color.BLACK);
			} else {
				upDirBtn.setText("^ Previous Directory ^");
				upDirBtn.setColor(Color.WHITE);
			}

		}
		
		public FileHandle getSelection(){
			return selection;
		}
	}

	private final Stage stage;
	FileBrowser browser;
	SelectBox selector;

	TextButtonStyle controlStyle = new TextButtonStyle();

	public ChooseSongScreen() {
		
		// stage setup
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		// controls
		TextButton homeButton = new TextButton("Main Menu", MainGame.Ui.skin, "menuTextButtonStyle");
		homeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				MainGame.game.setScreen(new MenuScreen());
			}
		});

		// layout
		browser = new FileBrowser(Gdx.files.external(""));
		
		Table root = new Table();
		root.setFillParent(true);
		
		root.add(homeButton);
		root.row();
		root.add(browser).expand().fill();

		stage.addActor(root);
	}

	@Override
	public void render(float delta) {

		// update
		MainGame.Gfx.lights.update(delta);
		stage.act();
		if (browser.getSelection() != null)
			finish();

		// draw
		MainGame.Gfx.lights.draw(null);
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
	
	private void finish(){
		Gdx.input.setInputProcessor(null);
		MainGame.Temporal.song = new Song(browser.getSelection());
		MainGame.game.setScreen(new ConfigScreen());
	}

}
