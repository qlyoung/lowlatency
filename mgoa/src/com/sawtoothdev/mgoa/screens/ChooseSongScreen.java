package com.sawtoothdev.mgoa.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sawtoothdev.mgoa.Mgoa;
import com.sawtoothdev.mgoa.objects.Song;

public class ChooseSongScreen implements Screen {
	
	class FileBrowser extends Table {
		
		class ListItem {
			
			FileHandle handle;
			String text;
			
			public ListItem(String text, FileHandle handle){
				this.text = text;
				this.handle = handle;
			}
			
			@Override
			public String toString() {
				return text;
			}
		}

		FileHandle selection = null;
		Skin skin;
		TextButton upDirBtn;
		List viewer;
		ListItem[] curItems;

		public FileBrowser(FileHandle root, Skin skin) {
			this.skin = skin;
			
			ListStyle style = new ListStyle(game.skin.getFont("naipol"), Color.BLUE,
					Color.WHITE, skin.getDrawable("selector-listbg"));
			
			viewer = new List(new String[] {"dog"}, style);
			viewer.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					FileHandle newhandle = curItems[viewer.getSelectedIndex()].handle;
					if (newhandle.isDirectory())
						updateViewer(newhandle);
					else
						selection = newhandle;
				}
			});
			ScrollPane sp = new ScrollPane(viewer);
			
			updateViewer(root);
			
			this.add(sp).expand().fill();
			this.row();
			this.add(upDirBtn).bottom();
		}

		private void updateViewer(FileHandle directory) {

			if (!directory.isDirectory())
				Gdx.app.error("filebrowser", "not directory");

			FileHandle[] children = directory.list();
			ArrayList<ListItem> items = new ArrayList<ListItem>();
			
			for (FileHandle h : children){
				if (h.name().charAt(0) == '.')
					continue;
				
				items.add(new ListItem(h.name(), h));
			}
			
			
			if (directory.path() != "")
				items.add(new ListItem("<-- Back", directory.parent()));
			
			curItems = new ListItem[items.size()];
			items.toArray(curItems);
			
			viewer.setItems(curItems);
		}
		
		public FileHandle getSelection(){
			return selection;
		}
	}

	Stage stage;
	FileBrowser browser;
	Mgoa game;

	public ChooseSongScreen(Mgoa gam) {
		game = gam;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		TextButton homeButton = new TextButton("Main Menu", game.skin, "menuTextButtonStyle");
		homeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		
		FileHandle externalRoot = Gdx.files.external("");
		browser = new FileBrowser(externalRoot, game.skin);

		
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
		game.lights.update(delta);
		stage.act();
		if (browser.getSelection() != null)
			finish();

		// draw
		game.lights.draw(null);
		stage.draw();
	}

	private void finish(){
		Gdx.input.setInputProcessor(null);
		game.song = new Song(browser.getSelection());
		game.setScreen(new ConfigScreen(game));
	}
	
	@Override
	public void resize(int width, int height) {}
	@Override
	public void show() {}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void dispose() {}

}
