package featherdev.mgoa.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import featherdev.mgoa.Mgoa;
import featherdev.mgoa.objects.Song;

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

		ClickListener viewerListener = new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				FileHandle newhandle = curItems[viewer.getSelectedIndex()].handle;
				
				if (newhandle.isDirectory())
					updateViewer(newhandle);
				else {
					String extension = newhandle.extension().toLowerCase();
					boolean isAudio = extension.contains("mp3") || extension.contains("ogg");
					
					if (isAudio)
						selection = newhandle;
					else {
						
						final Dialog dialog = new Dialog("", game.skin);
						TextButton ok = new TextButton("Ok", game.skin);
						ok.center().pad(0f, 30, 0, 30);
						ok.addListener(new ClickListener(){
							public void clicked(InputEvent event, float x, float y) {
								dialog.cancel();
							};
						});
						dialog.text("File type not supported");
						dialog.button(ok);
						Dialog.fadeDuration = .2f;
						
						dialog.show(stage);
					}
				}
			}
		};
		
		FileHandle selection = null;
		Skin skin;
		TextButton upDirBtn;
		List viewer;
		ListItem[] curItems;

		public FileBrowser(FileHandle root, Skin skin) {
			this.skin = skin;
			
			viewer = new List(new String[] {"why do we travel to the mines / with our own chests to dig in?"}, game.skin);
			viewer.addListener(viewerListener);
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

	public ChooseSongScreen() {
		game = Mgoa.getInstance();
		stage = new Stage();

		TextButton homeButton = new TextButton("Return", game.skin);
		homeButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				game.setScreen(new MenuScreen());
			}
		});
		browser = new FileBrowser(Gdx.files.external(""), game.skin);
		Table ctlbar = new Table(game.skin);
		ctlbar.add(homeButton);

		Table root = new Table(game.skin);
		root.add("Select Song").colspan(2).center().pad(5, 0, 15, 0);
		root.row();
		root.add(browser).expand().fill();
		root.row();
		root.add(homeButton).bottom().pad(10, 0, 5, 0);
		root.setFillParent(true);

		stage.addActor(root);
		stage.getRoot().getColor().a = 0;
		Gdx.input.setInputProcessor(stage);
	}

	private void finish(){
		Gdx.input.setInputProcessor(null);
		game.song = new Song(browser.getSelection());
		game.setScreen(new ConfigScreen());
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
	@Override
	public void resize(int width, int height) {}
	@Override
	public void show() {
		stage.addAction(Actions.fadeIn(.5f));
	}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void dispose() {
		stage.dispose();
	}

}
