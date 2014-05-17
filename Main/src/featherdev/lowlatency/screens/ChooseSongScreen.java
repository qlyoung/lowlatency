package featherdev.lowlatency.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import featherdev.lowlatency.objects.Song;
import featherdev.lowlatency.subsystems.Holder;
import featherdev.lowlatency.subsystems.LightTank;

import java.util.ArrayList;

public class ChooseSongScreen extends UiScreen {
	
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
					
					if (isAudio) {
                        selection = newhandle;
                    }
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

            viewer = new List(game.skin);
            viewer.setItems(new String[] {"why do we travel to the mines / with our own chests to dig in?"});
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
	FileBrowser browser;

	public ChooseSongScreen() {
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
		Holder.song = new Song(browser.getSelection());
		game.setScreen(new ConfigScreen());
		game.setScreen(new ConfigScreen());
	}
	@Override
	public void render(float delta) {

		// update
		LightTank.instance().update(delta);
		stage.act();
		if (browser.getSelection() != null)
			finish();

		// draw
		LightTank.instance().draw(null);
		stage.draw();
	}

}
